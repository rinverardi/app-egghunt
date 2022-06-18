package app.egghunt

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

object Actions {

    /**
     * Wait for the DB to be updated.
     *
     * Used as a workaround when a test case fails due to an asynchronous DB
     * update.
     */

    fun waitForDb(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()

            override fun getDescription(): String = "give the database a little time"

            override fun perform(controller: UiController, view: View) {
                controller.loopMainThreadForAtLeast(1000)
            }
        }
    }

    /**
     * Wait for the UI to be updated.
     *
     * Used as a workaround when a test case fails due to an asynchronous UI
     * update.
     */

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