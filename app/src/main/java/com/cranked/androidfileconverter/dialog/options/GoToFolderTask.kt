package com.cranked.androidfileconverter.dialog.options

import android.app.Dialog
import android.view.View
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.ui.camera.CameraFragmentViewModel
import com.cranked.androidfileconverter.ui.camera.CameraImageFragmentViewModel
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.model.ImagePreview
import com.cranked.androidfileconverter.ui.model.PhotoFile
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.ui.transition.toFavoriteModel
import java.io.File

class GoToFolderTask : ITask {
    lateinit var favoriteFile: FavoriteFile
    lateinit var transitionModel: TransitionModel
    lateinit var photoFile: PhotoFile
    lateinit var imagePreview: ImagePreview
    var view: View
    var dialog: Dialog? = null
    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {
        if (this.dialog != null && this.dialog!!.isShowing) this.dialog!!.dismiss()
        val tempFavoriteFile = transitionModel.toFavoriteModel()
        val path = tempFavoriteFile.path.substring(0, tempFavoriteFile.path.lastIndexOf("/")) + File.separator
        transitionFragmentViewModel.sendIntentToTransitionFragmentWithIntent(view, path)
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

    constructor(imagePreview: ImagePreview, view: View, dialog: Dialog) {
        this.imagePreview = imagePreview
        this.view = view
        this.dialog = dialog
    }

    constructor(transitionModel: TransitionModel, view: View, dialog: Dialog) {
        this.transitionModel = transitionModel
        this.view = view
        this.dialog = dialog
    }

    constructor(photoFile: PhotoFile, view: View, dialog: Dialog) {
        this.photoFile = photoFile
        this.view = view
        this.dialog = dialog
    }

    override fun doTask(homeFragmentViewModel: HomeFragmentViewModel) {
        if (this.dialog != null && this.dialog!!.isShowing) this.dialog!!.dismiss()
        val path = favoriteFile.path.substring(0, favoriteFile.path.lastIndexOf("/")) + File.separator
        homeFragmentViewModel.goToTransitionFragmentWithIntent(view, path)
    }

    override fun doTask(cameraFragmentViewModel: CameraFragmentViewModel) {
        if (this.dialog != null && this.dialog!!.isShowing) this.dialog!!.dismiss()
        val path = photoFile.path.substringBeforeLast("/")
        cameraFragmentViewModel.goToTransitionFragmentWithIntent(R.id.action_camera_dest_to_transition_fragment, view, path)
    }
    override fun doTask(cameraImageFragmentViewModel: CameraImageFragmentViewModel) {
        if (this.dialog != null && this.dialog!!.isShowing) this.dialog!!.dismiss()
        val path = imagePreview.path.substringBeforeLast("/")
        cameraImageFragmentViewModel.goToTransitionFragmentWithIntent(R.id.action_cameraImageFragment_to_transition_fragment, view, path)
    }
}