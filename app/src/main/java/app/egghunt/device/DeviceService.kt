package app.egghunt.device

import android.app.Service
import android.content.Intent
import android.os.IBinder
import app.egghunt.competition.CompetitionRepo
import app.egghunt.lib.Actions
import app.egghunt.lib.Extras
import com.google.firebase.database.DatabaseReference

/**
 * A background service for device management.
 *
 * _Key Responsibilities_
 *
 * * Automatically add devices to competitions.
 * * Automatically remove devices from competitions.
 * * Keep messaging registration tokens updated.
 *
 * _Actions_
 *
 * * Action.ENTER_COMPETITION -- when entering a competition
 * * Actions.LEAVE_COMPETITION -- when leaving a competition
 * * Actions.NEW_TOKEN -- when a messaging registration token changes
 */

class DeviceService : Service() {
    private var competition: DatabaseReference? = null
    private var token: String? = null

    private fun enterCompetition(intent: Intent) {
        competition = CompetitionRepo.sync(
            intent.getStringExtra(Extras.COMPETITION_DESCRIPTION)!!,
            intent.getStringExtra(Extras.COMPETITION_TAG)!!
        )

        token?.let { token ->
            DeviceRepo.insert(competition!!, token)
        }
    }

    private fun leaveCompetition(intent: Intent) {
        competition = CompetitionRepo.sync(
            intent.getStringExtra(Extras.COMPETITION_DESCRIPTION)!!,
            intent.getStringExtra(Extras.COMPETITION_TAG)!!
        )

        token?.let { token ->
            DeviceRepo.remove(competition!!, token)
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