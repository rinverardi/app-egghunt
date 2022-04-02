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

        with(competition.child(Keys.EGG).child(code.et!!)) {
            child(Keys.DESCRIPTION).setValue(code.ed)
            child(Keys.HUNTER_DESCRIPTION).setValue(hunterDescription)
            child(Keys.HUNTER_TAG).setValue(hunterTag)
            child(Keys.TIME_FOUND).setValue(time)
        }
    }

    fun hide(competition: DatabaseReference, code: Code) {
        val time = System.currentTimeMillis()

        with(competition.child(Keys.EGG).child(code.et!!)) {
            child(Keys.DESCRIPTION).setValue(code.ed)
            child(Keys.TIME_HIDDEN).setValue(time)
        }
    }
}