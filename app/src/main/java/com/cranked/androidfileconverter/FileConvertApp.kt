package com.cranked.androidfileconverter

import com.cranked.androidfileconverter.data.database.AppDataBase
import com.cranked.androidfileconverter.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class FileConvertApp : DaggerApplication() {
    val appComponent = DaggerAppComponent.builder().application(this).build()
    override fun applicationInjector(): AndroidInjector<DaggerApplication> {
        return this.appComponent
    }

    override fun onCreate() {
        super.onCreate()
        AppDataBase.invoke(this)
        this.appComponent.inject(this)
    }
}