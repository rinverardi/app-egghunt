package app.egghunt.hint

import com.google.firebase.database.DatabaseReference
import java.util.*

object HintManager {
    fun post(competition: DatabaseReference, text: String) {
        val hint = competition.child("hint").child(tag())

        val postedAt = System.currentTimeMillis()

        hint.child("order").setValue(-postedAt)
        hint.child("postedAt").setValue(postedAt)
        hint.child("text").setValue(text)
    }

    private fun tag(): String {
        return UUID.randomUUID().toString()
    }
}