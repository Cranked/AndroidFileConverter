package com.cranked.androidfileconverter.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import javax.inject.Inject

class FavoritesAdapterViewModel @Inject constructor(
    private val favoritesDao: FavoritesDao
) :
    BaseViewModel() {
    val favoritesList = favoritesDao.getAll()
    fun setAdapter(
        context: Context,
        recyclerView: RecyclerView,
        favoritesAdapter: FavoritesAdapter,
        list: List<FavoriteFile>,
    ) {
        favoritesAdapter.setItems(list)
        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = favoritesAdapter
        }
    }

}