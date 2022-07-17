package com.cranked.androidfileconverter.di.module

import com.cranked.androidfileconverter.ui.languages.LanguagesActivity
import com.cranked.androidfileconverter.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun bindmainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bingLanguagesActivity(): LanguagesActivity
}

