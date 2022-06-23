package com.cranked.androidfileconverter.ui.splash

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.ui.main.MainActivity
import com.cranked.androidfileconverter.utils.AnimationX
import com.cranked.androidfileconverter.utils.AnimationXUtils
import com.cranked.androidfileconverter.utils.animation.animationStart

class SplashActivity : AppCompatActivity() {
    var animatorSet: AnimatorSet = AnimationX().getNewAnimatorSet()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val imageView = findViewById<ImageView>(R.id.imageView)

        val pulseAnimation = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                imageView.clearAnimation()
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()
            }
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationRepeat(animation: Animator?) = Unit
        }
        val zoomInRightAnimation = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                val animatorSetX =
                    AnimationXUtils.pulse(imageView, AnimationX().getNewAnimatorSet())
                imageView.animationStart(2000, animatorSetX, pulseAnimation)
            }
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationRepeat(animation: Animator?) = Unit
        }
        val animatorSetX =
            AnimationXUtils.zoomInRight(imageView, animatorSet)
        imageView.animationStart(1000, animatorSetX, zoomInRightAnimation)
    }
}