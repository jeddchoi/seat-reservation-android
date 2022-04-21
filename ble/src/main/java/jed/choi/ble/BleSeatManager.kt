package jed.choi.ble

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.experimental.and


@Singleton
class BleSeatManager @Inject constructor(
    @ApplicationContext private val context: Context,
//    private val scanner: BluetoothLeScannerCompat
) {
    private fun getScanSettings(): ScanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
        .setNumOfMatches(1)
        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
        .setReportDelay(10000)
        .build()

    private fun getScanFilter(
        uuid: String,
        macAddress: String,
        seatName: String,
        sectionNumber: Int,
        seatNumber: Int
    ): ArrayList<ScanFilter> {
        val manufacturerData = ByteArray(23)
        System.arraycopy(UuidToByteArray(uuid), 0, manufacturerData, 2, 16)
        System.arraycopy(integerToByteArray(sectionNumber), 0, manufacturerData, 18, 2)
        System.arraycopy(integerToByteArray(seatNumber), 0, manufacturerData, 20, 2)

        return arrayListOf(
            ScanFilter.Builder()
                .setDeviceAddress(macAddress)
                .setDeviceName(seatName)
                .setManufacturerData(MANUFACTURER_ID, manufacturerData, UUID_MASK)
                .setManufacturerData(MANUFACTURER_ID, manufacturerData, MAJOR_BYTEARRAY_MASK)
                .setManufacturerData(MANUFACTURER_ID, manufacturerData, MINOR_BYTEARRAY_MASK)
                .build(),
        )
    }


    fun startScan(
        uuid: String,
        macAddress: String,
        seatName: String,
        sectionNumber: Int,
        seatNumber: Int,
    ) {
        BluetoothLeScannerCompat.getScanner().startScan(
            getScanFilter(uuid, macAddress, seatName, sectionNumber, seatNumber),
            getScanSettings(),
            context,
            PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_ALL_MATCHES,
                Intent(context, BleScanReceiver::class.java).apply {
                    action = ACTION_FOUND_DEVICE
                },
                PendingIntent.FLAG_UPDATE_CURRENT
            ),
            REQUEST_CODE_ALL_MATCHES
        )
    }

    fun stopScan() {
        // To stop scanning use the same PendingIntent and request code as one used to start scanning.
        BluetoothLeScannerCompat.getScanner().stopScan(
            context,
            PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_ALL_MATCHES,
                Intent(context, BleScanReceiver::class.java).apply { action = ACTION_FOUND_DEVICE },
                PendingIntent.FLAG_CANCEL_CURRENT
            ),
            REQUEST_CODE_ALL_MATCHES
        )
    }


    companion object {
        const val ACTION_FOUND_DEVICE = "jed.choi.seatreservation.ACTION_FOUND_DEVICE"
        const val REQUEST_CODE_ALL_MATCHES = 1001
        const val MANUFACTURER_ID = 0x59 // Nordic Semiconductor
        const val EXTRA_SEAT_STATE = "jed.choi.seatreservation.SEAT_STATE"
        val UUID_MASK = byteArrayOf(
            0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0
        )
        val MAJOR_BYTEARRAY_MASK = byteArrayOf(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0
        )
        val MINOR_BYTEARRAY_MASK = byteArrayOf(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0
        )
    }
}


/**
 * see https://github.com/inthepocket/ibeacon-scanner-android/blob/e2af105de79a66cb0217e5f069c68ce20fcd3b78/ibeaconscanner/src/main/java/mobi/inthepocket/android/beacons/ibeaconscanner/utils/ConversionUtils.java#L52
 * Converts a [UUID] to a byte[]. This is used to create a [android.bluetooth.le.ScanFilter].
 * From http://stackoverflow.com/questions/29664316/bluetooth-le-scan-filter-not-working.
 *
 * @param uuid UUID to convert to a byte[]
 * @return byte[]
 */
fun UuidToByteArray(uuid: String): ByteArray {
    val hex: String = uuid.replace("-", "")
    val length = hex.length
    val result = ByteArray(length / 2)
    var i = 0
    while (i < length) {
        result[i / 2] = ((Character.digit(hex[i], 16) shl 4) + Character.digit(
            hex[i + 1], 16
        )).toByte()
        i += 2
    }
    return result
}

/**
 * Convert major or minor to hex byte[]. This is used to create a [android.bluetooth.le.ScanFilter].
 *
 * @param value major or minor to convert to byte[]
 * @return byte[]
 */
fun integerToByteArray(value: Int): ByteArray {
    val result = ByteArray(2)
    result[0] = (value / 256).toByte()
    result[1] = (value % 256).toByte()
    return result
}

/**
 * Convert major and minor byte array to integer.
 *
 * @param byteArray that contains major and minor byte
 * @return integer value for major and minor
 */
fun byteArrayToInteger(byteArray: ByteArray): Int {
    return (byteArray[0] and -1) * 0x100 + (byteArray[1] and -1)
}


//    deviceName = max 14byte
//     Manufacture Data (Nordic Semiconductor ASA):
//     59-00- manufactureId(2)= Nordic Semiconductor
//     02- TYPEID(1) : beacon = 2
//     15- DATA LENGTH(1)=21bytes
//     1f-4a-e6-a0- UUID Header
//     00-37-27-58-90- Latitude
//     01-27-12-97-42- Longtitude
//     00-01- spare
//     00-00- major(2)
//     00-01- minor(2)
//     c3     txPower(1)


//     Manufacture Data (Nordic Semiconductor ASA): 59-00-02-15-1f-4a-e6-a0-00-37-27-58-90-01-27-12-97-42-00-01-00-00-00-01-c3

//     Manufacture Data (Nordic Semiconductor ASA):
//     59-00-02-15-
//     1f-4a-e6-a0-00-37-27-61-14-01-27-12-97-40-00-01- UUID
//     00-00-00-02- major minor
//     c3