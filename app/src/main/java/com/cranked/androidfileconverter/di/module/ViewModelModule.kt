package com.cranked.androidfileconverter.di.module

import androidx.lifecycle.ViewModel
import com.cranked.androidfileconverter.adapter.FavoritesAdapterViewModel
import com.cranked.androidfileconverter.adapter.recentfile.RecentFileAdapterViewModel
import com.cranked.androidfileconverter.ui.camera.CameraFragmentViewModel
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.languages.LanguageActivityViewModel
import com.cranked.androidfileconverter.ui.main.MainViewModel
import com.cranked.androidfileconverter.ui.settings.SettingsFragmentViewModel
import com.cranked.androidfileconverter.ui.task.TaskActivityViewModel
import com.cranked.androidfileconverter.ui.task.TaskFragmentViewModel
import com.cranked.androidfileconverter.ui.task.TaskTransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentViewModel::class)
    abstract fun homefragmentListViewModel(viewModel: HomeFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesAdapterViewModel::class)
    abstract fun favoriteAdapterViewModel(viewModel: FavoritesAdapterViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(RecentFileAdapterViewModel::class)
    abstract fun recentFileAdapterViewModel(viewModel: RecentFileAdapterViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TransitionFragmentViewModel::class)
    abstract fun transitionFragmentViewModel(viewModel: TransitionFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsFragmentViewModel::class)
    abstract fun settingsFragmentViewModel(viewModel: SettingsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LanguageActivityViewModel::class)
    abstract fun languagesActivityViewModel(viewModel: LanguageActivityViewModel): ViewModel

    @Singleton
    @Binds
    @IntoMap
    @ViewModelKey(TaskActivityViewModel::class)
    abstract fun taskActivityViewModel(viewModel: TaskActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskFragmentViewModel::class)
    abstract fun taskFragmentViewModel(viewModel: TaskFragmentViewModel): ViewModel

    @Singleton
    @Binds
    @IntoMap
    @ViewModelKey(TaskTransitionFragmentViewModel::class)
    abstract fun taskTransitionFragmentViewModel(viewModel: TaskTransitionFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CameraFragmentViewModel::class)
    abstract fun cameraFragmentViewModel(viewModel: CameraFragmentViewModel): ViewModel
}