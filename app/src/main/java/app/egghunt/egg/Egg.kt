package app.egghunt.egg

data class Egg(
    val description: String? = null,
    val hunterDescription: String? = null,
    val hunterTag: String? = null,
    val positionLatitude: Double? = null,
    val positionLongitude: Double? = null,
    var tag: String? = null,
    val timeFound: Long? = null,
    val timeHidden: Long? = null
)