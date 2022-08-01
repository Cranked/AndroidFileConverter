package com.cranked.androidfileconverter.dialog.options

import android.content.Context
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.ui.home.HomeFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionFragmentViewModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel

class ShareTask(
) : ITask() {
    lateinit var context: Context
    var transitionFragmentViewModel: TransitionFragmentViewModel? = null
    var homeFragmentViewModel: HomeFragmentViewModel? = null
    lateinit var transitionList: ArrayList<TransitionModel>
    lateinit var selectedRowList: ArrayList<TransitionModel>
    lateinit var favoriteList: ArrayList<FavoriteFile>

    constructor(context: Context, viewModel: HomeFragmentViewModel, favoriteList: ArrayList<FavoriteFile>) : this() {
        this.homeFragmentViewModel = viewModel
        this.context = context
        this.favoriteList = favoriteList
    }

    constructor(
        context: Context,
        viewModel: TransitionFragmentViewModel,
        transitionList: ArrayList<TransitionModel>,
        selectedRowList: ArrayList<TransitionModel>,
    ) : this() {
        this.context = context
        this.transitionFragmentViewModel = viewModel
        this.transitionList = transitionList
        this.selectedRowList = selectedRowList
    }

    override fun doTask() {
        if (this.transitionFragmentViewModel != null) {
            this.transitionFragmentViewModel!!.shareItemsList(context, transitionList)
            selectedRowList.clear()
        }
        if (this.homeFragmentViewModel != null) {
            this.homeFragmentViewModel!!.shareItemsList(context, favoriteList)
        }
    }


}