package com.cranked.androidfileconverter.ui.transition

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.dialog.BaseDialog
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.options.OptionsAdapter
import com.cranked.androidfileconverter.adapter.transition.TransitionGridAdapter
import com.cranked.androidfileconverter.adapter.transition.TransitionListAdapter
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.databinding.FragmentTransitionBinding
import com.cranked.androidfileconverter.databinding.RowOptionsItemBinding
import com.cranked.androidfileconverter.databinding.RowTransitionGridItemBinding
import com.cranked.androidfileconverter.databinding.RowTransitionListItemBinding
import com.cranked.androidfileconverter.dialog.DeleteDialog
import com.cranked.androidfileconverter.dialog.createfolder.CreateFolderBottomDialog
import com.cranked.androidfileconverter.dialog.options.OptionsBottomDialog
import com.cranked.androidfileconverter.ui.model.OptionsModel
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.enums.FilterState
import com.cranked.androidfileconverter.utils.enums.LayoutState
import com.cranked.androidfileconverter.utils.enums.TaskType
import com.cranked.androidfileconverter.utils.file.FileUtility
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.text.SimpleDateFormat
import javax.inject.Inject

class TransitionFragmentViewModel @Inject constructor(
    private val favoritesDao: FavoritesDao,
    private val context: Context,
) :
    BaseViewModel() {
    val TAG = TransitionFragmentViewModel::class.java.name
    val folderPath = MutableLiveData<String>()
    val noDataState = MutableLiveData<Boolean>()
    val filterState = MutableLiveData<Int>()
    private val itemsChangedState = MutableLiveData<Boolean>()
    var selectedRowList = arrayListOf<TransitionModel>()
    val longListenerActivated = MutableLiveData<Boolean>(false)
    val selectedRowSize = MutableLiveData<Int>()
    lateinit var supportFragmentManager: FragmentManager
    lateinit var optionsBottomDialog: OptionsBottomDialog
    fun setAdapter(
        context: Context,
        recylerView: RecyclerView,
        transitionListAdapter: TransitionListAdapter,
        list: MutableList<TransitionModel>,
    ): TransitionListAdapter {
        transitionListAdapter.apply {
            setItems(list)
            setListener(object :
                BaseViewBindingRecyclerViewAdapter.ClickListener<TransitionModel, RowTransitionListItemBinding> {
                override fun onItemClick(
                    item: TransitionModel,
                    position: Int,
                    rowBinding: RowTransitionListItemBinding,
                ) {
                    rowBinding.transitionLinearLayout.setOnLongClickListener {
                        if (!longListenerActivated.value!!) {
                            selectedRowList.clear()
                            selectedRowList.add(item)
                            selectedRowSize.postValue(selectedRowList.size)
                            sendLongListenerActivated(true)
                        }
                        return@setOnLongClickListener true
                    }
                    rowBinding.transitionLinearLayout.setOnClickListener {
                        if (!longListenerActivated.value!!) {
                            sendIntentToTransitionFragmentWithIntent(it,
                                item.filePath)
                        } else {
                            if (!selectedRowList.contains(item)) {
                                selectedRowList.add(item)
                                rowBinding.transitionLinearLayout.background =
                                    context.getDrawable(R.drawable.custom_adapter_selected_background)
                            } else {
                                selectedRowList.remove(item)
                                rowBinding.transitionLinearLayout.background =
                                    context.getDrawable(R.drawable.custom_adapter_unselected_background)
                            }
                            selectedRowSize.postValue(selectedRowList.size)
                            if (selectedRowList.isEmpty()) {
                                sendLongListenerActivated(false)
                            }
                        }
                    }
                    rowBinding.optionsImageView.setOnClickListener {
                        showOptionsBottomDialog(supportFragmentManager, item)
                    }
                }
            })
        }

        recylerView.apply {
            adapter = transitionListAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        return transitionListAdapter
    }

    fun setAdapter(
        context: Context,
        recylerView: RecyclerView,
        transitionGridAdapter: TransitionGridAdapter,
        list: MutableList<TransitionModel>,
    ): TransitionGridAdapter {
        transitionGridAdapter.apply {
            setItems(list)
            setListener(object :
                BaseViewBindingRecyclerViewAdapter.ClickListener<TransitionModel, RowTransitionGridItemBinding> {
                override fun onItemClick(
                    item: TransitionModel,
                    position: Int,
                    rowBinding: RowTransitionGridItemBinding,
                ) {
                    rowBinding.transitionGridLinearLayout.setOnLongClickListener {
                        if (!longListenerActivated.value!!) {
                            sendLongListenerActivated(true)
                            selectedRowList.clear()
                            selectedRowList.add(item)
                            selectedRowSize.postValue(selectedRowList.size)
                        }
                        return@setOnLongClickListener true
                    }
                    rowBinding.transitionGridLinearLayout.setOnClickListener {
                        if (!longListenerActivated.value!!) {
                            sendIntentToTransitionFragmentWithIntent(it,
                                item.filePath)
                        } else {
                            if (!selectedRowList.contains(item)) {
                                selectedRowList.add(item)
                                rowBinding.transitionGridLinearLayout.background =
                                    context.getDrawable(R.drawable.custom_adapter_selected_background)
                            } else {
                                selectedRowList.remove(item)
                                rowBinding.transitionGridLinearLayout.background =
                                    context.getDrawable(R.drawable.custom_adapter_unselected_background)
                            }
                            selectedRowSize.postValue(selectedRowList.size)
                            if (selectedRowList.isEmpty()) {
                                longListenerActivated.value = false
                            }
                        }
                    }
                    rowBinding.optionsGridImageView.setOnClickListener {
                        showOptionsBottomDialog(supportFragmentManager, item)
                    }
                }
            })
        }
        recylerView.apply {
            adapter = transitionGridAdapter
            layoutManager =
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
        return transitionGridAdapter
    }

    fun showOptionsBottomDialog(
        supportFragmentManager: FragmentManager,
        transitionModel: TransitionModel,
    ) {
        val list = mutableListOf<OptionsModel>()
        when (transitionModel.fileType) {
            1 -> {
                list += OptionsModel(ContextCompat.getDrawable(context,
                    R.drawable.icon_selection)!!,
                    context.getString(R.string.create_folder_with_selections),
                    TaskType.CREATEFOLDERWITHSELECTIONTASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_favorite)!!,
                    if (!transitionModel.isFavorite) context.getString(R.string.mark_as_favorite) else context.getString(
                        R.string.remove_favorite), TaskType.MARKFAVORITETASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_rename)!!,
                    context.getString(R.string.rename), TaskType.RENAMETASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_delete)!!,
                    context.getString(R.string.delete), TaskType.DELETETASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_move)!!,
                    context.getString(R.string.move), TaskType.MOVETASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_copy)!!,
                    context.getString(R.string.copy), TaskType.COPYTASK.value)
            }
            else -> {
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_tools)!!,
                    context.getString(R.string.tools), TaskType.TOOLSTASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_share)!!,
                    context.getString(R.string.share), TaskType.SHARETASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context,
                    R.drawable.icon_selection)!!,
                    context.getString(R.string.create_folder_with_selections),
                    TaskType.CREATEFOLDERWITHSELECTIONTASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_favorite)!!,
                    if (!transitionModel.isFavorite) context.getString(R.string.mark_as_favorite) else context.getString(
                        R.string.remove_favorite), TaskType.MARKFAVORITETASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_rename)!!,
                    context.getString(R.string.rename), TaskType.RENAMETASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_delete)!!,
                    context.getString(R.string.delete), TaskType.DELETETASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_move)!!,
                    context.getString(R.string.move), TaskType.MOVETASK.value)
                list += OptionsModel(ContextCompat.getDrawable(context, R.drawable.icon_copy)!!,
                    context.getString(R.string.copy), TaskType.COPYTASK.value)
            }
        }
        val adapter = OptionsAdapter()
        adapter.setListener(object :
            BaseViewBindingRecyclerViewAdapter.ClickListener<OptionsModel, RowOptionsItemBinding> {
            override fun onItemClick(
                item: OptionsModel,
                position: Int,
                rowBinding: RowOptionsItemBinding,
            ) {
                rowBinding.optionsBottomLinearLayout.setOnClickListener {
                    when (item.tasktype) {
                        TaskType.MARKFAVORITETASK.value -> {
                            if (transitionModel.isFavorite) {
                                removeFavorite(transitionModel.filePath,
                                    transitionModel.fileName,
                                    transitionModel.fileType)
                            } else {
                                markFavorite(transitionModel.filePath,
                                    transitionModel.fileExtension,
                                    transitionModel.fileName,
                                    transitionModel.fileType)
                            }
                        }
                        TaskType.DELETETASK.value -> {
                            val dialog = DeleteDialog(this@TransitionFragmentViewModel,
                                arrayListOf(transitionModel),
                                favoritesDao)
                            dialog.show(supportFragmentManager, "DeleteTaskDialog")
                        }
                        TaskType.RENAMETASK.value -> {
                            val dialog = BaseDialog(it.context, R.layout.rename_file_layout)
                            val fileNameEditText =
                                dialog.view.findViewById<TextInputEditText>(R.id.renameEditText)
                            fileNameEditText.setText(transitionModel.fileName)
                            dialog.view.background = context.getDrawable(R.drawable.custom_dialog)
                            dialog.view.findViewById<TextView>(R.id.cancelButton)
                                .setOnClickListener {
                                    dialog.getDialog().dismiss()
                                }
                            dialog.view.findViewById<TextView>(R.id.okButton).setOnClickListener {
                                val realPath = transitionModel.filePath.substring(0,
                                    transitionModel.filePath.lastIndexOf(transitionModel.fileName))
                                val editTextString = fileNameEditText.text.toString()
                                if (editTextString.equals(transitionModel.fileName)) {
                                    dialog.getDialog().dismiss()
                                    return@setOnClickListener
                                }
                                if (editTextString.isEmpty()) {
                                    showToast(context.getString(R.string.file_name_required))
                                    return@setOnClickListener
                                }
                                val existNewFileName =
                                    File(realPath + fileNameEditText.text.toString()).exists() and !transitionModel.fileName.equals(
                                        editTextString)
                                if (existNewFileName) {
                                    showToast(context.getString(R.string.file_name_exist))
                                    return@setOnClickListener
                                }
                                if (fileNameEditText.text!!.isNotEmpty()) {
                                    val newPath = realPath + fileNameEditText.text.toString()
                                    val favoriteFile =
                                        favoritesDao.getFavorite(transitionModel.filePath,
                                            transitionModel.fileName,
                                            transitionModel.fileType)
                                    if (favoriteFile != null) {
                                        favoriteFile.fileName = fileNameEditText.text.toString()
                                        favoriteFile.path = newPath
                                        favoritesDao.update(favoriteFile)
                                    }
                                    val result = FileUtility.renameFile(transitionModel.filePath,
                                        newPath)
                                    dialog.getDialog().dismiss()
                                    sendItemsChangedSate(true)
                                } else {
                                    showToast(context.getString(R.string.file_name_required))
                                }
                            }
                            dialog.getDialog().show()
                        }
                    }
                    sendItemsChangedSate(true)
                    optionsBottomDialog.dismiss()
                }
            }
        })
        adapter.setItems(list)
        optionsBottomDialog = OptionsBottomDialog(adapter, transitionModel.fileName)
        optionsBottomDialog.show(supportFragmentManager, "OptionsBottomDialog")
    }

    fun showCreateFolderBottomDialog(supportFragmentManager: FragmentManager, path: String) {
        val bottomDialog = CreateFolderBottomDialog(this, path)
        bottomDialog.show(supportFragmentManager, "CreateFolderBottomDialog")
    }

    fun removeFavorite(filePath: String, fileName: String, fileType: Int) {
        val favoriteFile = favoritesDao.getFavorite(filePath, fileName, fileType)
        if (favoriteFile != null) {
            favoritesDao.delete(favoriteFile)
        }
    }

    fun markFavorite(filePath: String, fileExtension: String, fileName: String, fileType: Int) {
        favoritesDao.insert(FavoriteFile(fileName, fileExtension, fileType, filePath))
    }

    fun sendIntentToTransitionFragmentWithIntent(view: View, path: String) {
        val bundle = Bundle()
        bundle.putString(Constants.DESTINATION_PATH_ACTION, path)
        view.findNavController()
            .navigate(R.id.action_transition_fragment_self, bundle)
    }

    fun init(
        binding: FragmentTransitionBinding,
        transitionFragment: TransitionFragment,
        activity: FragmentActivity,
        app: FileConvertApp,
        path: String,
        spinnerList: List<String>,
    ) {
        supportFragmentManager = activity.supportFragmentManager
        binding.transitionToolbarMenu.backImageView.setOnClickListener { backStack(it) }
        binding.multipleSelectionMenu.backButtonMultiple.setOnClickListener { closeLongClick() }
        binding.createFolderButton.setOnClickListener {
            showCreateFolderBottomDialog(supportFragmentManager,
                folderPath.value.toString())
        }
        binding.multipleSelectionMenu.deleteMultiple.setOnClickListener {
            showDeleteFialog(supportFragmentManager,
                selectedRowList)
        }

        setMenuVisibility(binding.transitionToolbarMenu.root,
            !longListenerActivated.value!!)
        setMenuVisibility(binding.multipleSelectionMenu.root, longListenerActivated.value!!)

        val title = path.split("/").last { it.isNotEmpty() }
        binding.transitionToolbarMenu.titleToolBar.text = title
        when (app.getLayoutState()) {
            LayoutState.LIST_LAYOUT.value -> binding.transitionToolbarMenu.layoutImageView.setImageDrawable(
                context.getDrawable(
                    R.drawable.icon_grid))
            LayoutState.GRID_LAYOUT.value -> binding.transitionToolbarMenu.layoutImageView.setImageDrawable(
                context.getDrawable(
                    R.drawable.icon_list))
        }
        binding.transitionToolbarMenu.layoutImageView.setOnClickListener {
            when (app.getLayoutState()) {
                LayoutState.LIST_LAYOUT.value -> {
                    app.setLayoutState(LayoutState.GRID_LAYOUT.value)
                    binding.transitionToolbarMenu.layoutImageView.setImageDrawable(context.getDrawable(
                        R.drawable.icon_list))
                    setAdapter(context,
                        binding.transitionRecylerView,
                        transitionFragment.transitionGridAdapter,
                        getFilesFromPath(path, app.getFilterState()))
                }
                LayoutState.GRID_LAYOUT.value -> {
                    app.setLayoutState(LayoutState.LIST_LAYOUT.value)
                    binding.transitionToolbarMenu.layoutImageView.setImageDrawable(context.getDrawable(
                        R.drawable.icon_grid))
                    setAdapter(context,
                        binding.transitionRecylerView,
                        transitionFragment.transitionListAdapter,
                        getFilesFromPath(path, app.getFilterState()))
                }
            }
        }
        val arrayAdapter =
            ArrayAdapter(this.context,
                R.layout.row_spinner_item_child,
                spinnerList)
        arrayAdapter.setDropDownViewResource(R.layout.row_spinner_item)
        binding.transitionToolbarMenu.sortSpinner.adapter = arrayAdapter
        when (app.getFilterState()) {
            FilterState.ORDERBYNAME_A_TO_Z.value -> {
                binding.transitionToolbarMenu.sortSpinner.setSelection(0)
            }
            FilterState.ORDERBYNAME_Z_TO_A.value -> {
                binding.transitionToolbarMenu.sortSpinner.setSelection(1)
            }
            FilterState.ORDERBY_LAST_MODIFIED_NEWEST.value -> {
                binding.transitionToolbarMenu.sortSpinner.setSelection(2)
            }
            FilterState.ORDERBY_LAST_MODIFIED_OLDEST.value -> {
                binding.transitionToolbarMenu.sortSpinner.setSelection(3)
            }
        }
        binding.transitionToolbarMenu.sortSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    try {
                        app.setFilterState(position + 1)
                        sendFilterState(position + 1)
                    } catch (e: Exception) {
                        println(e.toString())
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
    }

    fun backStack(view: View) {
        try {
            if (view.isEnabled)
                if (!longListenerActivated.value!!) {
                    view.findNavController().navigateUp()
                } else {
                    sendLongListenerActivated(false)
                }
        } catch (e: Exception) {
        }
    }

    fun closeLongClick() {
        selectedRowList.clear()
        sendLongListenerActivated(false)
    }

    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun getFilesFromPath(path: String, state: Int): MutableList<TransitionModel> {
        val list = FileUtils.getFolderFiles(path, 1, 1)
            .filter { it.isDirectory or Constants.VALID_TYPES.contains(it.extension) }
            .toTransitionList(favoritesDao)
        when (state) {
            FilterState.ORDERBYNAME_A_TO_Z.value -> {
                return list.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.fileName }))
                    .toMutableList()
            }
            FilterState.ORDERBYNAME_Z_TO_A.value -> {
                return list.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.fileName }))
                    .reversed().toMutableList()
            }
            FilterState.ORDERBY_LAST_MODIFIED_NEWEST.value -> {
                return list.sortedBy { SimpleDateFormat(Constants.dateFormat).parse(it.lastModified)!!.time }
                    .reversed()
                    .toMutableList()
            }
            FilterState.ORDERBY_LAST_MODIFIED_OLDEST.value -> {
                return list.sortedBy { SimpleDateFormat(Constants.dateFormat).parse(it.lastModified)!!.time }
                    .toMutableList()
            }
            else -> {
                return list.toMutableList()
            }
        }
    }

    fun sendPath(path: String) {
        folderPath.postValue(path)
    }

    fun sendFilterState(value: Int) {
        filterState.postValue(value)
    }

    fun sendNoDataState(state: Boolean) {
        noDataState.postValue(state)
    }

    fun sendItemsChangedSate(value: Boolean) {
        itemsChangedState.postValue(value)
    }

    fun sendLongListenerActivated(state: Boolean) {
        longListenerActivated.postValue(state)
    }

    fun showDeleteFialog(
        supportFragmentManager: FragmentManager,
        list: ArrayList<TransitionModel>,
    ) {
        val deleteDialogFragment = DeleteDialog(this, list, favoritesDao)
        deleteDialogFragment.show(supportFragmentManager, "DeleteDialogFragment")
    }

    fun setMenuVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun showDialog(context: Context, path: String) {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        val view =
            (layoutInflater as LayoutInflater).inflate(R.layout.create_folder_dialog_layout, null)
        val cancelButton = view.findViewById<TextView>(R.id.cancelButton)
        val okButton = view.findViewById<TextView>(R.id.okButton)
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

    fun getItemsChangedStateMutableLiveData() = this.itemsChangedState
}