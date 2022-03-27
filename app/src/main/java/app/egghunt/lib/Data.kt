package app.egghunt.lib

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

object Data {
    private fun open(): FirebaseDatabase {
        val url = "https://egg-hunt-344616-default-rtdb.europe-west1.firebasedatabase.app"

        return FirebaseDatabase.getInstance(url)
    }

    fun syncCompetition(
        competitionDescription: String,
        competitionTag: String,
    ): DatabaseReference {
        val reference = open().getReference("competition/$competitionTag")

        reference.child("description").setValue(competitionDescription)
        reference.keepSynced(true)

        return reference
    }

    fun tag(): String {
        return UUID.randomUUID().toString()
    }
}
