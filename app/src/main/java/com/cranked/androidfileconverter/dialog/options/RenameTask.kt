package com.cranked.androidfileconverter.dialog.options

import androidx.fragment.app.FragmentManager
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.dialog.RenameDialog
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class RenameTask(private val supportFragmentManager: FragmentManager,private val transitionList: List<TransitionModel>,private val favoritesDao: FavoritesDao):ITask() {
    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {
        when (transitionList.size) {
            1 -> {
                val model = transitionList[0]
                val dialog = RenameDialog(transitionFragmentViewModel, model, favoritesDao)
                dialog.show(supportFragmentManager, "RenameTaskDialog")
            }
        }
    }
}