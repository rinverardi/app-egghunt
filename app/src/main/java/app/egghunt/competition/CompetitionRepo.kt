package app.egghunt.competition

import app.egghunt.lib.Keys
import app.egghunt.lib.RemoteData
import com.google.firebase.database.DatabaseReference

object CompetitionRepo {
    fun sync(
        competitionDescription: String,
        competitionTag: String,
    ): DatabaseReference {
        val database = RemoteData.instance

        return database.getReference("${Keys.COMPETITION}/$competitionTag").apply {
            child(Keys.DESCRIPTION).setValue(competitionDescription)
            keepSynced(true)
        }
    }
}
