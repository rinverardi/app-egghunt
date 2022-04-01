package app.egghunt.lib

import android.content.Context
import android.content.Intent
import android.util.Log
import app.egghunt.device.DeviceService
import com.google.firebase.messaging.FirebaseMessaging

object Messaging {
    fun register(context: Context) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i(javaClass.simpleName, "Registration succeeded: " + task.result)

                val intent = Intent(context, DeviceService::class.java)

                intent.action = Actions.NEW_TOKEN
                intent.putExtra(Extras.TOKEN, task.result)

                context.startService(intent)
            } else {
                Log.w(javaClass.simpleName, "Registration failed", task.exception)
            }
        }
    }
}