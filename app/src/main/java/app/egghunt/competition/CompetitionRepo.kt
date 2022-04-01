package app.egghunt.competition

import app.egghunt.lib.Keys
import app.egghunt.lib.RemoteData
import com.google.firebase.database.DatabaseReference

object CompetitionRepo {
    fun sync(
        competitionDescription: String,
        competitionTag: String,
    ): DatabaseReference {
        val reference = RemoteData.open().getReference("${Keys.COMPETITION}/$competitionTag")

        reference.child(Keys.DESCRIPTION).setValue(competitionDescription)
        reference.keepSynced(true)

        return reference
    }
}
