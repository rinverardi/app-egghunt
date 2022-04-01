package app.egghunt.lib

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging

object Messaging {
    fun start(competition: DatabaseReference) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i(javaClass.simpleName, "Registration succeeded: " + task.result)

                competition
                    .child("device")
                    .child(task.result)
                    .setValue(System.currentTimeMillis())
            } else {
                Log.w(javaClass.simpleName, "Registration failed", task.exception)
            }
        }
    }
}