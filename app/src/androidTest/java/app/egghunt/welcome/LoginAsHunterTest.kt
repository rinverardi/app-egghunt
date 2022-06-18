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
import app.egghunt.Matchers.activityTitle
import app.egghunt.Matchers.upButton
import app.egghunt.R
import app.egghunt.action.scan.ScanActivity
import app.egghunt.hunter.HunterActivity
import app.egghunt.lib.LocalData
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * An integration test for the 'login as hunter' workflow.
 *
 * Preconditions: Logged out.
 *
 * _Welcome Activity_
 *
 * (1) Click the 'scan' button.
 *
 * _Scan Activity_
 *
 * (2) Open the options menu.
 * (3) Scan the QR code for the 'fake hunter'.
 *
 * _Hunter Activity_
 *
 * (4) Check the activity title.
 * (5) Click the 'up navigation' button.
 * (6) Click the 'logout' button.
 */

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginAsHunterTest {
    @JvmField
    @Rule
    val allowCamera = GrantPermissionRule.grant("android.permission.CAMERA")!!

    @JvmField
    @Rule
    var scenarioRule = ActivityScenarioRule(WelcomeActivity::class.java)

    private fun doLoginAsHunter(hunter: String) {
        val button = onView(allOf(withId(R.id.button)))

        button.perform(click())

        Intents.intended(hasComponent(ScanActivity::class.qualifiedName))

        Espresso.openActionBarOverflowOrOptionsMenu(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        val menuItem = onView(allOf(withText(hunter)))

        menuItem.perform(click())

        Intents.intended(hasComponent(HunterActivity::class.qualifiedName))

        onView(activityTitle()).check(matches(withText(hunter)))
    }

    private fun doLogout() {
        val upButton = onView(upButton())

        upButton.perform(click())

        val logoutButton = onView(allOf(instanceOf(Button::class.java), withText("Logout")))

        logoutButton.perform(click())

        Intents.intended(hasComponent(WelcomeActivity::class.qualifiedName))
    }

    @Test
    fun loginAsHunter1() {
        Intents.init()

        try {
            doLoginAsHunter("Fake Hunter 1")
            doLogout()
        } finally {
            Intents.release()
        }
    }

    @Test
    fun loginAsHunter2() {
        Intents.init()

        try {
            doLoginAsHunter("Fake Hunter 2")
            doLogout()
        } finally {
            Intents.release()
        }
    }

    @Test
    fun loginAsHunter3() {
        Intents.init()

        try {
            doLoginAsHunter("Fake Hunter 3")
            doLogout()
        } finally {
            Intents.release()
        }
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpClass() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext

            LocalData.clear(context)
        }
    }
}
