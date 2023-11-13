package com.cin.testfeatures

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cin.testfeatures.test_iu_espresso.EspressoActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EspressoActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(EspressoActivity::class.java)

    @Test
    fun testButtonClick() {
        // Realizar clic en el botón
        onView(withId(R.id.button_test)).perform(click())
        // Verificar que se ha realizado el clic y se ha actualizado el texto del botón
        onView(withId(R.id.button_test)).check(matches(withText("Prueba realizada")))
    }
}