package com.cranked.androidfileconverter.ui.main

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.utils.AnimationX
import com.cranked.androidfileconverter.utils.AnimationXUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.textView)
        val fade_animation = AnimationUtils.loadAnimation(this, R.anim.fade_animation)
        val move_animation = AnimationUtils.loadAnimation(this, R.anim.move_animation)
        val slide_animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation)
        val rotate_animation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)

        findViewById<Button>(R.id.button1).setOnClickListener {
            textView.clearAnimation()
            textView.startAnimation(fade_animation)
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            textView.clearAnimation()
            textView.startAnimation(move_animation)
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            textView.clearAnimation()
            textView.startAnimation(slide_animation)
        }
        findViewById<Button>(R.id.button4).setOnClickListener {
            textView.clearAnimation()
            textView.startAnimation(rotate_animation)
        }
        findViewById<Button>(R.id.button5).setOnClickListener {
            val animatorSet = AnimationX().getNewAnimatorSet()
            val animatorSetX = AnimationXUtils.shake(
                textView,
                animatorSet
            )
            AnimationX().setDuration(600).setAnimatorSet(animatorSetX).start()
        }
    }
}