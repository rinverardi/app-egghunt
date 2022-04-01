package app.egghunt.lib

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object LocalData {
    fun clear(context: Context) {
        open(context).edit {
            clear()
        }
    }

    fun loadCurrentHunter(context: Context): List<String>? {
        val sharedPrefs = open(context)

        return when {
            sharedPrefs.contains(Keys.HUNTER_TAG) -> listOf(
                sharedPrefs.getString(Keys.COMPETITION_DESCRIPTION, "")!!,
                sharedPrefs.getString(Keys.COMPETITION_TAG, "")!!,
                sharedPrefs.getString(Keys.HUNTER_DESCRIPTION, "")!!,
                sharedPrefs.getString(Keys.HUNTER_TAG, "")!!
            )
            else -> null
        }
    }

    fun loadCurrentOrganizer(context: Context): List<String>? {
        val sharedPrefs = open(context)

        return when {
            sharedPrefs.contains(Keys.HUNTER_TAG) -> null
            sharedPrefs.contains(Keys.COMPETITION_TAG) -> listOf(
                sharedPrefs.getString(Keys.COMPETITION_DESCRIPTION, "")!!,
                sharedPrefs.getString(Keys.COMPETITION_TAG, "")!!
            )
            else -> null
        }
    }

    private fun open(context: Context): SharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun saveCurrentHunter(
        context: Context,
        competitionDescription: String,
        competitionTag: String,
        hunterDescription: String,
        hunterTag: String
    ) {
        open(context).edit {
            putString(Keys.COMPETITION_DESCRIPTION, competitionDescription)
            putString(Keys.COMPETITION_TAG, competitionTag)
            putString(Keys.HUNTER_DESCRIPTION, hunterDescription)
            putString(Keys.HUNTER_TAG, hunterTag)
        }
    }

    fun saveCurrentOrganizer(
        context: Context,
        competitionDescription: String,
        competitionTag: String
    ) {
        open(context).edit {
            putString(Keys.COMPETITION_DESCRIPTION, competitionDescription)
            putString(Keys.COMPETITION_TAG, competitionTag)
            remove(Keys.HUNTER_DESCRIPTION)
            remove(Keys.HUNTER_TAG)
        }
    }
}