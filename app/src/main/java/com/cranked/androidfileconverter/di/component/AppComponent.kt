package com.cranked.androidfileconverter.di.component

import android.app.Application
import com.cranked.androidfileconverter.di.module.*
import com.cranked.androidfileconverter.ui.camera.CameraFragment
import com.cranked.androidfileconverter.ui.home.HomeFragment
import com.cranked.androidfileconverter.ui.languages.LanguagesActivity
import com.cranked.androidfileconverter.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

@SuppressWarnings("unchecked")
@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, DataBaseModule::class, ActivityModule::class, FragmentModule::class, ViewModelModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance // Binds a particular instance of the object through the component of the time of construction
        fun application(application: Application): Builder // This makes the application available through all modules available
        fun build(): AppComponent
    }

    override fun inject(application: DaggerApplication?)
    fun bindHomeFragment(fragment: HomeFragment)
    fun bindCameraFragment(fragment: CameraFragment)
    fun bindMainActivity(activity: MainActivity)
    fun bindLanguagesActivity(activity: LanguagesActivity)
}
