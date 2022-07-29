package com.cranked.androidfileconverter.di.module

import com.cranked.androidfileconverter.ui.languages.LanguagesActivity
import com.cranked.androidfileconverter.ui.main.MainActivity
import com.cranked.androidfileconverter.ui.task.TaskActivity
import com.cranked.androidfileconverter.ui.task.TaskTransitionFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun bindmainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindLanguagesActivity(): LanguagesActivity

    @ContributesAndroidInjector
    abstract fun bindTaskActivity(): TaskActivity
}

