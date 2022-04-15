package app.egghunt.egg

import app.egghunt.lib.Keys
import com.google.firebase.database.DatabaseReference

object EggManager {
    fun find(
        competition: DatabaseReference,
        eggDescription: String,
        eggTag: String,
        hunterDescription: String,
        hunterTag: String
    ) {
        val time = System.currentTimeMillis()

        competition.child(Keys.EGG).child(eggTag).updateChildren(
            mapOf(
                Keys.DESCRIPTION to eggDescription,
                Keys.HUNTER_DESCRIPTION to hunterDescription,
                Keys.HUNTER_TAG to hunterTag,
                Keys.TIME_FOUND to time
            )
        )
    }

    fun hide(
        competition: DatabaseReference,
        eggDescription: String,
        eggTag: String
    ) {
        val time = System.currentTimeMillis()

        competition.child(Keys.EGG).child(eggTag).updateChildren(
            mapOf(
                Keys.DESCRIPTION to eggDescription,
                Keys.TIME_HIDDEN to time
            )
        )
    }

    fun position(
        competition: DatabaseReference,
        eggTag: String,
        positionLatitude: Double?,
        positionLongitude: Double?
    ) {
        competition.child(Keys.EGG).child(eggTag).updateChildren(
            mapOf(
                Keys.POSITION_LATITUDE to positionLatitude,
                Keys.POSITION_LONGITUDE to positionLongitude,
            )
        )
    }
}