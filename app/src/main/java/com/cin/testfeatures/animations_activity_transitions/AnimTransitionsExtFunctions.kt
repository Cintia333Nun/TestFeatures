package com.cin.testfeatures.animations_activity_transitions

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.cin.testfeatures.R

/**
 * @author Cintia333Nun
 *
 * CUSTOM ANIM FOR TRANSITIONS
 *
 * System Types:
 * Enter ->
 * Exit <-
 * Shared elements o -> <- O (same Image o (transform to) O)
 *
 * Android Supports
 *
 * Enter and Exit transitions types:
 * Explode (in our out to center)
 * Slide (in our out to the edges)
 * Fade (change opacity)
 *
 *  Shared elements transitions types: (Transform)
 *  ChangeBounds (layout bounds of target view)
 *  ChangeClipBounds (clip bounds of target view)
 *  ChangeTransform (scale and rotation of target view)
 *  ChangeImageTransform (size and scale of target image)
 *
 *  Available on API 28 (android 5 Lollipop) and Up
 *  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
 *
 *  In Style app:
 *  Enable window content transitions -> <item name="android:windowContentTransitions">true</item>
 *  You can specify enter and Exit animations (Or programmatically)
 *  You can specify shared elements animations (Or programmatically)
 *
 *
 * */

//region KEYS SHARED ELEMENTS
const val IMAGE_ASSISTANT = "IMAGE_ASSISTANT"
const val VIEW_TOOLBAR = "IMAGE_ASSISTANT"
//endregion

//region CONFIG WINDOW AND START TRANSITION
/**
 * If android:windowContentTransitions is disabled in the app Theme add Before Content
 * @param isEnableAnim Status of user-defined animations in settings module
 * */
fun Window.enableTransitions(isEnableAnim: Boolean) {
    if (isEnableAnim) requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
}

/**
 * Start transition activity with same animation
 * @param isEnableAnim Status of user-defined animations in settings module
 * @param intent Go to the next Activity
 * */
fun AppCompatActivity.transition(isEnableAnim: Boolean, intent: Intent) {
    if (isEnableAnim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
        } else {
            val options = ActivityOptions.makeCustomAnimation(
                this, R.anim.side_in, R.anim.side_out
            )
            startActivity(intent, options.toBundle())
        }
    } else startActivity(intent)
}
//endregion

//region ENTER AND EXIT TRANSITIONS
/** Define types Enter and Exit animations for transitions */
enum class TypeAnimEnterExit{EXPLODE,SLIDE,FADE}
/**
 * Config animations on API 28 (android 5 Lollipop) and Up, define types anim
 * @param isEnableAnim Status of user-defined animations in settings module
 * @param overlap Start and exit transition as soon as possible (dramatic)
 * @param typeAnimEnterExit Define type Project Animations
 * */
fun Window.configEnterExitAnim(
    isEnableAnim: Boolean = false, overlap: Boolean = false,
    typeAnimEnterExit: TypeAnimEnterExit = TypeAnimEnterExit.SLIDE
) {
    if (isEnableAnim) {
        allowEnterTransitionOverlap = overlap //Dramatic
        allowReturnTransitionOverlap = overlap
        when (typeAnimEnterExit) {
            TypeAnimEnterExit.EXPLODE -> configEnterExitAnimExplode()
            TypeAnimEnterExit.SLIDE -> configEnterExitAnimSlide()
            TypeAnimEnterExit.FADE -> configEnterExitAnimFade()
        }
    }
}

private fun Window.configEnterExitAnimExplode() {
    enterTransition = Explode()
    exitTransition = Explode()
}

private fun Window.configEnterExitAnimSlide() {
    enterTransition = Slide()
    exitTransition = Slide()
}

private fun Window.configEnterExitAnimFade() {
    enterTransition = Fade()
    exitTransition = Fade()
}
//endregion

//region SHARED ELEMENTS TRANSITIONS
/**
 * Start transition activity with animation on shared objects
 * @param isEnableAnim Status of user-defined animations in settings module
 * @param intent Go to the next Activity
 * @param sharedElements Shared Objects
 * */
fun AppCompatActivity.transitionWithShadeObjects(
    isEnableAnim: Boolean, intent: Intent,
    vararg sharedElements: android.util.Pair<View, String>
) {
    val options = ActivityOptions.makeSceneTransitionAnimation(
        this, *sharedElements
    )

    if (isEnableAnim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, options.toBundle())
        } else {
            val anim = ActivityOptions.makeCustomAnimation(
                this, R.anim.side_in, R.anim.side_out
            )
            startActivity(intent, anim.toBundle())
        }
    } else startActivity(intent)
}

/**
 *
 * */

fun View.configTransition(id: String) {
    ViewCompat.setTransitionName(this,id)
}

/**
 *
 * */
fun Window.transitionWithShadeObjects(listener: () -> Unit): Boolean {
    val transition = sharedElementEnterTransition
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if (transition != null) {
            transition.addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(p0: Transition?) {
                    listener.invoke()
                }

                override fun onTransitionEnd(p0: Transition?) {}

                override fun onTransitionCancel(p0: Transition?) {
                    transition.removeListener(this)
                }

                override fun onTransitionPause(p0: Transition?) {}

                override fun onTransitionResume(p0: Transition?) {}
            })
            true
        } else false
    } else false
}
//endregion