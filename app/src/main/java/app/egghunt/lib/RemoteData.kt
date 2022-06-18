package app.egghunt.lib

import com.google.firebase.database.FirebaseDatabase

/**
 * Remote data is stored in the cloud and cached on the device.
 *
 * Uses the Firebase realtime database as the persistence mechanism.
 */

object RemoteData {
    val instance: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance(URL).apply {
            setPersistenceEnabled(true)
        }
    }

    private const val URL = "https://egg-hunt-344616-default-rtdb.europe-west1.firebasedatabase.app"
}
