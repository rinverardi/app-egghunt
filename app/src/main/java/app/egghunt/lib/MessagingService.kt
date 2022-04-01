package app.egghunt.lib

import android.content.Intent
import app.egghunt.device.DeviceService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val intent = Intent()

        intent.action = Actions.NEW_MESSAGE

        intent.putExtra(Extras.BODY, message.notification!!.body)
        intent.putExtra(Extras.TITLE, message.notification!!.title)

        sendBroadcast(intent)
    }

    override fun onNewToken(token: String) {
        val intent = Intent(applicationContext, DeviceService::class.java)

        intent.action = Actions.NEW_TOKEN
        intent.putExtra(Extras.TOKEN, token)

        startService(intent)
    }
}