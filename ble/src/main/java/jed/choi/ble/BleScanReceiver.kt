package jed.choi.ble

import android.Manifest
import android.bluetooth.le.BluetoothLeScanner
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import no.nordicsemi.android.support.v18.scanner.ScanResult


class BleScanReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.i(TAG, "onReceive: $intent")
        // if action is same as [ACTION_FOUND_DEVICE]

        if (intent.hasExtra(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT)) {
            val results = intent.getParcelableArrayListExtra<ScanResult>(BluetoothLeScanner.EXTRA_LIST_SCAN_RESULT)
            if (results != null) {
                for (result in results) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    Log.e(TAG, "onScanResult : " + result.device.name.toString() + " " + result.device.address)
                    Toast.makeText(context, "${result.device.name} ${result.device.address}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        const val TAG = "BleScanReceiver"
    }
}