package com.cranked.androidfileconverter.ui.languages

import android.os.Bundle
import android.view.MenuItem
import com.cranked.androidcorelibrary.ui.base.BaseDaggerActivity
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.language.LanguageAdapter
import com.cranked.androidfileconverter.databinding.ActivityLanguagesBinding
import com.cranked.androidfileconverter.ui.splash.SplashActivity
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.localization.LocalizationUtil
import java.util.*

class LanguagesActivity : BaseDaggerActivity<LanguageActivityViewModel, ActivityLanguagesBinding>(
    LanguageActivityViewModel::class.java,
    R.layout.activity_languages) {
    val languageAdapter by lazy {
        LanguageAdapter()
    }
    val app by lazy {
        (applicationContext as FileConvertApp)
    }
    val languageList by lazy {
        Constants.languagesKeyValue
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app.appComponent.bindLanguagesActivity(this)
        viewModel.setToolbar(binding.languagesMenu, getString(R.string.language))
        setSupportActionBar(binding.languagesMenu)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        binding.languagesMenu.setNavigationOnClickListener {
            restart()
        }
        val selectedLang = app.getLanguage()
        val mutableLangList =
            viewModel.toListLanguageModel(languageList, selectedLang).toMutableList()
        viewModel.setAdapter(this,
            binding.languagesRecylerView,
            languageAdapter,
            mutableLangList)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                restart()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onBackPressed() {
        restart()
    }

    override fun createLiveData() {
        viewModel.selectedLanguage.observe(this) {
            if (app.getLanguage() != it) {
                LocalizationUtil.applyLanguageContext(this, Locale(it))
                app.setLanguage(it)
                languageAdapter.setItems(viewModel.toListLanguageModel(languageList,
                    app.getLanguage()))
            }
        }

    }

    fun restart() {
        reStartApp(SplashActivity::class.java)
    }

    override fun initViewModel(viewModel: LanguageActivityViewModel) {
        binding.viewModel = viewModel
    }
}