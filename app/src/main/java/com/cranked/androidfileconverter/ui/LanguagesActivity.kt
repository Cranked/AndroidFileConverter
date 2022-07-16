package com.cranked.androidfileconverter.ui

import android.os.Bundle
import com.cranked.androidcorelibrary.ui.raw.RawActivity
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.ui.main.MainActivity

class LanguagesActivity : RawActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_languages)
    }

    override fun onBackPressed() {
        startActivity(MainActivity::class.java, true)
    }
}