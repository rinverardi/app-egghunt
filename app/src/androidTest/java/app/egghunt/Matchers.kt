package app.egghunt

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withResourceName
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.instanceOf

object Matchers {

    /**
     * Match the activity title.
     *
     * Works for both action bars and toolbars.
     */

    fun activityTitle(): Matcher<View> = anyOf(
        allOf(
            instanceOf(TextView::class.java),
            isDisplayed(),
            withParent(withResourceName("action_bar"))
        ),
        allOf(
            instanceOf(TextView::class.java),
            isDisplayed(),
            withParent(withId(R.id.toolbar))
        )
    )

    /**
     * Match the 'up navigation' title.
     */

    fun upButton(): Matcher<View> = allOf(
        instanceOf(ImageButton::class.java),
        isDisplayed(),
        withParent(withId(R.id.toolbar))
    )
}