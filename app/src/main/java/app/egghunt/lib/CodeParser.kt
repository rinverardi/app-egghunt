package app.egghunt.lib

import com.google.gson.Gson

object CodeParser {
    private fun checkDescription(name: String, value: String?) {
        if (value != null && value.length > 30) {
            throw IllegalArgumentException(name)
        }
    }

    private fun checkTag(name: String, value: String?) {
        if (value != null && !Regex("[0-9a-zA-Z]{6}").matches(value)) {
            throw IllegalArgumentException(name)
        }
    }

    fun parse(codeString: String): Code? {

        // abort if qr code does not contain competition tag
        if (!codeString.contains("ct")) {
            return null
        }

        return try {
            val code = Gson().fromJson(codeString, Code::class.java)
            checkDescription("cd", code.cd)
            checkTag("ct", code.ct)
            checkDescription("ed", code.ed)
            checkTag("et", code.et)
            checkDescription("hd", code.hd)
            checkTag("ht", code.ht)
            code
        } catch(exception: IllegalArgumentException) {
            return null
        }
    }

}