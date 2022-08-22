package com.cranked.androidfileconverter.dialog.options

import android.app.Dialog
import android.view.View
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import java.io.File

class GoToFolderTask : ITask {
    lateinit var favoriteFile: FavoriteFile
    lateinit var view: View
    var dialog: Dialog? = null
    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {

    }

    constructor(favoriteFile: FavoriteFile, view: View, dialog: Dialog) {
        this.favoriteFile = favoriteFile
        this.view = view
        this.dialog = dialog
    }

    constructor(favoriteFile: FavoriteFile, view: View) {
        this.favoriteFile = favoriteFile
        this.view = view
    }


    override fun doTask(homeFragmentViewModel: HomeFragmentViewModel) {
        if (this.dialog != null && this.dialog!!.isShowing) this.dialog!!.dismiss()
        val path = favoriteFile.path.substring(0, favoriteFile.path.lastIndexOf("/")) + File.separator
        homeFragmentViewModel.goToTransitionFragmentWithIntent(view, path)
    }
}