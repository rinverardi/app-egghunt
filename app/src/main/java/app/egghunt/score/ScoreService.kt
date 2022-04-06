package app.egghunt.score

import android.app.Service
import android.content.Intent
import android.os.IBinder
import app.egghunt.R
import app.egghunt.competition.CompetitionRepo
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

    private fun assignMedals(ranks: List<Rank>) {
        for (position in 1..3) {
            ranks.filter { it.position == position }.forEach { rank ->
                rank.scores.forEach { score ->
                    score.medal = resources.getStringArray(R.array.medals)[position - 1]
                }
            }
        }
    }

    private fun assignPositions(ranks: List<Rank>) {
        var position = 1

        ranks.forEach {
            it.position = position

            position += it.scores.size
        }
    }

    private fun buildRanks(scores: List<Score>): List<Rank> = scores
        .sortedWith(compareBy(Score::order, Score::hunterDescription))
        .groupBy(Score::count)
        .map { Rank(0, it.value) }

    private fun buildScores(snapshot: DataSnapshot): List<Score> =
        snapshot.child(Keys.EGG).children
            .filter { it.child(Keys.HUNTER_TAG).exists() }
            .groupBy { it.child(Keys.HUNTER_TAG).value }
            .map {
                Score(
                    it.value.size,
                    it.value.first().child(Keys.HUNTER_DESCRIPTION).value as String,
                    it.value.first().child(Keys.HUNTER_TAG).value as String
                )
            }

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
        val scores = buildScores(snapshot)
        val ranks = buildRanks(scores)

        assignPositions(ranks)
        assignMedals(ranks)

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