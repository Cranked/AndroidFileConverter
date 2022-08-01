package com.cranked.androidfileconverter.dialog.options

import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class MarkFavoriteTask(private val viewModel: TransitionFragmentViewModel,private val transitionList:List<TransitionModel>) : ITask() {
    override fun doTask() {
        when (transitionList.size) {
            1 -> {
                val model = transitionList.get(0)
                if (model.isFavorite) {
                    viewModel.removeFavorite(model.filePath,
                        model.fileName,
                        model.fileType)
                } else {
                    viewModel.markFavorite(model.filePath,
                        model.fileExtension,
                        model.fileName,
                        model.fileType)
                }
            }
        }

    }

}