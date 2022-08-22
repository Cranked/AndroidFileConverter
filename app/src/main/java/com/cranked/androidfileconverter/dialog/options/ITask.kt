package com.cranked.androidfileconverter.dialog.options

import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel


abstract class ITask {
    abstract fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel)
    abstract fun doTask(homeFragmentViewModel: HomeFragmentViewModel)

}