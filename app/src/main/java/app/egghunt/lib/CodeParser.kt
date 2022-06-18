package app.egghunt.lib

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

/**
 * A parser for QR codes.
 *
 * _Key Responsibilities_
 *
 * * Parse competition codes.
 * * Parse egg codes.
 * * Parse hunter codes.
 */

object CodeParser {
    private fun allowDescription(value: String?): Boolean =
        value == null || value.length <= 30

    private fun allowTag(value: String?): Boolean =
        value == null || Regex("[0-9a-zA-Z]{6}").matches(value)

    private fun requireTag(value: String?): Boolean =
        value != null && allowTag(value)

    fun parse(codeString: String): Code? {
        try {
            val code = Gson().fromJson(codeString, Code::class.java)

            if (
                allowDescription(code.cd) &&
                requireTag(code.ct) &&
                allowDescription(code.ed) &&
                allowTag(code.et) &&
                allowDescription(code.hd) &&
                allowTag(code.ht)
            ) {
                return code
            }
        } catch (exception: JsonSyntaxException) {
            Log.wtf(javaClass.simpleName, exception)
        }

        return null
    }
}