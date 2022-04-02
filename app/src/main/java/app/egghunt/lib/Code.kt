package app.egghunt.lib

data class Code(
    val cd: String?,
    val ct: String?,
    val ed: String?,
    val et: String?,
    val hd: String?,
    val ht: String?
) {
    fun isEgg(): Boolean = et != null

    fun isHunter(): Boolean = ht != null
}