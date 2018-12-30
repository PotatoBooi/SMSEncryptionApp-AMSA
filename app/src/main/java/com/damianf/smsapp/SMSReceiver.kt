package com.damianf.smsapp

import android.content.*
import android.widget.Toast
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log


class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsSender = ""
            var smsBody = ""
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsBody += smsMessage.messageBody
            }

            if (smsBody.startsWith(SmsHelper.SMS_CONDITION)) {
                Log.d(TAG, "Sms with condition detected")
                Toast.makeText(context, "BroadcastReceiver caught conditional SMS: ${smsBody.removePrefix(SmsHelper.SMS_CONDITION)}", Toast.LENGTH_LONG).show()
                val encryptor = MessageEncryptor()
                val decrypted = encryptor.decrypt(smsBody.removePrefix(SmsHelper.SMS_CONDITION))

                val intent = Intent("SmsMessage.intent.MAIN")
                intent.putExtra("SMS_ENC",smsBody)
                intent.putExtra("SMS",decrypted)

                context.sendBroadcast(intent)
            }
            Log.d(TAG, "SMS detected: From $smsSender With text $smsBody")
        }
    }

    companion object {

        private val TAG = "SmsBroadcastReceiver"
    }


    object SmsHelper {

        val SMS_CONDITION = "SECRETMSG"

        fun isValidPhoneNumber(phoneNumber: String): Boolean {
            return android.util.Patterns.PHONE.matcher(phoneNumber).matches()
        }

        fun sendDebugSms(number: String, smsBody: String) {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number, null, smsBody, null, null)
        }
    }
}