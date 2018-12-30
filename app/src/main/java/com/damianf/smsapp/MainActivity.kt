package com.damianf.smsapp

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.Manifest.permission
import android.Manifest.permission.READ_SMS
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.provider.Telephony
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var smsReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isSmsPermissionGranted())
            requestReadAndSendSmsPermission()

        button_send.setOnClickListener {
            sendSMS(editText_number.text.toString(), editText_message.text.toString())
        }

    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("SmsMessage.intent.MAIN")
        smsReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                val message = intent?.getStringExtra("SMS")
                val enc = intent?.getStringExtra("SMS_ENC")
                textView_enc.text = "${textView_enc.text}\n$enc\n$message"

            }

        }
        registerReceiver(smsReceiver,intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(smsReceiver)
    }

    private fun sendSMS(number: String, message: String) {
        val prefix = "SECRETMSG"
        val smsManager = SmsManager.getDefault()
        val encryptor = MessageEncryptor()

        val toSend = "$prefix${encryptor.encrypt(message)}"
        Toast.makeText(this,"$prefix${encryptor.encrypt(message)}",Toast.LENGTH_LONG)
            .show()
        smsManager.sendTextMessage(
            number,
            null,
            toSend,
            null,
            null
        )

    }


    private fun isSmsPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BROADCAST_SMS
        ) == PackageManager.PERMISSION_GRANTED)
    }

    /**
     * Request runtime SMS permission
     */
    private fun requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html
        }
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.BROADCAST_SMS
            ), SMS_PERMISSION_CODE
        )
    }

    private val SMS_PERMISSION_CODE = 0
}
