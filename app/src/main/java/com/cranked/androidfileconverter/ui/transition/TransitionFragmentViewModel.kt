package com.cranked.androidfileconverter.ui.transition

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
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
import com.cranked.androidfileconverter.dialog.CreateFolderWithSelectionDialog
import com.cranked.androidfileconverter.dialog.DeleteDialog
import com.cranked.androidfileconverter.dialog.RenameDialog
import com.cranked.androidfileconverter.dialog.createfolder.CreateFolderBottomDialog
import com.cranked.androidfileconverter.dialog.options.OptionsBottomDialog
import com.cranked.androidfileconverter.ui.model.OptionsModel
import com.cranked.androidfileconverter.ui.task.TaskActivity
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.enums.FileType
import com.cranked.androidfileconverter.utils.enums.FilterState
import com.cranked.androidfileconverter.utils.enums.LayoutState
import com.cranked.androidfileconverter.utils.enums.TaskType
import com.cranked.androidfileconverter.utils.file.FileUtility
import java.io.File
import java.text.SimpleDateFormat
import javax.inject.Inject

class TransitionFragmentViewModel @Inject constructor(
    private val favoritesDao: FavoritesDao,
    private val context: Context,
) :
    BaseViewModel() {
    val TAG = TransitionFragmentViewModel::class.java.name
    private val folderPath = MutableLiveData<String>()
    private val noDataState = MutableLiveData<Boolean>()
    private val filterState = MutableLiveData<Int>()
    private val itemsChangedState = MutableLiveData<Boolean>()
    private var selectedRowList = arrayListOf<TransitionModel>()
    private val longListenerActivated = MutableLiveData(false)
    private val selectedRowSize = MutableLiveData<Int>()
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
                            sendSelectedRowSize(selectedRowList.size)
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
                            sendSelectedRowSize(selectedRowList.size)
                        }
                    }
                    rowBinding.optionsImageView.setOnClickListener {
                        showOptionsBottomDialog(supportFragmentManager, arrayListOf(item))
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
                            sendSelectedRowSize(selectedRowList.size)
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
                            sendSelectedRowSize(selectedRowList.size)
                        }
                    }
                    rowBinding.optionsGridImageView.setOnClickListener {
                        showOptionsBottomDialog(supportFragmentManager, arrayListOf(item))
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
        transitionList: ArrayList<TransitionModel>,
    ) {
        try {
            val list = arrayListOf<OptionsModel>()
            val stringList = context.resources.getStringArray(R.array.optionsMenuStringArray).toList()
            val drawableList = context.resources.obtainTypedArray(R.array.imagesArray)
            val taskTypeList = TaskType.values().toList()
            when (transitionList.size) {

                0, 1 -> {
                    transitionList.forEach {
                        when (it.fileType) {
                            FileType.FOLDER.type -> {
                                taskTypeList.forEachIndexed { index, s ->
                                    if (s.value != TaskType.SHARETASK.value && s.value != TaskType.TOOLSTASK.value)
                                        list += OptionsModel(drawableList.getDrawable(index)!!,
                                            stringList.get(index).toString(),
                                            s.value)
                                }
                            }
                            else -> {
                                taskTypeList.forEachIndexed { index, s ->
                                    list += OptionsModel(drawableList.getDrawable(index)!!,
                                        stringList.get(index).toString(),
                                        s.value)
                                }
                            }
                        }
                    }
                }
                else -> {
                    if (transitionList.filter { it.fileType == FileType.FOLDER.type }.isNotEmpty()) {
                        taskTypeList.forEachIndexed { index, s ->
                            if (s.value != TaskType.TOOLSTASK.value && s.value != TaskType.SHARETASK.value &&
                                s.value != TaskType.RENAMETASK.value && s.value != TaskType.MARKFAVORITETASK.value
                            )
                                list += OptionsModel(drawableList.getDrawable(index)!!, stringList.get(index).toString(), s.value)
                        }
                    } else {
                        taskTypeList.forEachIndexed { index, s ->
                            if (s.value != TaskType.RENAMETASK.value && s.value != TaskType.MARKFAVORITETASK.value)
                                list += OptionsModel(drawableList.getDrawable(index)!!,
                                    stringList.get(index).toString(),
                                    s.value)
                        }
                    }
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
                                when (transitionList.size) {
                                    1 -> {
                                        val model = transitionList.get(0)
                                        if (model.isFavorite) {
                                            removeFavorite(model.filePath,
                                                model.fileName,
                                                model.fileType)
                                        } else {
                                            markFavorite(model.filePath,
                                                model.fileExtension,
                                                model.fileName,
                                                model.fileType)
                                        }
                                    }
                                }
                            }
                            TaskType.RENAMETASK.value -> {
                                when (transitionList.size) {
                                    1 -> {
                                        val model = transitionList.get(0)
                                        val dialog = RenameDialog(this@TransitionFragmentViewModel, model, favoritesDao)
                                        dialog.show(supportFragmentManager, "RenameTaskDialog")
                                    }
                                }
                            }
                            TaskType.DELETETASK.value -> {
                                showDeleteFialog(supportFragmentManager, transitionList)
                            }

                            TaskType.CREATEFOLDERWITHSELECTIONTASK.value -> {
                                val dialog = CreateFolderWithSelectionDialog(this@TransitionFragmentViewModel,
                                    transitionList,
                                    folderPath.value!!,
                                    favoritesDao)
                                dialog.show(supportFragmentManager, "CreateFolderWithSelection")
                            }
                            TaskType.DUPLICATE.value -> {
                                transitionList.forEach { model ->
                                    val targetFolderName =
                                        FileUtils.createfolder(model.filePath.substring(0, model.filePath.lastIndexOf("/")),
                                            model.fileName)
                                    FileUtility.duplicate(model.filePath + File.separator, targetFolderName)
                                }
                                selectedRowList.clear()
                                if (getLongListenerActivatedMutableLiveData().value!!)
                                    sendLongListenerActivated(false)
                            }
                            TaskType.MOVETASK.value -> {
                                optionsBottomDialog.dismiss()
                                sendLongListenerActivated(false)
                                val intent = Intent(context, TaskActivity::class.java)
                                intent.putExtra(Constants.FILE_TASK_TYPE, TaskType.MOVETASK.value)
                                intent.putParcelableArrayListExtra(Constants.SELECTED_LIST, transitionList)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                context.startActivity(intent)
                            }
                            TaskType.COPYTASK.value -> {
                                optionsBottomDialog.dismiss()
                                sendLongListenerActivated(false)
                                val intent = Intent(context, TaskActivity::class.java)
                                intent.putExtra(Constants.FILE_TASK_TYPE, TaskType.COPYTASK.value)
                                intent.putParcelableArrayListExtra(Constants.SELECTED_LIST, transitionList)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                context.startActivity(intent)
                            }
                        }
                        optionsBottomDialog.dismiss()
                        sendItemsChangedSate(true)
                        sendLongListenerActivated(false)
                    }
                }
            })

            adapter.setItems(list)
            val title =
                if (transitionList.size > 1) transitionList.size.toString() + "  " + context!!.getString(R.string.item) else transitionList.get(
                    0).fileName
            optionsBottomDialog = OptionsBottomDialog(adapter, title)
            optionsBottomDialog.show(supportFragmentManager, "OptionsBottomDialog")
        } catch (e: Exception) {
            LogManager.log(TAG, e.toString())
        }
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
        binding.multipleSelectionMenu.selectAllMultiple.setOnClickListener {
            val list: List<TransitionModel> = getFilesFromPath(path,
                app.getFilterState()).filter { !selectedRowList.contains(it) }
            selectedRowList.addAll(list)
            sendItemsChangedSate(true)
            sendSelectedRowSize(selectedRowList.size)
        }
        binding.multipleSelectionMenu.deleteMultiple.setOnClickListener {
            showDeleteFialog(supportFragmentManager,
                selectedRowList)
        }
        binding.multipleSelectionMenu.optionsMultiple.setOnClickListener {
            showOptionsBottomDialog(supportFragmentManager, selectedRowList)
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
                    if (getLongListenerActivatedMutableLiveData().value!!)
                        sendLongListenerActivated(false)
                }
        } catch (e: Exception) {
        }
    }

    fun closeLongClick() {
        selectedRowList.clear()
        if (getLongListenerActivatedMutableLiveData().value!!)
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

    fun sendSelectedRowSize(size: Int) {
        selectedRowSize.postValue(size)
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
        val deleteDialogFragment = DeleteDialog(this@TransitionFragmentViewModel, list, favoritesDao)
        deleteDialogFragment.show(supportFragmentManager, "DeleteDialogFragment")
    }

    fun setMenuVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun getItemsChangedStateMutableLiveData() = this.itemsChangedState
    fun getSelectedRowSizeMutableLiveData() = this.selectedRowSize
    fun getLongListenerActivatedMutableLiveData() = this.longListenerActivated
    fun getSelectedRowList() = this.selectedRowList
    fun getFilterStateMutableLiveData() = this.filterState
    fun getNoDataStateMutableLiveData() = this.noDataState
    fun getFolderPathMutableLiveData() = this.folderPath
}