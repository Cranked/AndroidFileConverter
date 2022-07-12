package com.cranked.androidfileconverter.di.module

import com.cranked.androidfileconverter.ui.camera.CameraFragment
import com.cranked.androidfileconverter.ui.home.HomeFragment
import com.cranked.androidfileconverter.ui.settings.SettingsFragment
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
    abstract fun bindSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun bindToolsFragment(): ToolsFragment

    @ContributesAndroidInjector
    abstract fun bindTransitionFragment(): TransitionFragment

}