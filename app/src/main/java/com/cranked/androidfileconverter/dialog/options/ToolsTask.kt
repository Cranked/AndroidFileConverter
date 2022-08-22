package com.cranked.androidfileconverter.dialog.options

import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel

class ToolsTask:ITask() {
    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) = Unit
    override fun doTask(homeFragmentViewModel: HomeFragmentViewModel) = Unit
}