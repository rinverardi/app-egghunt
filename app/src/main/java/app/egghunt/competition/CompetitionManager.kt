package app.egghunt.competition

import android.content.Context
import android.content.Intent
import app.egghunt.device.DeviceService
import app.egghunt.hunter.HunterService
import app.egghunt.lib.Actions
import app.egghunt.lib.Extras
import app.egghunt.lib.LocalData
import app.egghunt.score.ScoreService

object CompetitionManager {
    private val SERVICES = listOf(
        DeviceService::class.java,
        HunterService::class.java,
        ScoreService::class.java
    )

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

        SERVICES.forEach { service ->
            val intent = Intent(context, service).apply {
                action = Actions.ENTER_COMPETITION

                putExtra(Extras.COMPETITION_DESCRIPTION, competitionDescription)
                putExtra(Extras.COMPETITION_TAG, competitionTag)
                putExtra(Extras.HUNTER_DESCRIPTION, hunterDescription)
                putExtra(Extras.HUNTER_TAG, hunterTag)
            }

            context.startService(intent)
        }
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

        SERVICES.forEach { service ->
            val intent = Intent(context, service).apply {
                action = Actions.ENTER_COMPETITION

                putExtra(Extras.COMPETITION_DESCRIPTION, competitionDescription)
                putExtra(Extras.COMPETITION_TAG, competitionTag)
            }

            context.startService(intent)
        }
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