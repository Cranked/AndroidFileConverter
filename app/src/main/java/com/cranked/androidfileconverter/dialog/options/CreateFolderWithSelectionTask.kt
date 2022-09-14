package com.cranked.androidfileconverter.dialog.options

import androidx.fragment.app.FragmentManager
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.dialog.CreateFolderWithSelectionDialog
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class CreateFolderWithSelectionTask(
    private val supportFragmentManager: FragmentManager,
    private val transitionList: ArrayList<TransitionModel>,
    private val path: String,
    private val favoritesDao: FavoritesDao,
) : ITask() {
    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {
        val dialog = CreateFolderWithSelectionDialog(transitionFragmentViewModel,
            transitionList,
            path,
            favoritesDao)
        dialog.show(supportFragmentManager, "CreateFolderWithSelection")
    }
}