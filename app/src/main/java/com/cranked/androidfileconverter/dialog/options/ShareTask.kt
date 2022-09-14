package com.cranked.androidfileconverter.dialog.options

import android.content.Context
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.ui.camera.CameraImageFragmentViewModel
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.model.ImagePreview
import com.cranked.androidfileconverter.ui.model.PhotoFile
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class ShareTask(
) : ITask() {
    lateinit var context: Context
    lateinit var transitionList: ArrayList<TransitionModel>
    lateinit var selectedRowList: ArrayList<TransitionModel>
    lateinit var favoriteList: ArrayList<FavoriteFile>
    lateinit var photoFile: PhotoFile
    lateinit var imagePreview: ImagePreview

    constructor(context: Context, favoriteList: ArrayList<FavoriteFile>) : this() {
        this.context = context
        this.favoriteList = favoriteList
    }

    constructor(
        context: Context,
        transitionList: ArrayList<TransitionModel>,
        selectedRowList: ArrayList<TransitionModel>,
    ) : this() {
        this.context = context
        this.transitionList = transitionList
        this.selectedRowList = selectedRowList
    }

    constructor(context: Context, photoFile: PhotoFile) : this() {
        this.context = context
        this.photoFile = photoFile
    }

    constructor(context: Context, imagePreview: ImagePreview) : this() {
        this.context = context
        this.imagePreview = imagePreview
    }

    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {
        transitionFragmentViewModel.shareItemsList(context, transitionList)
        selectedRowList.clear()
    }

    override fun doTask(homeFragmentViewModel: HomeFragmentViewModel) {
        homeFragmentViewModel.shareItemsList(context, favoriteList)
    }

    override fun doTask(cameraImageFragmentViewModel: CameraImageFragmentViewModel) {
        cameraImageFragmentViewModel.shareItemsList(context, arrayListOf(imagePreview))
    }
}