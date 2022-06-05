package app.egghunt.welcome

import android.widget.Button
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import app.egghunt.Matchers
import app.egghunt.Matchers.activityTitle
import app.egghunt.R
import app.egghunt.action.scan.ScanActivity
import app.egghunt.lib.LocalData
import app.egghunt.organizer.OrganizerActivity
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginAsOrganizerTest {
    @JvmField
    @Rule
    val allowCamera = GrantPermissionRule.grant("android.permission.CAMERA")!!

    @JvmField
    @Rule
    var scenarioRule = ActivityScenarioRule(WelcomeActivity::class.java)

    private fun doLoginAsOrganizer() {
        val button = onView(allOf(withId(R.id.button)))

        button.perform(click())

        Intents.intended(hasComponent(ScanActivity::class.qualifiedName))

        Espresso.openActionBarOverflowOrOptionsMenu(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val menuItem = onView(allOf(withText("Fake Competition")))

        menuItem.perform(click())

        Intents.intended(hasComponent(OrganizerActivity::class.qualifiedName))

        onView(activityTitle()).check(matches(withText("Organizer")))
    }

    private fun doLogout() {
        val upButton = onView(Matchers.upButton())

        upButton.perform(click())

        val logoutButton = onView(allOf(instanceOf(Button::class.java), withText("Logout")))

        logoutButton.perform(click())

        Intents.intended(hasComponent(WelcomeActivity::class.qualifiedName))
    }

    @Test
    fun loginAsOrganizer() {
        Intents.init()

        try {
            doLoginAsOrganizer()
            doLogout()
        } finally {
            Intents.release()
        }
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUp() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext

            LocalData.clear(context)
        }
    }
}
