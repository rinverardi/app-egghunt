package app.egghunt.lib

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * A unit test for the QR code parser.
 */

internal class CodeParserTest {

    /**
     * Parse a valid competition code.
     */

    @Test
    fun parse() {
        val codeString = "{" +
                "\"cd\":\"Fake Competition\"," +
                "\"ct\":\"CCCCCC\"}"

        val code = CodeParser.parse(codeString)!!

        assertEquals("Fake Competition", code.cd)
        assertEquals("CCCCCC", code.ct)
        assertNull(code.ed)
        assertNull(code.et)
        assertNull(code.hd)
        assertNull(code.ht)
    }

    /**
     * Parse an invalid competition code (invalid description).
     */

    @Test
    fun parse_invalidDescription() {
        var codeString = "{" +
                "\"cd\":\"${"Fake Competition".padEnd(30, 'X')}\"," +
                "\"ct\":\"CCCCCC\"}"

        for (pass in 1..2) {
            val code = CodeParser.parse(codeString)

            assertEquals(pass == 2, code == null)

            codeString = codeString.replace("Fake Competition", "Fake CompetitionX")
        }
    }

    /**
     * Parse an invalid competition code (invalid tag).
     */

    @Test
    fun parse_invalidTag() {
        var codeString = "{\"ct\":\"CCCCCC\"}"

        for (pass in 1..2) {
            val code = CodeParser.parse(codeString)

            assertEquals(pass == 2, code == null)

            codeString = codeString.replace("CCCCCC", "CCCCCCX")
        }
    }

    /**
     * Parse an invalid competition code (malformed).
     */

    @Test
    fun parse_malformed() {
        var codeString = "{\"ct\":\"CCCCCC\"}"

        for (pass in 1..2) {
            val code = CodeParser.parse(codeString)

            assertEquals(pass == 2, code == null)

            codeString += "X"
        }
    }

    /**
     * Parse a valid egg code.
     */

    @Test
    fun parse_withEgg() {
        val codeString = "{" +
                "\"ct\":\"CCCCCC\"," +
                "\"ed\":\"Fake Egg\"," +
                "\"et\":\"EEEEEE\"}"

        val code = CodeParser.parse(codeString)!!

        assertNull(code.cd)
        assertEquals("CCCCCC", code.ct)
        assertEquals("Fake Egg", code.ed)
        assertEquals("EEEEEE", code.et)
        assertNull(code.hd)
        assertNull(code.ht)
    }

    /**
     * Parse a valid hunter code.
     */

    @Test
    fun parse_withHunter() {
        val codeString = "{" +
                "\"ct\":\"CCCCCC\"," +
                "\"hd\":\"Fake Hunter\"," +
                "\"ht\":\"HHHHHH\"}"

        val code = CodeParser.parse(codeString)!!

        assertNull(code.cd)
        assertEquals("CCCCCC", code.ct)
        assertNull(code.ed)
        assertNull(code.et)
        assertEquals("Fake Hunter", code.hd)
        assertEquals("HHHHHH", code.ht)
    }

    /**
     * Parse an invalid competition code (missing tag).
     */

    @Test
    fun parse_withoutCompetition() {
        var codeString = "{\"ct\":\"CCCCCC\"}"

        for (pass in 1..2) {
            val code = CodeParser.parse(codeString)

            assertEquals(pass == 2, code == null)

            codeString = "{}"
        }
    }

    @Before
    fun setUp() {
        mockkStatic(Log::class)

        every { Log.wtf(any(), any<Throwable>()) } returns 0
    }
}