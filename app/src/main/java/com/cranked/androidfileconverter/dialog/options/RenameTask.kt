package com.cranked.androidfileconverter.dialog.options

import androidx.fragment.app.FragmentManager
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.dialog.RenameDialog
import com.cranked.androidfileconverter.ui.camera.CameraFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class RenameTask : ITask {
    var transitionList: List<TransitionModel>
    var supportFragmentManager: FragmentManager
    var favoritesDao: FavoritesDao

    constructor(supportFragmentManager: FragmentManager, transitionList: List<TransitionModel>, favoritesDao: FavoritesDao) {
        this.supportFragmentManager = supportFragmentManager
        this.transitionList = transitionList
        this.favoritesDao = favoritesDao
    }

    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {
        when (transitionList.size) {
            1 -> {
                val model = transitionList[0]
                val dialog = RenameDialog(transitionFragmentViewModel, model, favoritesDao)
                dialog.show(supportFragmentManager, "RenameTaskDialog")
            }
        }
    }

    override fun doTask(cameraFragmentViewModel: CameraFragmentViewModel) {
        when (transitionList.size) {
            1 -> {
                val model = transitionList[0]
                val dialog = RenameDialog(cameraFragmentViewModel, model, favoritesDao)
                dialog.show(supportFragmentManager, "RenameTaskDialog")
            }
        }
    }
}