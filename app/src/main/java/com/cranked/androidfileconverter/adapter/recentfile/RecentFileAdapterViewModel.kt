package com.cranked.androidfileconverter.adapter.recentfile

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.data.database.dao.RecentFilesDao
import com.cranked.androidfileconverter.data.database.entity.RecentFile
import javax.inject.Inject

class RecentFileAdapterViewModel @Inject constructor(
    private val recentFilesDao: RecentFilesDao,
) :
    BaseViewModel() {
    val recentFileList = recentFilesDao.getAll()
    fun setAdapter(
        context: Context,
        recyclerView: RecyclerView,
        recentFileAdapter: RecentFileAdapter,
        list: List<RecentFile>,
    ): RecentFileAdapter {
        recentFileAdapter.apply {
            setItems(list)
        }
        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recentFileAdapter
        }
        return recentFileAdapter
    }
}