package app.egghunt.hunter

import android.app.Service
import android.content.Intent
import android.os.IBinder
import app.egghunt.competition.CompetitionRepo
import app.egghunt.lib.Actions
import app.egghunt.lib.Extras
import com.google.firebase.database.DatabaseReference

class HunterService : Service() {
    private var competition: DatabaseReference? = null

    private fun enterCompetition(intent: Intent) {
        competition = CompetitionRepo.sync(
            intent.getStringExtra(Extras.COMPETITION_DESCRIPTION)!!,
            intent.getStringExtra(Extras.COMPETITION_TAG)!!
        )

        val hunterDescription = intent.getStringExtra(Extras.HUNTER_DESCRIPTION)
        val hunterTag = intent.getStringExtra(Extras.HUNTER_TAG)

        if (hunterDescription != null && hunterTag != null) {
            HunterRepo.insert(competition!!, hunterDescription, hunterTag)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, id: Int): Int {
        if (intent?.action == Actions.ENTER_COMPETITION) {
            enterCompetition(intent)
        }

        return START_REDELIVER_INTENT
    }
}