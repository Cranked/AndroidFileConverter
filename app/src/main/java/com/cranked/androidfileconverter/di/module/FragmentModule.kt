package com.cranked.androidfileconverter.di.module

import com.cranked.androidfileconverter.ui.camera.CameraFragment
import com.cranked.androidfileconverter.ui.camera.CameraImageFragment
import com.cranked.androidfileconverter.ui.filetype.FileTypeFragment
import com.cranked.androidfileconverter.ui.home.HomeFragment
import com.cranked.androidfileconverter.ui.settings.SettingsFragment
import com.cranked.androidfileconverter.ui.task.TaskFragment
import com.cranked.androidfileconverter.ui.task.TaskTransitionFragment
import com.cranked.androidfileconverter.ui.tools.ToolsFragment
import com.cranked.androidfileconverter.ui.transition.TransitionFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun bingCameraFragment(): CameraFragment

    @ContributesAndroidInjector
    abstract fun bindCameraImageFragment(): CameraImageFragment

    @ContributesAndroidInjector
    abstract fun bindSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun bindToolsFragment(): ToolsFragment

    @ContributesAndroidInjector
    abstract fun bindTransitionFragment(): TransitionFragment

    @ContributesAndroidInjector
    abstract fun bindTaskFragment(): TaskFragment

    @ContributesAndroidInjector
    abstract fun bindTaskTransitionFragment(): TaskTransitionFragment

    @ContributesAndroidInjector
    abstract fun bindFileTypeFragment(): FileTypeFragment


}