package com.cranked.androidfileconverter.dialog.options

import androidx.fragment.app.FragmentManager
import com.cranked.androidfileconverter.ui.camera.CameraFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
 class DeleteTask(private val supportFragmentManager: FragmentManager,private val transitionList: ArrayList<TransitionModel>) : ITask() {

    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {
        transitionFragmentViewModel.showDeleteFialog(supportFragmentManager, transitionList)
    }

    override fun doTask(cameraFragmentViewModel: CameraFragmentViewModel) {
        cameraFragmentViewModel.showDeleteFialog(supportFragmentManager,transitionList)

    }
}