package com.cranked.androidfileconverter.di.module

import androidx.lifecycle.ViewModel
import com.cranked.androidfileconverter.adapter.FavoritesAdapterViewModel
import com.cranked.androidfileconverter.adapter.recentfile.RecentFileAdapterViewModel
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.main.MainViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
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
    abstract fun favoriteAdapterViewModel(viewModel: FavoritesAdapterViewModel):ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(RecentFileAdapterViewModel::class)
    abstract fun recentFileAdapterViewModel(viewModel: RecentFileAdapterViewModel):ViewModel


}