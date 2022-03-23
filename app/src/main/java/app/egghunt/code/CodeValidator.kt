package app.egghunt.code

object CodeValidator {
    private fun checkDescription(name: String, value: String?) {
        if (value != null) {
            if (value.length > 30) {
                throw IllegalArgumentException(name)
            }
        }
    }

    private fun checkTag(name: String, value: String?) {
        if (value != null) {
            if (!Regex("[0-9a-zA-Z]{6}").matches(value)) {
                throw IllegalArgumentException(name)
            }
        }
    }

    fun validate(code: Code) {
        checkDescription("cd", code.cd)
        checkTag("ct", code.ct)
        checkDescription("ed", code.ed)
        checkTag("et", code.et)
        checkDescription("hd", code.hd)
        checkTag("ht", code.ht)
    }
}