package com.cranked.androidfileconverter.dialog.options

import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel

class RemoveFavoriteTask(private val favoritesDao: FavoritesDao, private val favoriteFile: FavoriteFile) : ITask() {
    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {
    }

    override fun doTask(homeFragmentViewModel: HomeFragmentViewModel) {
        favoritesDao.delete(favoriteFile)
        homeFragmentViewModel.getFavItemsChangedMutableLiveData().postValue(true)
    }
}