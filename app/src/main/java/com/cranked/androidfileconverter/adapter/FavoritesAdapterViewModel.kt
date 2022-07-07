package com.cranked.androidfileconverter.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import javax.inject.Inject

class FavoritesAdapterViewModel @Inject constructor(
    private val favoritesDao: FavoritesDao
) :
    BaseViewModel() {
    val favoritesList = favoritesDao.getAll()


}