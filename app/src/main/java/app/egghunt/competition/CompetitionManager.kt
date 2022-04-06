package app.egghunt.competition

import android.content.Context
import android.content.Intent
import app.egghunt.device.DeviceService
import app.egghunt.lib.Actions
import app.egghunt.lib.Extras
import app.egghunt.lib.LocalData
import app.egghunt.score.ScoreService

object CompetitionManager {
    private val SERVICES = listOf(
        DeviceService::class.java,
        ScoreService::class.java
    )

    private fun enter(
        context: Context,
        competitionDescription: String,
        competitionTag: String
    ) {
        SERVICES.forEach { service ->
            val intent = Intent(context, service).apply {
                action = Actions.ENTER_COMPETITION

                putExtra(Extras.COMPETITION_DESCRIPTION, competitionDescription)
                putExtra(Extras.COMPETITION_TAG, competitionTag)
            }

            context.startService(intent)
        }
    }

    fun enterAsHunter(
        context: Context,
        competitionDescription: String,
        competitionTag: String,
        hunterDescription: String,
        hunterTag: String
    ) {
        LocalData.saveCurrentHunter(
            context,
            competitionDescription,
            competitionTag,
            hunterDescription,
            hunterTag
        )

        enter(context, competitionDescription, competitionTag)
    }

    fun enterAsOrganizer(
        context: Context,
        competitionDescription: String,
        competitionTag: String
    ) {
        LocalData.saveCurrentOrganizer(
            context,
            competitionDescription,
            competitionTag
        )

        enter(context, competitionDescription, competitionTag)
    }

    fun leave(context: Context, competitionDescription: String, competitionTag: String) {
        LocalData.clear(context)

        SERVICES.forEach { service ->
            val intent = Intent(context, service).apply {
                action = Actions.LEAVE_COMPETITION

                putExtra(Extras.COMPETITION_DESCRIPTION, competitionDescription)
                putExtra(Extras.COMPETITION_TAG, competitionTag)
            }

            context.startService(intent)
        }
    }
}