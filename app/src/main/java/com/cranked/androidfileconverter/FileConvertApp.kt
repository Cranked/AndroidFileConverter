package com.cranked.androidfileconverter

import com.cranked.androidcorelibrary.local.PrefManager
import com.cranked.androidfileconverter.data.database.AppDataBase
import com.cranked.androidfileconverter.di.component.DaggerAppComponent
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.enums.FilterState
import com.cranked.androidfileconverter.utils.enums.LayoutState
import com.cranked.androidfileconverter.utils.rxjava.RxBus
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class FileConvertApp : DaggerApplication() {
    val appComponent = DaggerAppComponent.builder().application(this).build()
    val prefManager by lazy {
        PrefManager(this)
    }
    val rxBus by lazy {
        RxBus()
    }

    override fun applicationInjector(): AndroidInjector<DaggerApplication> {
        return this.appComponent
    }


    override fun onCreate() {
        super.onCreate()
        AppDataBase.invoke(this)
        this.appComponent.inject(this)
    }

    fun getLanguage() = prefManager.get(Constants.LANGUAGE, Constants.DEFAULT_LANGUAGE).toString()
    fun setLanguage(language: String) {
        prefManager.addValueCommit(Constants.LANGUAGE, language)
    }

    fun getPageDefaultSize() = prefManager.get(Constants.DEFAULT_PAGE_SIZE, Constants.DEFAULT_PAGE_SIZE_VALUE)
    fun setPageDefaultSize(pageType: String) {
        prefManager.addValueCommit(Constants.DEFAULT_PAGE_SIZE, pageType)
    }

    fun getLayoutState() = prefManager.getSharedPreferences().getInt(Constants.LAYOUT_STATE, LayoutState.LIST_LAYOUT.value)
    fun setLayoutState(value: Int) {
        prefManager.addValueCommit(Constants.LAYOUT_STATE, value)
    }


    fun setFilterState(value: Int) {
        prefManager.addValueCommit(Constants.FILTER_STATE, value)
    }

    fun getFilterState() = prefManager.getSharedPreferences()
        .getInt(Constants.FILTER_STATE, FilterState.ORDERBYNAME_A_TO_Z.value)

}