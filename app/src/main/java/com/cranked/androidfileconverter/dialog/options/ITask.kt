package com.cranked.androidfileconverter.dialog.options

import com.cranked.androidfileconverter.ui.camera.CameraFragmentViewModel
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel


abstract class ITask {
    open fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {}
    open fun doTask(homeFragmentViewModel: HomeFragmentViewModel) {}
    open fun doTask(cameraFragmentViewModel: CameraFragmentViewModel) {}
}