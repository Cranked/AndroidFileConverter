package com.cranked.androidfileconverter.dialog.options

import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class MarkFavoriteTask(private val transitionList: List<TransitionModel>) : ITask() {
    override fun doTask(transitionFragmentViewModel: TransitionFragmentViewModel) {
        when (transitionList.size) {
            1 -> {
                val model = transitionList[0]
                if (model.isFavorite) {
                    transitionFragmentViewModel.removeFavorite(model.filePath,
                        model.fileName,
                        model.fileType)
                } else {
                    transitionFragmentViewModel.markFavorite(model.filePath,
                        model.fileExtension,
                        model.fileName,
                        model.fileType)
                }
            }
        }
    }
}