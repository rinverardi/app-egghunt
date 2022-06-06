package app.egghunt.score

import android.app.Service
import android.content.Intent
import android.os.IBinder
import app.egghunt.competition.CompetitionRepo
import app.egghunt.egg.Egg
import app.egghunt.lib.Actions
import app.egghunt.lib.Extras
import app.egghunt.lib.Keys
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson

class ScoreService : Service() {
    private var competition: DatabaseReference? = null

    private fun enterCompetition(intent: Intent) {
        competition = CompetitionRepo.sync(
            intent.getStringExtra(Extras.COMPETITION_DESCRIPTION)!!,
            intent.getStringExtra(Extras.COMPETITION_TAG)!!
        )

        competition!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                onChange(snapshot)
            }
        })
    }

    private fun leaveCompetition() {
        competition = null
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun onChange(snapshot: DataSnapshot) {
        val eggs = snapshot.child(Keys.EGG).children
            .map { it.getValue(Egg::class.java)!! }
            .toList()

        val ranks = ScoreBuilder.build(applicationContext, eggs)

        val intent = Intent().apply {
            action = Actions.NEW_SCORES

            putExtra(Extras.RANKS, Gson().toJson(ranks))
        }

        sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, id: Int): Int {
        when (intent?.action) {
            Actions.ENTER_COMPETITION -> enterCompetition(intent)
            Actions.LEAVE_COMPETITION -> leaveCompetition()
        }

        return START_REDELIVER_INTENT
    }
}