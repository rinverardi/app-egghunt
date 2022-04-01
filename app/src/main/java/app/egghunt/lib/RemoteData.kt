package app.egghunt.lib

import com.google.firebase.database.FirebaseDatabase

object RemoteData {
    fun open(): FirebaseDatabase {
        val url = "https://egg-hunt-344616-default-rtdb.europe-west1.firebasedatabase.app"

        return FirebaseDatabase.getInstance(url)
    }
}
