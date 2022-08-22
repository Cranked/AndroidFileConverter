package com.cranked.androidfileconverter.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.adapter.favorites.FavoritesAdapter
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import javax.inject.Inject

class FavoritesAdapterViewModel @Inject constructor() :
    BaseViewModel() {
    fun setAdapter(
        context: Context,
        recyclerView: RecyclerView,
        favoritesAdapter: FavoritesAdapter,
        list: List<FavoriteFile>,
    ): FavoritesAdapter {
        favoritesAdapter.apply {
            setItems(list)
        }
        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = favoritesAdapter
        }
        return favoritesAdapter
    }

}