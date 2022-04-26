package app.egghunt.hunter

import app.egghunt.lib.Keys
import com.google.firebase.database.DatabaseReference

object HunterRepo {
    fun insert(competition: DatabaseReference, hunterDescription: String, hunterTag: String) {
        competition.child(Keys.HUNTER).child(hunterTag).setValue(hunterDescription)
    }

    fun list(
        competition: DatabaseReference,
        onFailure: () -> Unit,
        onSuccess: (List<Hunter>) -> Unit
    ) {
        competition.child(Keys.HUNTER).orderByValue().get()
            .addOnFailureListener {
                onFailure()
            }
            .addOnSuccessListener { hunters ->
                onSuccess(hunters.children.map { hunter ->
                    Hunter(hunter.value!!.toString(), hunter.key!!)
                })
            }
    }
}