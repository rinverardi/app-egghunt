package app.egghunt.lib

import android.content.Intent
import com.google.android.gms.maps.model.LatLng

object Extras {
    const val BODY = "body"
    const val COMPETITION_DESCRIPTION = "competitionDescription"
    const val COMPETITION_TAG = "competitionTag"
    const val EGG_DESCRIPTION = "eggDescription"
    const val EGG_TAG = "eggTag"
    const val HUNTER_DESCRIPTION = "hunterDescription"
    const val HUNTER_TAG = "hunterTag"
    const val POSITION_LATITUDE = "positionLatitude"
    const val POSITION_LONGITUDE = "positionLongitude"
    const val RANKS = "ranks"
    const val TITLE = "title"
    const val TOKEN = "token"

    fun getPosition(intent: Intent): LatLng? {
        val positionLatitude = getPositionLatitude(intent)
        val positionLongitude = getPositionLongitude(intent)

        return if (positionLatitude == null || positionLongitude == null) {
            null
        } else {
            LatLng(positionLatitude, positionLongitude)
        }
    }

    fun getPositionLatitude(intent: Intent?): Double? =
        intent?.getDoubleExtra(POSITION_LATITUDE, Double.NaN).takeUnless {
            it?.isNaN() == true
        }

    fun getPositionLongitude(intent: Intent?): Double? =
        intent?.getDoubleExtra(POSITION_LONGITUDE, Double.NaN).takeUnless {
            it?.isNaN() == true
        }

    fun setPosition(intent: Intent, position: LatLng?) {
        intent.putExtra(POSITION_LATITUDE, position?.latitude)
        intent.putExtra(POSITION_LONGITUDE, position?.longitude)
    }
}