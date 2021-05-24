package com.example.android_chess

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun onClick() {
        onView(withId(R.id.reset_button)).perform(click())
        onView(withId(R.id.chess_view)).check(ViewAssertions.matches(object : BaseMatcher<View>() {
            override fun describeTo(description: Description?) {

            }

            override fun matches(item: Any?): Boolean {
                if (item != null && item is ChessFront) {
                    return !item.clickedOnce
                }
                return false
            }

        }))
    }

    @Test
    fun clickableBoard() {
        onView(withId(R.id.chess_view)).perform(click())
    }
}