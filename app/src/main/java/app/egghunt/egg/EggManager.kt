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
        val egg = competition.child(Keys.EGG).child(code.et!!)

        egg.child(Keys.DESCRIPTION).setValue(code.ed)
        egg.child(Keys.HUNTER_DESCRIPTION).setValue(hunterDescription)
        egg.child(Keys.HUNTER_TAG).setValue(hunterTag)
        egg.child(Keys.TIME_FOUND).setValue(System.currentTimeMillis())
    }

    fun hide(competition: DatabaseReference, code: Code) {
        val egg = competition.child(Keys.EGG).child(code.et!!)

        egg.child(Keys.DESCRIPTION).setValue(code.ed)
        egg.child(Keys.TIME_HIDDEN).setValue(System.currentTimeMillis())
    }
}