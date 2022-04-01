package app.egghunt.competition

import android.content.Context
import android.content.Intent
import app.egghunt.device.DeviceService
import app.egghunt.lib.Actions
import app.egghunt.lib.Extras
import app.egghunt.lib.LocalData

object CompetitionManager {
    private fun enter(
        context: Context,
        competitionDescription: String,
        competitionTag: String
    ) {
        val intent = Intent(context, DeviceService::class.java)

        intent.action = Actions.ENTER_COMPETITION

        intent.putExtra(Extras.COMPETITION_DESCRIPTION, competitionDescription)
        intent.putExtra(Extras.COMPETITION_TAG, competitionTag)

        context.startService(intent)
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

        val intent = Intent(context, DeviceService::class.java)

        intent.action = Actions.LEAVE_COMPETITION

        intent.putExtra(Extras.COMPETITION_DESCRIPTION, competitionDescription)
        intent.putExtra(Extras.COMPETITION_TAG, competitionTag)

        context.startService(intent)
    }
}