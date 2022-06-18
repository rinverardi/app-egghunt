package app.egghunt.lib

import android.content.Intent
import app.egghunt.device.DeviceService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * A background service for push messaging.
 *
 * _Key Responsibilities_
 *
 * * Fire a broadcasts after a push message is received.
 * * Keep messaging registration tokens updated.
 *
 * _Broadcasts_
 *
 * * Actions.NEW_MESSAGE -- when a new push message is received
 */

class MessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val intent = Intent().apply {
            action = Actions.NEW_MESSAGE

            putExtra(Extras.BODY, message.notification!!.body)
            putExtra(Extras.TITLE, message.notification!!.title)
        }

        sendBroadcast(intent)
    }

    override fun onNewToken(token: String) {
        val intent = Intent(applicationContext, DeviceService::class.java).apply {
            action = Actions.NEW_TOKEN

            putExtra(Extras.TOKEN, token)
        }

        startService(intent)
    }
}