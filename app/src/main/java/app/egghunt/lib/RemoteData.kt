package app.egghunt.lib

import com.google.firebase.database.FirebaseDatabase

object RemoteData {
    val instance: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance(URL).apply {
            setPersistenceEnabled(true)
        }
    }

    private const val URL = "https://egg-hunt-344616-default-rtdb.europe-west1.firebasedatabase.app"
}
