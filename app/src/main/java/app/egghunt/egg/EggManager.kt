package app.egghunt.egg

import app.egghunt.lib.Code
import app.egghunt.lib.Keys
import com.google.firebase.database.DatabaseReference

object EggManager {
    fun find(
        competition: DatabaseReference,
        code: Code,
        hunterDescription: String,
        hunterTag: String
    ) {
        val time = System.currentTimeMillis()

        competition.child(Keys.EGG).child(code.et!!).updateChildren(
            mapOf(
                Keys.DESCRIPTION to code.ed,
                Keys.HUNTER_DESCRIPTION to hunterDescription,
                Keys.HUNTER_TAG to hunterTag,
                Keys.TIME_FOUND to time
            )
        )
    }

    fun hide(competition: DatabaseReference, code: Code) {
        val time = System.currentTimeMillis()

        competition.child(Keys.EGG).child(code.et!!).updateChildren(
            mapOf(
                Keys.DESCRIPTION to code.ed,
                Keys.TIME_HIDDEN to time
            )
        )
    }
}