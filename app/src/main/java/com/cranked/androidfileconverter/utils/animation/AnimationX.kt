package com.cranked.androidfileconverter.utils

import android.animation.AnimatorSet
import android.view.animation.AccelerateInterpolator

class AnimationX {
    private var mAnimatorX = AnimatorSet()
    private var mDuration: Long = 1000
    private lateinit var mAnimatorSetX: AnimatorSet

    fun setDuration(duration: Long): AnimationX {
        this.mDuration = duration
        return this
    }

    fun setAnimatorSet(animatorset: AnimatorSet): AnimationX {
        this.mAnimatorSetX = animatorset
        return this
    }

    fun start(): AnimationX {
        mAnimatorSetX.duration = mDuration
        mAnimatorSetX.interpolator = AccelerateInterpolator()
        mAnimatorSetX.start()
        return this
    }

    fun getAnimatorX() = mAnimatorSetX
    fun getNewAnimatorSet() = mAnimatorX

}