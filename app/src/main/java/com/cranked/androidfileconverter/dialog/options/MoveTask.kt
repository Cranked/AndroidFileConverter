package com.cranked.androidfileconverter.dialog.options

import android.content.Context
import android.content.Intent
import com.cranked.androidfileconverter.ui.task.TaskActivity
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.enums.TaskType

class MoveTask(
    private val context: Context,
    private val viewModel: TransitionFragmentViewModel,
    private val optionsBottomDialog: OptionsBottomDialog,
    private val transitionList: ArrayList<TransitionModel>,
) : ITask() {

    override fun doTask() {
        optionsBottomDialog!!.dismiss()
        viewModel.sendLongListenerActivated(false)
        val intent = Intent(context, TaskActivity::class.java)
        intent.putExtra(Constants.FILE_TASK_TYPE, TaskType.MOVETASK.value)
        intent.putParcelableArrayListExtra(Constants.SELECTED_LIST, transitionList)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }
}