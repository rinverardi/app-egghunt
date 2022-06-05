package app.egghunt.organizer

import android.content.Intent
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.egghunt.R
import app.egghunt.lib.Extras
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.startsWith
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class PostHintTest {
    @JvmField
    @Rule
    var scenarioRule = ActivityScenarioRule<OrganizerActivity>(Intent(
        ApplicationProvider.getApplicationContext(),
        OrganizerActivity::class.java
    ).apply {
        putExtra(Extras.COMPETITION_DESCRIPTION, "Fake Competition")
        putExtra(Extras.COMPETITION_TAG, "CCCCCC")
    })

    @Test
    fun postHint() {
        scenarioRule.scenario.onActivity {
            it.reset()
        }

        val tab = onView(allOf(instanceOf(TextView::class.java), withText("Hints")))

        tab.perform(click())

        val edit = onView(allOf(withId(R.id.edit_post)))

        edit.perform(replaceText("Fake Hint"))

        val button = onView(allOf(withId(R.id.button_post)))

        button.perform(click())

        val hintText = onView(allOf(withId(R.id.text)))

        hintText.check(matches(withText("Fake Hint")))

        val hintStatus = onView(allOf(withId(R.id.time_posted)))

        hintStatus.check(matches(withText(startsWith("Posted at"))))
    }
}
