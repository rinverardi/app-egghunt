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
            sharedPrefs.contains(KEY_HUNTER_TAG) -> listOf(
                sharedPrefs.getString(KEY_COMPETITION_DESCRIPTION, "")!!,
                sharedPrefs.getString(KEY_COMPETITION_TAG, "")!!,
                sharedPrefs.getString(KEY_HUNTER_DESCRIPTION, "")!!,
                sharedPrefs.getString(KEY_HUNTER_TAG, "")!!
            )
            else -> null
        }
    }

    fun loadCurrentOrganizer(context: Context): List<String>? {
        val sharedPrefs = open(context)

        return when {
            sharedPrefs.contains(KEY_HUNTER_TAG) -> null
            sharedPrefs.contains(KEY_COMPETITION_TAG) -> listOf(
                sharedPrefs.getString(KEY_COMPETITION_DESCRIPTION, "")!!,
                sharedPrefs.getString(KEY_COMPETITION_TAG, "")!!
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
            putString(KEY_COMPETITION_DESCRIPTION, competitionDescription)
            putString(KEY_COMPETITION_TAG, competitionTag)
            putString(KEY_HUNTER_DESCRIPTION, hunterDescription)
            putString(KEY_HUNTER_TAG, hunterTag)
        }
    }

    fun saveCurrentOrganizer(
        context: Context,
        competitionDescription: String,
        competitionTag: String
    ) {
        open(context).edit {
            putString(KEY_COMPETITION_DESCRIPTION, competitionDescription)
            putString(KEY_COMPETITION_TAG, competitionTag)
            remove(KEY_HUNTER_DESCRIPTION)
            remove(KEY_HUNTER_TAG)
        }
    }

    private const val KEY_COMPETITION_DESCRIPTION = "competition_description"
    private const val KEY_COMPETITION_TAG = "competition_tag"
    private const val KEY_HUNTER_DESCRIPTION = "hunter_description"
    private const val KEY_HUNTER_TAG = "hunter_tag"
}