package app.egghunt.device

import app.egghunt.lib.Keys
import com.google.firebase.database.DatabaseReference

object DeviceRepo {
    fun insert(competition: DatabaseReference, token: String) {
        val time = System.currentTimeMillis()

        competition.child(Keys.DEVICE).child(token).setValue(time)
    }

    fun remove(competition: DatabaseReference, token: String) {
        competition.child(Keys.DEVICE).child(token).removeValue()
    }
}