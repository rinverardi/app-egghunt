package app.egghunt.competition

import app.egghunt.lib.RemoteData
import com.google.firebase.database.DatabaseReference

object CompetitionRepo {
    fun sync(
        competitionDescription: String,
        competitionTag: String,
    ): DatabaseReference {
        val reference = RemoteData.open().getReference("competition/$competitionTag")

        reference.child("description").setValue(competitionDescription)
        reference.keepSynced(true)

        return reference
    }
}
