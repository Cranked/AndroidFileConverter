package com.cranked.androidfileconverter.ui.transition

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.transition.TransitionGridAdapter
import com.cranked.androidfileconverter.adapter.transition.TransitionListAdapter
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.databinding.RowTransitionGridItemBinding
import com.cranked.androidfileconverter.databinding.RowTransitionListItemBinding
import com.cranked.androidfileconverter.dialog.createfolder.CreateFolderBottomDialog
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.enums.FilterState
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

class TransitionFragmentViewModel @Inject constructor(
    private val favoritesDao: FavoritesDao,
) :
    BaseViewModel() {

    val folderPath = MutableLiveData<String>()
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
                BaseViewBindingRecyclerViewAdapter.ClickListener<TransitionModel, RowTransitionListItemBinding> {
                override fun onItemClick(
                    item: TransitionModel,
                    position: Int,
                    rowBinding: RowTransitionListItemBinding,
                ) {
                    rowBinding.transitionLinearLayout.setOnClickListener {
                        sendIntentToTransitionFragmentWithIntent(it, getItems()[position].filePath)
                    }
                    rowBinding.optionsImageView.setOnClickListener {
                        Toast.makeText(rowBinding.root.context,
                            "OptionsaTıklandı",
                            Toast.LENGTH_SHORT).show()
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

    fun setAdapter(
        context: Context,
        recylerView: RecyclerView,
        transitionGridAdapter: TransitionGridAdapter,
        list: MutableList<TransitionModel>,
    ) {
        transitionGridAdapter.apply {
            setItems(list)
            setListener(object :
                BaseViewBindingRecyclerViewAdapter.ClickListener<TransitionModel, RowTransitionGridItemBinding> {
                override fun onItemClick(
                    item: TransitionModel,
                    position: Int,
                    rowBinding: RowTransitionGridItemBinding,
                ) {
                    rowBinding.transitionGridLinearLayout.setOnClickListener {
                        sendIntentToTransitionFragmentWithIntent(it, getItems()[position].filePath)
                    }
                    rowBinding.optionsGridImageView.setOnClickListener {
                        Toast.makeText(rowBinding.root.context,
                            "OptionsaTıklandı",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        recylerView.apply {
            adapter = transitionGridAdapter
            layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

    fun showCreateFolderBottomDialog(supportFragmentManager: FragmentManager, path: String) {
        val bottomDialog = CreateFolderBottomDialog(this, path)
        bottomDialog.show(supportFragmentManager, "CreateFolderBottomDialog")
    }

    fun sendIntentToTransitionFragmentWithIntent(view: View, path: String) {
        val bundle = Bundle()
        bundle.putString(Constants.DESTINATION_PATH_ACTION, path)
        view.findNavController()
            .navigate(R.id.action_transition_fragment_self, bundle)
    }

    fun backStack(view: View) {
        view.findNavController().navigateUp()
    }

    fun getFilesFromPath(path: String, state: Int): MutableList<TransitionModel> {
        val list = FileUtils.getFolderFiles(path, 1, 1)
            .filter { it.isDirectory or Constants.VALID_TYPES.contains(FileUtils.getExtension(it.name)) }
            .toTransitionList(favoritesDao)
        when (state) {
            FilterState.ORDERBYNAME_A_TO_Z.value -> {
                return list.sortedBy { it.fileName }.toMutableList()
            }
            FilterState.ORDERBYNAME_Z_TO_A.value -> {
                return list.sortedBy { it.fileName }.reversed().toMutableList()
            }
            FilterState.ORDERBY_LAST_MODIFIED_NEWEST.value -> {
                return list.sortedBy { it.lastModified }.toMutableList()
            }
            FilterState.ORDERBY_LAST_MODIFIED_OLDEST.value -> {
                return list.sortedBy { it.lastModified }.reversed().toMutableList()
            }
            else -> {
                return list.toMutableList()
            }
        }
    }

    fun sendPath(path: String) {
        folderPath.postValue(path)
    }

    fun sendNoDataState(state: Boolean) {
        noDataState.postValue(state)
    }

    fun showDialog(context: Context, path: String) {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        val view =
            (layoutInflater as LayoutInflater).inflate(R.layout.create_folder_dialog_layout, null)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val okButton = view.findViewById<Button>(R.id.okButton)
        val dialog = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(true)
            .create()
        dialog.show()
        cancelButton.setOnClickListener { dialog.dismiss() }
        okButton.setOnClickListener {
            val folderName =
                view.findViewById<TextInputEditText>(R.id.folderNameEditText).text!!.trim()
                    .toString()
            FileUtils.createfolder(path, folderName)
            sendPath(path)
            dialog.dismiss()
        }
    }
}