package com.cranked.androidfileconverter.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

object AnimationXUtils {
    fun rubberBand(view: View, animatorSet: AnimatorSet): AnimatorSet {
        val object1: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.75f, 1.25f, 0.85f, 1f)
        val object2: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.25f, 0.75f, 1.15f, 1f)
        animatorSet.playTogether(object1, object2)
        return animatorSet
    }

    fun shake(view: View, animatorSet: AnimatorSet): AnimatorSet {
        val object1: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "translationX", 0f, 10f, -10f, 6f, -6f, 3f, -3f, 0f)

        animatorSet.playTogether(object1)
        return animatorSet
    }

    fun bounce(view: View, animatorSet: AnimatorSet): AnimatorSet {
        val object1: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "translationY", 0f, 10f, -10f, 6f, -6f, 3f, -3f, 0f)

        animatorSet.playTogether(object1)
        return animatorSet
    }

    fun swing(view: View, animatorSet: AnimatorSet): AnimatorSet {
        val object1: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "rotation", 0f, 10f, -10f, 6f, -6f, 3f, -3f, 0f)
        animatorSet.playTogether(object1)
        return animatorSet
    }

    fun zoomInRight(view: View, animatorSet: AnimatorSet): AnimatorSet {
        val width = -view.width.toFloat()
        val right = -view.paddingRight.toFloat()
        val object1 = ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 0.475f, 1f)
        val object2 = ObjectAnimator.ofFloat(view, "scaleY", 0.1f, 0.475f, 1f)
        val object3 = ObjectAnimator.ofFloat(view, "translationX", width + right, -48f, 0f)
        val object4 = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f, 1f)
        animatorSet.playTogether(object1, object2, object3, object4)
        return animatorSet
    }

    fun pulse(view: View, animatorSet: AnimatorSet): AnimatorSet {
        val object1: ObjectAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f)
        val object2: ObjectAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f)
        animatorSet.playTogether(object1, object2)
        return animatorSet
    }

}