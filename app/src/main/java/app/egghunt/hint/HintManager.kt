package app.egghunt.hint

import app.egghunt.lib.Keys
import com.google.firebase.database.DatabaseReference
import java.util.UUID

object HintManager {
    fun post(competition: DatabaseReference, text: String) {
        val time = System.currentTimeMillis()

        with (competition.child(Keys.HINT).child(tag())) {
            child(Keys.ORDER).setValue(-time)
            child(Keys.TEXT).setValue(text)
            child(Keys.TIME_POSTED).setValue(time)
        }
    }

    private fun tag(): String = UUID.randomUUID().toString()
}