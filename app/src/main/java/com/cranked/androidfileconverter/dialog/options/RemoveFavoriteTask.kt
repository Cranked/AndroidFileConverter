package com.cranked.androidfileconverter.dialog.options

import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel

class RemoveFavoriteTask(private val viewModel: HomeFragmentViewModel,private val favoritesDao: FavoritesDao,private val favoriteFile: FavoriteFile):ITask() {
    override fun doTask() {
        favoritesDao.delete(favoriteFile)
        viewModel.getFavItemsChangedMutableLiveData().postValue(true)
    }
}