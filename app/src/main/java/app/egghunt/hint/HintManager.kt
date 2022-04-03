package app.egghunt.hint

import app.egghunt.lib.Keys
import com.google.firebase.database.DatabaseReference
import java.util.UUID

object HintManager {
    fun post(competition: DatabaseReference, text: String) {
        val time = System.currentTimeMillis()

        competition.child(Keys.HINT).child(tag()).setValue(
            mapOf(
                Keys.ORDER to -time,
                Keys.TEXT to text,
                Keys.TIME_POSTED to time
            )
        )
    }

    private fun tag(): String = UUID.randomUUID().toString()
}