package app.egghunt.score

data class Score(
    val count: Int,
    val hunterDescription: String,
    val hunterTag: String,
    var medal: String = ""
) {
    val order = -count
}