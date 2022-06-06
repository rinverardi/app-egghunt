package app.egghunt.organizer

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
import app.egghunt.action.position.PositionActivity
import app.egghunt.action.scan.ScanActivity
import app.egghunt.lib.Extras
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.startsWith
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HideEggTest {
    @JvmField
    @Rule
    val allowCamera = GrantPermissionRule.grant("android.permission.CAMERA")!!

    @JvmField
    @Rule
    val allowLocation = GrantPermissionRule.grant("android.permission.ACCESS_COARSE_LOCATION")!!

    @JvmField
    @Rule
    var scenarioRule = ActivityScenarioRule<OrganizerActivity>(Intent(
        ApplicationProvider.getApplicationContext(),
        OrganizerActivity::class.java
    ).apply {
        putExtra(Extras.COMPETITION_DESCRIPTION, "Fake Competition")
        putExtra(Extras.COMPETITION_TAG, "CCCCCC")
    })

    private fun doHideEgg() {
        scenarioRule.scenario.onActivity {
            it.reset()
        }

        val buttonHide = onView(allOf(withId(R.id.button_hide)))

        buttonHide.perform(click())

        Intents.intended(hasComponent(ScanActivity::class.qualifiedName))

        openActionBarOverflowOrOptionsMenu(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val item = onView(allOf(withText("Fake Egg")))

        item.perform(click())

        Intents.intended(hasComponent(PositionActivity::class.qualifiedName))

        val buttonAccept = onView(allOf(withId(R.id.button_accept)))

        buttonAccept.perform(click())

        onView(isRoot()).perform(waitForDb())

        val eggDescription = onView(allOf(withId(R.id.description)))

        eggDescription.check(matches(withText("Fake Egg")))

        val eggStatus = onView(allOf(withId(R.id.time_hidden)))

        eggStatus.check(matches(withText(startsWith("Hidden at"))))
    }

    @Test
    fun hideEgg() {
        Intents.init()

        try {
            doHideEgg()
        } finally {
            Intents.release()
        }
    }
}
