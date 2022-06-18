package app.egghunt.hunter

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import app.egghunt.Actions.waitForDb
import app.egghunt.R
import app.egghunt.action.scan.ScanActivity
import app.egghunt.lib.Extras
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.startsWith
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * An integration test for the 'find egg' workflow.
 *
 * Preconditions: Logged in to the 'fake competition'  as the 'fake hunter'.
 *
 * _Hunter Activity_
 *
 * (1) Reset the remote data.
 * (2) Click the 'find' button.
 *
 * _Scan Activity_
 *
 * (3) Open the options menu.
 * (4) Scan the QR code for the 'fake egg'.
 *
 * _Hunter Activity_
 *
 * (5) Assert that the 'fake egg' is listed as found.
 */

@LargeTest
@RunWith(AndroidJUnit4::class)
class FindEggTest {
    @JvmField
    @Rule
    val allowCamera = GrantPermissionRule.grant("android.permission.CAMERA")!!

    @JvmField
    @Rule
    var scenarioRule = ActivityScenarioRule<HunterActivity>(Intent(
        ApplicationProvider.getApplicationContext(),
        HunterActivity::class.java
    ).apply {
        putExtra(Extras.COMPETITION_DESCRIPTION, "Fake Competition")
        putExtra(Extras.COMPETITION_TAG, "CCCCCC")
        putExtra(Extras.HUNTER_DESCRIPTION, "Fake Hunter")
        putExtra(Extras.HUNTER_TAG, "HHHHHH")
    })

    private fun doFindEgg() {
        scenarioRule.scenario.onActivity {
            it.reset()
        }

        val buttonFind = onView(allOf(withId(R.id.button_find)))

        buttonFind.perform(click())

        Intents.intended(hasComponent(ScanActivity::class.qualifiedName))

        openActionBarOverflowOrOptionsMenu(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val item = onView(allOf(withText("Fake Egg")))

        item.perform(click())

        onView(isRoot()).perform(waitForDb())

        val eggDescription = onView(allOf(withId(R.id.description)))

        eggDescription.check(matches(withText("Fake Egg")))

        val eggStatus = onView(allOf(withId(R.id.time_found)))

        eggStatus.check(matches(withText(startsWith("Found at"))))
        eggStatus.check(matches(withText(containsString("by Fake Hunter"))))
    }

    @Test
    fun findEgg() {
        Intents.init()

        try {
            doFindEgg()
        } finally {
            Intents.release()
        }
    }
}
