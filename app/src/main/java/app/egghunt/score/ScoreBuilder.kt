package app.egghunt.score

import android.content.Context
import app.egghunt.R
import app.egghunt.egg.Egg

object ScoreBuilder {
    private fun assignMedals(context: Context, ranks: List<Rank>) {
        for (position in 1..3) {
            ranks.filter { it.position == position }.forEach { rank ->
                rank.scores.forEach { score ->
                    score.medal = context.resources.getStringArray(R.array.medals)[position - 1]
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

    fun build(context: Context, eggs: List<Egg>): List<Rank> {
        val scores = buildScores(eggs)
        val ranks = buildRanks(scores)

        assignPositions(ranks)
        assignMedals(context, ranks)

        return ranks
    }

    private fun buildRanks(scores: List<Score>): List<Rank> = scores
        .sortedWith(compareBy(Score::order, Score::hunterDescription))
        .groupBy(Score::count)
        .map { Rank(0, it.value) }

    private fun buildScores(eggs: List<Egg>): List<Score> =
        eggs
            .filter { it.hunterTag != null }
            .groupBy { it.hunterTag }
            .map {
                Score(
                    it.value.size,
                    it.value.first().hunterDescription!!,
                    it.value.first().hunterTag!!
                )
            }
}