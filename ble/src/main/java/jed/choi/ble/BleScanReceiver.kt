package jed.choi.ble

import android.bluetooth.le.BluetoothLeScanner
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import jed.choi.domain.entity.SeatState
import jed.choi.domain.usecase.seat.GetUserSeat
import jed.choi.domain.usecase.seat.LeaveAwaySeat
import jed.choi.domain.usecase.seat.ResumeUsingSeat
import jed.choi.domain.usecase.seat.StartUsingSeat
import kotlinx.coroutines.*
import no.nordicsemi.android.support.v18.scanner.ScanResult
import javax.inject.Inject

@DelicateCoroutinesApi
@AndroidEntryPoint
class BleScanReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getUserSeat: GetUserSeat

    @Inject
    lateinit var startUsingSeat: StartUsingSeat

    @Inject
    lateinit var resumeUsingSeat: ResumeUsingSeat

    @Inject
    lateinit var leaveAwaySeat: LeaveAwaySeat


    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.e(
            TAG,
            "onReceive: ${intent.action} ${intent.hasExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT)} ${
                intent.getParcelableArrayListExtra<ScanResult>(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT)?.size
            }"
        )

        if (intent.action == BleSeatManager.ACTION_FOUND_DEVICE) {
            if (intent.hasExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT)) {
                val results =
                    intent.getParcelableArrayListExtra<ScanResult>(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT)
                val isInRange =
                    results?.singleOrNull()?.rssi?.compareTo(MINIMUM_RSSI_IN_RANGE) ?: -100 > 0

                Log.e(TAG, "onReceive: isInRange = $isInRange")
                if (isInRange) {
                    handleIfInRange(results!!.single())
                } else {
                    handleIfNotInRange()
                }
            }
        }
    }

    private fun handleIfInRange(scanResult: ScanResult) {
        goAsync(GlobalScope, Dispatchers.IO) {
            val seat = getUserSeat.invoke()
            when (seat?.state) {
                SeatState.RESERVED -> {
                    startUsingSeat.invoke().collect() {
                        Log.e(TAG, "${seat.state} -> startUsingSeat: $it")
                    }
                }
                SeatState.AWAY -> {
                    resumeUsingSeat.invoke().collect() {
                        Log.e(TAG, "${seat.state} -> resumeUsingSeat: $it")
                    }
                }
                SeatState.IDLE,
                SeatState.ACQUIRED,
                SeatState.NOT_USED,
                null -> {
                    // no - op
                }
            }
        }
    }


    private fun handleIfNotInRange() {
        goAsync(GlobalScope, Dispatchers.IO) {
            val seat = getUserSeat.invoke()
            when (seat?.state) {
                SeatState.ACQUIRED -> {
                    leaveAwaySeat.invoke().collect() {
                        Log.e(TAG, "${seat.state} -> leaveAwaySeat: $it")
                    }
                }
                SeatState.RESERVED,
                SeatState.AWAY,
                SeatState.IDLE,
                SeatState.NOT_USED,
                null -> {
                    // no - op
                }

            }

        }
    }


    fun BroadcastReceiver.goAsync(
        coroutineScope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        block: suspend () -> Unit
    ) {
        val pendingResult = goAsync()
        coroutineScope.launch(dispatcher) {
            block()
            pendingResult.finish()
        }
    }

    companion object {
        const val TAG = "BleScanReceiver"
        const val MINIMUM_RSSI_IN_RANGE = -70
    }
}

//                    ScanResult{
//                    device=EC:60:5E:E5:34:AC,
//                    scanRecord=ScanRecord [
//                      advertiseFlags=6,
//                      serviceUuids=null,
//                      manufacturerSpecificData={89=[2, 21, 31, 74, -26, -96, 0, 55, 39, 88, -112, 1, 39, 18, -105, 66, 0, 1, 0, 0, 0, 1, -61]},
//                      serviceData={0000180a-0000-1000-8000-00805f9b34fb=[1, 3, -20, 96, 94, -27, 52, -84, 11, -42, 24]},
//                      txPowerLevel=-2147483648,
//                      deviceName=Seat_1
//                    ],
//                    rssi=-41,
//                    timestampNanos=3616834213026,
//                    eventType=17,
//                    primaryPhy=1,
//                    secondaryPhy=0,
//                    advertisingSid=255,
//                    txPower=127,
//                    periodicAdvertisingInterval=0}
