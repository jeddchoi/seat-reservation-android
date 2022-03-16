package jed.choi.ble

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.ParcelUuid
import androidx.core.os.bundleOf
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
    private val scanner: BluetoothLeScannerCompat
) {
    private fun getScanSettings(callbackType: Int): ScanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
        .setNumOfMatches(1)
        .setCallbackType(callbackType)
        .setReportDelay(10000)
        .build()

    private fun getPendingIntent(
        requestCode: Int,
        extra: Bundle? = null
    ): PendingIntent {
        // explicit intent
        val intent = Intent(context, BleScanReceiver::class.java).apply {
            action = ACTION_FOUND_DEVICE
            extra?.let { putExtras(it) }
        }

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getScanFilter(
        uuid: String,
        macAddress: String,
        seatName: String,
        sectionNumber: Int,
        seatNumber: Int
    ): ArrayList<ScanFilter> {
        val manufacturerData = ByteArray(23)
        val major = integerToByteArray(sectionNumber)
        manufacturerData[18] = major[0]
        manufacturerData[19] = major[1]
        val minor = integerToByteArray(seatNumber)
        manufacturerData[20] = minor[0]
        manufacturerData[21] = minor[1]

        return arrayListOf(
            ScanFilter.Builder().setServiceUuid(ParcelUuid(UUID.fromString(uuid))).build(),
            ScanFilter.Builder().setDeviceAddress(macAddress).build(),
            ScanFilter.Builder().setDeviceName(seatName).build(),
            ScanFilter.Builder()
                .setManufacturerData(MANUFACTURER_ID, manufacturerData, MAJOR_BYTEARRAY_MASK)
                .build(),
            ScanFilter.Builder()
                .setManufacturerData(MANUFACTURER_ID, manufacturerData, MINOR_BYTEARRAY_MASK)
                .build()
        )
    }


    // when reservation or away
    fun startScanMatchFirst(
        uuid: String,
        macAddress: String,
        seatName: String,
        sectionNumber: Int,
        seatNumber: Int
    ) = scanner.startScan(
        getScanFilter(uuid, macAddress, seatName, sectionNumber, seatNumber),
        getScanSettings(ScanSettings.CALLBACK_TYPE_FIRST_MATCH),
        context,
        getPendingIntent(REQUEST_CODE_MATCH_FIRST, bundleOf()),
        REQUEST_CODE_MATCH_FIRST
    )


    //  when using seat
    fun startScanMatchLost(
        uuid: String,
        macAddress: String,
        seatName: String,
        sectionNumber: Int,
        seatNumber: Int
    ) = scanner.startScan(
        getScanFilter(uuid, macAddress, seatName, sectionNumber, seatNumber),
        getScanSettings(ScanSettings.CALLBACK_TYPE_MATCH_LOST),
        context,
        getPendingIntent(REQUEST_CODE_MATCH_LOST, bundleOf()),
        REQUEST_CODE_MATCH_LOST
    )


    fun stopScan(requestCode: Int) {
        // To stop scanning use the same PendingIntent and request code as one used to start scanning.
        val pendingIntent = getPendingIntent(requestCode)
        scanner.stopScan(context, pendingIntent, requestCode)
    }




    companion object {
        const val ACTION_FOUND_DEVICE = "jed.choi.seatreservation.ACTION_FOUND_DEVICE"
        const val REQUEST_CODE_MATCH_FIRST = 1001
        const val REQUEST_CODE_MATCH_LOST = 1002
        const val MANUFACTURER_ID = 0x59 // Nordic Semiconductor
        val MAJOR_BYTEARRAY_MASK = byteArrayOf(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            // major
            1, 1,
            // minor
            0, 0,
            0
        )
        val MINOR_BYTEARRAY_MASK = byteArrayOf(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            // major
            0, 0,
            // minor
            1, 1,
            0
        )
    }
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