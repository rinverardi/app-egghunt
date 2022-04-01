package app.egghunt.hint

import app.egghunt.lib.Keys
import com.google.firebase.database.DatabaseReference
import java.util.UUID

object HintManager {
    fun post(competition: DatabaseReference, text: String) {
        val hint = competition.child(Keys.HINT).child(tag())

        val time = System.currentTimeMillis()

        hint.child(Keys.ORDER).setValue(-time)
        hint.child(Keys.TEXT).setValue(text)
        hint.child(Keys.TIME_POSTED).setValue(time)
    }

    private fun tag(): String = UUID.randomUUID().toString()
}