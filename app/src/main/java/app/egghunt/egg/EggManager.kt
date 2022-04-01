package app.egghunt.egg

import app.egghunt.lib.Code
import com.google.firebase.database.DatabaseReference

object EggManager {
    fun find(
        competition: DatabaseReference,
        code: Code,
        hunterDescription: String,
        hunterTag: String
    ) {
        val egg = competition.child("egg").child(code.et!!)

        egg.child("description").setValue(code.ed)
        egg.child("hunterDescription").setValue(hunterDescription)
        egg.child("hunterTag").setValue(hunterTag)
        egg.child("timeFound").setValue(System.currentTimeMillis())
    }

    fun hide(competition: DatabaseReference, code: Code) {
        val egg = competition.child("egg").child(code.et!!)

        egg.child("description").setValue(code.ed)
        egg.child("timeHidden").setValue(System.currentTimeMillis())
    }
}