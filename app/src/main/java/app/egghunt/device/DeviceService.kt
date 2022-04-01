package app.egghunt.device

import android.app.Service
import android.content.Intent
import android.os.IBinder
import app.egghunt.competition.CompetitionRepo
import app.egghunt.lib.Actions
import app.egghunt.lib.Extras
import com.google.firebase.database.DatabaseReference

class DeviceService : Service() {
    private var competition: DatabaseReference? = null
    private var token: String? = null

    private fun enterCompetition(intent: Intent) {
        competition = CompetitionRepo.sync(
            intent.getStringExtra(Extras.COMPETITION_DESCRIPTION)!!,
            intent.getStringExtra(Extras.COMPETITION_TAG)!!
        )

        if (token != null) {
            DeviceRepo.insert(competition!!, token!!)
        }
    }

    private fun leaveCompetition(intent: Intent) {
        competition = CompetitionRepo.sync(
            intent.getStringExtra(Extras.COMPETITION_DESCRIPTION)!!,
            intent.getStringExtra(Extras.COMPETITION_TAG)!!
        )

        if (token != null) {
            DeviceRepo.remove(competition!!, token!!)
        }

        competition = null
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, id: Int): Int {
        when (intent?.action) {
            Actions.ENTER_COMPETITION -> enterCompetition(intent)
            Actions.LEAVE_COMPETITION -> leaveCompetition(intent)
            Actions.NEW_TOKEN -> updateToken(intent)
        }

        return START_REDELIVER_INTENT
    }

    private fun updateToken(intent: Intent) {
        val oldToken = token
        val newToken = intent.getStringExtra(Extras.TOKEN)!!

        if (competition != null) {
            if (oldToken != null && oldToken != newToken) {
                DeviceRepo.remove(competition!!, oldToken)
            }

            DeviceRepo.insert(competition!!, newToken)
        }

        token = newToken
    }
}