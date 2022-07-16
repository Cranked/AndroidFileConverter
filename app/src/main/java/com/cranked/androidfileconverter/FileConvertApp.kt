package com.cranked.androidfileconverter

import com.cranked.androidcorelibrary.local.PrefManager
import com.cranked.androidfileconverter.data.database.AppDataBase
import com.cranked.androidfileconverter.di.component.DaggerAppComponent
import com.cranked.androidfileconverter.utils.Constants
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class FileConvertApp : DaggerApplication() {
    val appComponent = DaggerAppComponent.builder().application(this).build()
   val prefManager by lazy {
       PrefManager(this)
   }

    override fun applicationInjector(): AndroidInjector<DaggerApplication> {
        return this.appComponent
    }


    override fun onCreate() {
        super.onCreate()
        AppDataBase.invoke(this)
        this.appComponent.inject(this)
    }

    fun getLanguage(): String {
        return prefManager.get(Constants.LANGUAGE, Constants.DEFAULT_LANGUAGE).toString()
    }

    fun setLanguage(language: String) {
        prefManager.addValueCommit(Constants.LANGUAGE, language)
    }
}