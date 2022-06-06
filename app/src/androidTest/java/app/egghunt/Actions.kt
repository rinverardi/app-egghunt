package app.egghunt

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

object Actions {
    fun waitForDb(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()

            override fun getDescription(): String = "give the database a little time"

            override fun perform(controller: UiController, view: View) {
                controller.loopMainThreadForAtLeast(1000)
            }
        }
    }

    fun waitForUi(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()

            override fun getDescription(): String = "give the user interface a little time"

            override fun perform(controller: UiController, view: View) {
                controller.loopMainThreadForAtLeast(1000)
            }
        }
    }
}