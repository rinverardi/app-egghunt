package app.egghunt.score

import android.content.Context
import android.content.res.Resources
import app.egghunt.R
import app.egghunt.egg.Egg
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.skyscreamer.jsonassert.JSONAssert

/**
 * A unit test for the score builder.
 */

internal class ScoreBuilderTest {
    private val context = mockk<Context>().also { context ->
        every { context.resources } returns mockk<Resources>().also { resources ->
            every { resources.getStringArray(R.array.medals) } returns arrayOf(
                "gold",
                "silver",
                "bronze"
            )
        }
    }

    /**
     * Build the scores.
     *
     * * 'Fake hunter 1' scores gold.
     * * 'Fake hunter 2' scores silver.
     * * 'Fake hunter 3' scores bronze.
     */

    @Test
    fun build() = doBuild("")

    /**
     * Build the scores.
     *
     * * 'Fake hunter 1' and 'fake hunter 2' share gold.
     * * 'Fake hunter 3' scores bronze.
     */

    @Test
    fun build_sharedGold() = doBuild("_sharedGold")

    /**
     * Build the scores.
     *
     * * 'Fake hunter 1' scores gold.
     * * 'Fake hunter 2' and 'fake hunter 3' share silver.
     */

    @Test
    fun build_sharedSilver() = doBuild("_sharedSilver")

    private fun doBuild(variant: String) {
        val eggs = loadAsObject("eggs${variant}.json", Array<Egg>::class.java)
        val ranks = ScoreBuilder.build(context, eggs.toList())

        val expectedResult = loadAsString("ranks${variant}.json")
        val actualResult = Gson().toJson(ranks)

        JSONAssert.assertEquals(expectedResult, actualResult, true)
    }

    private fun <T> loadAsObject(from: String, to: Class<T>): T {
        val stream = javaClass.getResourceAsStream(from)!!

        return Gson().fromJson(stream.reader(), to)
    }

    private fun loadAsString(from: String): String {
        val stream = javaClass.getResourceAsStream(from)!!

        return stream.bufferedReader().readText()
    }
}