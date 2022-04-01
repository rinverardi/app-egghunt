package app.egghunt.hint

import com.google.firebase.database.DatabaseReference
import java.util.*

object HintManager {
    fun post(competition: DatabaseReference, text: String) {
        val hint = competition.child("hint").child(tag())

        val time = System.currentTimeMillis()

        hint.child("order").setValue(-time)
        hint.child("text").setValue(text)
        hint.child("timePosted").setValue(time)
    }

    private fun tag(): String {
        return UUID.randomUUID().toString()
    }
}