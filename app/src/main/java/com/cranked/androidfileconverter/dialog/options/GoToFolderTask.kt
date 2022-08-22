package com.cranked.androidfileconverter.dialog.options

import android.view.View
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import java.io.File

class GoToFolderTask(private val favoriteFile: FavoriteFile,private val view: View):ITask() {
    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {

    }

    override fun doTask(homeFragmentViewModel: HomeFragmentViewModel) {
        val path = favoriteFile.path.substring(0, favoriteFile.path.lastIndexOf("/")) + File.separator
        homeFragmentViewModel.goToTransitionFragmentWithIntent(view, path)
    }
}