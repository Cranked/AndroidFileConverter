package com.cranked.androidfileconverter.dialog.options

import androidx.fragment.app.FragmentManager
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class DeleteTask(private val viewModel: TransitionFragmentViewModel,private val supportFragmentManager: FragmentManager,private val transitionList:ArrayList<TransitionModel>):ITask(
    ) {
    override fun doTask() {
        viewModel.showDeleteFialog(supportFragmentManager, transitionList)
    }
}