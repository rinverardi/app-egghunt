package app.egghunt.score

data class Score(
    val count: Int,
    val hunterDescription: String,
    val hunterTag: String,
    var medal: String = ""
) {
    @Transient
    val order = -count
}