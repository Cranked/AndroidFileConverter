package com.cranked.androidfileconverter.utils.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.view.View
import com.cranked.androidfileconverter.utils.AnimationX

fun View.animationStart(
    duration: Long = 1000,
    animatorSet: AnimatorSet,
    listener: Animator.AnimatorListener? = null
) {
    renderAnimation(animatorSet, duration, listener)
}

fun View.renderAnimation(
    animatorSet: AnimatorSet,
    duration: Long,
    listener: Animator.AnimatorListener? = null
) {
    val aniObject = AnimationX().setDuration(duration)
        .setAnimatorSet(animatorSet)
    aniObject.getAnimatorX().addListener(listener)
    aniObject.start()
}


