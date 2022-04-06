package app.egghunt.score

data class Rank(
    var position: Int,
    val scores: List<Score> = ArrayList()
)