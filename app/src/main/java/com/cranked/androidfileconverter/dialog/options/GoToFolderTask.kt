package com.cranked.androidfileconverter.dialog.options

import android.view.View
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import java.io.File

class GoToFolderTask(private val viewModel: HomeFragmentViewModel,private val favoriteFile: FavoriteFile,private val view: View):ITask() {
    override fun doTask() {
        val path = favoriteFile.path.substring(0, favoriteFile.path.lastIndexOf("/")) + File.separator
        viewModel.goToTransitionFragmentWithIntent(view, path)
    }
}