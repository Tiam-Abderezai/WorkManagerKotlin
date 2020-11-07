package com.example.bluetoothkotlin

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ENABLE_BT: Int = 1
    private val REQUEST_CODE_DISCOVERABLE_BT: Int = 2

    //Bluetooth Adapter
    lateinit var bAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init Bluetooth Adapter
        bAdapter = BluetoothAdapter.getDefaultAdapter()
        // Check if bluetooth is on/off
        if (bAdapter == null) {
            bluetoothStatusTv.text = "Bluetooth is not available"
        } else {
            bluetoothStatusTv.text = "Bluetooth is available"
        }
        // Set image according to bluetooth status(on/off)
        if (bAdapter.isEnabled) {
            // Bluetooth is On
            bluetoothIv.setImageResource(R.drawable.ic_bluetooth_on)
        } else {
            // Bluetooth is Off
            bluetoothIv.setImageResource(R.drawable.ic_bluetooth_off)
        }
        // Turn On Bluetooth
        turnOnBtn.setOnClickListener {
            if (bAdapter.isEnabled) {
                // Already Enabled
                Toast.makeText(this, "Already On", Toast.LENGTH_LONG).show()
            } else {
                // Turn On Bluetooth
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)
            }
        }
        // Turn Off Bluetooth
        turnOffBtn.setOnClickListener {
            turnOffBtn.setOnClickListener {
                if (!bAdapter.isEnabled) {
                    // Already Enabled
                    Toast.makeText(this, "Already Off", Toast.LENGTH_LONG).show()
                } else {
                    // Turn Off Bluetooth
                    bAdapter.disable()
                    bluetoothIv.setImageResource(R.drawable.ic_bluetooth_off)
                    Toast.makeText(this, "Bluetooth turned Off", Toast.LENGTH_LONG).show()
                }
            }
        }
        // Make Bluetooth Discoverable
        discoverableBtn.setOnClickListener {
            if (!bAdapter.isDiscovering) {
                Toast.makeText(this, "Making your Device Discoverable", Toast.LENGTH_LONG).show()
                val intent = Intent(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BT)
            }
        }
// Get list of Paired Devices
        pairedBtn.setOnClickListener{
            if (bAdapter.isEnabled){
                pairedTv.text = "Paired Devices"
                val devices = bAdapter.bondedDevices
                for (device in devices){
                    val deviceName = device.name
                    val deviceAddress = device
                    pairedTv.append("\nDevice: $deviceName, $device")
                }
            }
            else {
                Toast.makeText(this, "Making your Device Discoverable", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

            REQUEST_CODE_ENABLE_BT ->
                if (resultCode == Activity.RESULT_OK) {
                    bluetoothIv.setImageResource(R.drawable.ic_bluetooth_on)
                    Toast.makeText(this, "Bluetooth is On", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(this, "Couldn't turn Bluetooth On...", Toast.LENGTH_LONG)
                        .show()
                }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

}