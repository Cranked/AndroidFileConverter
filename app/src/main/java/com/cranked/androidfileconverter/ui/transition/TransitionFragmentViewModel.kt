package com.cranked.androidfileconverter.ui.transition

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.transition.TransitionListAdapter
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.databinding.RowItemListTransitionBinding
import com.cranked.androidfileconverter.utils.Constants
import javax.inject.Inject

class TransitionFragmentViewModel @Inject constructor(private val favoritesDao: FavoritesDao) :
    BaseViewModel() {
    val folderPath = MutableLiveData<String>()
    val toastMessage = MutableLiveData<String>()
    val noDataState = MutableLiveData<Boolean>()
    fun setAdapter(
        context: Context,
        recylerView: RecyclerView,
        transitionListAdapter: TransitionListAdapter,
        list: MutableList<TransitionModel>,
    ) {
        transitionListAdapter.apply {
            setItems(list)
            setListener(object :
                BaseViewBindingRecyclerViewAdapter.ClickListener<TransitionModel, RowItemListTransitionBinding> {
                override fun onItemClick(
                    item: TransitionModel,
                    position: Int,
                    rowBinding: RowItemListTransitionBinding,
                ) {
                    rowBinding.transitionConstraint.setOnClickListener {
                        sendIntentToTransitionFragmentWithIntent(it, getItems()[position].filePath)
                    }
                    rowBinding.optionsImageView.setOnClickListener {
                        toastMessage("Options'a tıklandı...")
                    }
                }

            })
        }

        recylerView.apply {
            adapter = transitionListAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun sendIntentToTransitionFragmentWithIntent(view: View, path: String) {
        val bundle = Bundle()
        bundle.putString(Constants.DESTINATION_PATH_ACTION, path)
        view.findNavController()
            .navigate(R.id.action_transition_fragment_self, bundle)
    }

    fun getFilesFromPath(path: String): MutableList<TransitionModel> {
        return FileUtils.getFolderFiles(path, 1, 1)
            .filter { it.isDirectory or Constants.VALID_TYPES.contains(FileUtils.getExtension(it.name)) }
            .toTransitionList(favoritesDao).toMutableList()
    }

    fun sendPath(path: String) {
        folderPath.postValue(path)
    }

    fun sendNoDataState(state: Boolean) {
        noDataState.postValue(state)
    }

    fun toastMessage(string: String) {
        toastMessage.postValue(string)
    }
}