package com.cranked.androidfileconverter.dialog.options

import android.content.Context
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.ui.model.PageModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.utils.file.FileUtility


class ToolsTask(private val context: Context, private val selectedList: ArrayList<String>, private val pageModel: PageModel) : ITask() {
    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {
        selectedList.forEach {
            if (!FileUtility.imageToPdf(context,pageModel, it)) {
                transitionFragmentViewModel.sendErrorMessage(context.getString(R.string.something_went_wrong))
                return@forEach
            }
        }
    }
}