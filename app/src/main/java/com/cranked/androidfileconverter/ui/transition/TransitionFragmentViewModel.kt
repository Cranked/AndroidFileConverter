package com.cranked.androidfileconverter.ui.transition

import android.animation.Animator
import android.animation.AnimatorSet
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.BitmapFactory
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.get
import androidx.core.view.isVisible
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
import com.cranked.androidfileconverter.BuildConfig
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.options.OptionsAdapter
import com.cranked.androidfileconverter.adapter.transition.TransitionGridAdapter
import com.cranked.androidfileconverter.adapter.transition.TransitionListAdapter
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.databinding.*
import com.cranked.androidfileconverter.dialog.DeleteDialog
import com.cranked.androidfileconverter.dialog.createfolder.CreateFolderBottomDialog
import com.cranked.androidfileconverter.dialog.favorite.FavoriteOptionsBottomDialog
import com.cranked.androidfileconverter.dialog.options.*
import com.cranked.androidfileconverter.ui.model.OptionsModel
import com.cranked.androidfileconverter.utils.*
import com.cranked.androidfileconverter.utils.animation.animationStart
import com.cranked.androidfileconverter.utils.enums.FileType
import com.cranked.androidfileconverter.utils.enums.FilterState
import com.cranked.androidfileconverter.utils.enums.LayoutState
import com.cranked.androidfileconverter.utils.enums.TaskType
import com.cranked.androidfileconverter.utils.image.BitmapUtils
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
    lateinit var dialog: Dialog
    private lateinit var favoriteOptionsBottomDialog: FavoriteOptionsBottomDialog

    fun setAdapter(
        context: Context,
        activity: FragmentActivity,
        layoutInflater: LayoutInflater,
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
                            val bindingImage =
                                ShowImageLayoutBinding.inflate(layoutInflater)
                            when (item.fileType) {
                                FileType.FOLDER.type -> {
                                    sendIntentToTransitionFragmentWithIntent(it,
                                        item.filePath)
                                }
                                FileType.PNG.type, FileType.JPG.type -> {
                                    val view = layoutInflater.inflate(R.layout.show_image_layout, null)
                                    view.findViewById<ImageView>(R.id.backShowImageView)
                                        .setOnClickListener {
                                            dialog.dismiss()
                                        }
                                    val bitmap = BitmapFactory.decodeFile(item.filePath)
                                    val imageView = view.findViewById<ImageView>(R.id.showImageView)
                                    imageView.setImageBitmap(bitmap)
                                    dialog = Dialog(activity!!, R.style.fullscreenalert)
                                    dialog.setContentView(view)
                                    showDialog(activity!!, dialog)
                                    dialog.setOnCancelListener {
                                        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                    }
                                    dialog.setOnDismissListener {
                                        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                    }
                                }
                                FileType.PDF.type -> {
                                    bindingImage.backShowImageView.setOnClickListener {
                                        dialog.cancel()
                                    }
                                    dialog = Dialog(activity!!, R.style.fullscreenalert)
                                    val showImageBitmap = BitmapUtils.getImageOfPdf(activity!!, File(item.filePath), 0)
                                    bindingImage.showImageView.setImageBitmap(BitmapUtils.getRoundedBitmap(activity.resources,
                                        showImageBitmap,
                                        10f))
                                    val list = BitmapUtils.pdfToBitmap(context, File(item.filePath))
                                    val radioGroup = RadioGroup(bindingImage.root.context)
                                    radioGroup.orientation = RadioGroup.HORIZONTAL
                                    radioGroup.clearCheck()
                                    list.forEachIndexed { index, imageBitmap ->
                                        val drawable =
                                            BitmapUtils.getRoundedBitmap(activity.resources, imageBitmap, 10f)
                                                .toDrawable(activity.resources)
                                        val layerDrawable = LayerDrawable(arrayOf(drawable))
                                        var params = LinearLayout.LayoutParams(100,
                                            100)
                                        params.setMargins(0, 0, 0, 0)
                                        val radioButton = RadioButton(context)
                                        params.gravity = Gravity.CENTER
                                        radioButton.buttonDrawable = null
                                        radioButton.id = index
                                        radioButton.setOnCheckedChangeListener { _, isChecked ->
                                            if (isChecked) {
                                                params.width += 50
                                                params.height += 50
                                                radioButton.layoutParams = params
                                                val layerDrawable = LayerDrawable(arrayOf(drawable))
                                                radioButton.background = layerDrawable
                                                bindingImage.showImageView.setImageBitmap(imageBitmap)
                                                bindingImage.executePendingBindings()
                                            } else {
                                                params.width -= 50
                                                params.height -= 50
                                                radioButton.layoutParams = params
                                                val layerDrawable = LayerDrawable(arrayOf(drawable))
                                                radioButton.background = layerDrawable
                                            }
                                        }
                                        radioButton.layoutParams = params
                                        radioButton.background = layerDrawable
                                        radioButton.setOnClickListener {
                                            bindingImage.showImageView.setImageBitmap(imageBitmap)
                                            bindingImage.executePendingBindings()
                                        }
                                        radioGroup.addView(radioButton)
                                        bindingImage.executePendingBindings()
                                    }
                                    bindingImage.root.setOnTouchListener(object : OnSwipeTouchListener(context),
                                        View.OnTouchListener {
                                        override fun onSwipeRight(): Boolean {
                                            if (radioGroup.checkedRadioButtonId < radioGroup.childCount - 1) {
                                                (radioGroup[radioGroup.checkedRadioButtonId + 1] as RadioButton).isChecked = true
                                            } else
                                                (radioGroup[0] as RadioButton).isChecked = true
                                            return true
                                        }

                                        override fun onSwipeLeft(): Boolean {
                                            if (radioGroup.checkedRadioButtonId > 0)
                                                (radioGroup[radioGroup.checkedRadioButtonId - 1] as RadioButton).isChecked = true
                                            else
                                                (radioGroup[radioGroup.childCount - 1] as RadioButton).isChecked = true
                                            return true
                                        }
                                    })
                                    radioGroup.gravity = Gravity.CENTER
                                    (radioGroup.getChildAt(0) as RadioButton).isChecked = true
                                    bindingImage.footLinearLayout.addView(radioGroup)
                                    bindingImage.executePendingBindings()
                                    dialog.setContentView(bindingImage.root)
                                    showDialog(activity!!, dialog)
                                    dialog.setOnDismissListener {
                                        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                    }
                                    dialog.setOnCancelListener {
                                        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                    }
                                }
                            }
                            bindingImage.optionsOfFileDetail.setOnClickListener {
                                var stringList =
                                    activity!!.resources.getStringArray(R.array.favorites_optionsmenu_string_array).toMutableList()
                                val drawableList = activity!!.resources.obtainTypedArray(R.array.favorites_images_array)
                                var taskTypeList = TaskType.values().filter {
                                    it.value == TaskType.TOOLSTASK.value || it.value == TaskType.SHARETASK.value ||
                                            it.value == TaskType.GOTOFOLDER.value
                                }.toMutableList()
                                if (favoritesDao.getFavorite(item.filePath, item.fileName, item.fileType) != null) {
                                    stringList.add(activity.resources.getString(R.string.remove_favorite))
                                    taskTypeList.add(TaskType.REMOVEFAVORITETASK)
                                } else {
                                    stringList.add(activity.resources.getString(R.string.mark_as_favorite))
                                    taskTypeList.add(TaskType.MARKFAVORITETASK)
                                }
                                showFavoritesBottomDialog(activity!!.supportFragmentManager,
                                    rowBinding.root,
                                    dialog!!,
                                    item,
                                    stringList,
                                    drawableList,
                                    taskTypeList)
                            }
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
            setHasFixedSize(true)
        }
        return transitionListAdapter
    }

    fun showFavoritesBottomDialog(
        supportFragmentManager: FragmentManager,
        view: View,
        dialog: Dialog,
        transitionModel: TransitionModel,
        stringList: List<String>,
        drawableList: TypedArray,
        taskTypeList: List<TaskType>,
    ) {
        try {
            val list = arrayListOf<OptionsModel>()
            val taskList = arrayListOf(ToolsTask(),
                ShareTask(context, arrayListOf(transitionModel), arrayListOf()),
                GoToFolderTask(transitionModel, view, dialog))
            val favoriteFile = transitionModel.toFavoriteModel()
            if (favoritesDao.getFavorite(favoriteFile.path, favoriteFile.fileName, favoriteFile.fileType) != null) {
                taskList.add(RemoveFavoriteTask(favoritesDao,
                    favoritesDao.getFavorite(favoriteFile.path,
                        favoriteFile.fileName,
                        favoriteFile.fileType)))
            } else {
                taskList.add(MarkFavoriteTask(arrayListOf(transitionModel)))
            }
            val optionsAdapter = OptionsAdapter()
            when (transitionModel.fileType) {
                FileType.PNG.type, FileType.JPG.type -> {
                    taskTypeList.forEachIndexed { index, s ->
                        if (s.value == TaskType.SHARETASK.value ||
                            s.value == TaskType.REMOVEFAVORITETASK.value ||
                            s.value == TaskType.MARKFAVORITETASK.value ||
                            s.value == TaskType.GOTOFOLDER.value
                        )
                            list += OptionsModel(drawableList.getDrawable(index)!!,
                                stringList[index],
                                taskList[index])
                    }
                }
                FileType.PDF.type -> {
                    taskTypeList.forEachIndexed { index, s ->
                        if (s.value == TaskType.TOOLSTASK.value ||
                            s.value == TaskType.SHARETASK.value ||
                            s.value == TaskType.REMOVEFAVORITETASK.value ||
                            s.value == TaskType.MARKFAVORITETASK.value ||
                            s.value == TaskType.GOTOFOLDER.value
                        )
                            list += OptionsModel(drawableList.getDrawable(index)!!,
                                stringList[index],
                                taskList[index])
                    }
                }
            }
            optionsAdapter.setItems(list)
            optionsAdapter.setListener(object : BaseViewBindingRecyclerViewAdapter.ClickListener<OptionsModel, RowOptionsItemBinding> {
                override fun onItemClick(item: OptionsModel, position: Int, rowBinding: RowOptionsItemBinding) {
                    rowBinding.root.setOnClickListener {
                        item.task.doTask(this@TransitionFragmentViewModel)
                        favoriteOptionsBottomDialog.dismiss()
                    }
                }
            })
            favoriteOptionsBottomDialog = FavoriteOptionsBottomDialog(optionsAdapter)
            favoriteOptionsBottomDialog.show(supportFragmentManager, "FavoriteOptionsBottomDialog")
        } catch (e: Exception) {
            LogManager.log(TAG, e.toString())
        }
    }

    fun setAdapter(
        context: Context,
        activity: FragmentActivity,
        layoutInflater: LayoutInflater,
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
                            val bindingImage =
                                ShowImageLayoutBinding.inflate(layoutInflater)
                            when (item.fileType) {
                                FileType.FOLDER.type -> {
                                    sendIntentToTransitionFragmentWithIntent(it,
                                        item.filePath)
                                }
                                FileType.PNG.type, FileType.JPG.type -> {
                                    val view = layoutInflater.inflate(R.layout.show_image_layout, null)
                                    view.findViewById<ImageView>(R.id.backShowImageView)
                                        .setOnClickListener {
                                            dialog.dismiss()
                                        }
                                    val bitmap = BitmapFactory.decodeFile(item.filePath)
                                    val imageView = view.findViewById<ImageView>(R.id.showImageView)
                                    imageView.setImageBitmap(bitmap)
                                    dialog = Dialog(activity!!, R.style.fullscreenalert)
                                    dialog.setContentView(view)
                                    showDialog(activity!!, dialog)
                                    dialog.setOnCancelListener {
                                        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                    }
                                    dialog.setOnDismissListener {
                                        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                    }

                                }
                                FileType.PDF.type -> {
                                    bindingImage.backShowImageView.setOnClickListener {
                                        dialog.cancel()
                                    }
                                    dialog = Dialog(activity!!, R.style.fullscreenalert)
                                    val showImageBitmap = BitmapUtils.getImageOfPdf(activity!!, File(item.filePath), 0)
                                    bindingImage.showImageView.setImageBitmap(BitmapUtils.getRoundedBitmap(activity.resources,
                                        showImageBitmap,
                                        10f))
                                    val list = BitmapUtils.pdfToBitmap(context, File(item.filePath))
                                    val radioGroup = RadioGroup(bindingImage.root.context)
                                    radioGroup.orientation = RadioGroup.HORIZONTAL
                                    radioGroup.clearCheck()
                                    list.forEachIndexed { index, imageBitmap ->
                                        val drawable =
                                            BitmapUtils.getRoundedBitmap(activity.resources, imageBitmap, 10f)
                                                .toDrawable(activity.resources)
                                        val layerDrawable = LayerDrawable(arrayOf(drawable))
                                        var params = LinearLayout.LayoutParams(100,
                                            100)
                                        params.setMargins(0, 0, 0, 0)
                                        val radioButton = RadioButton(context)
                                        params.gravity = Gravity.CENTER
                                        radioButton.buttonDrawable = null
                                        radioButton.id = index
                                        radioButton.setOnCheckedChangeListener { _, isChecked ->
                                            if (isChecked) {
                                                params.width += 50
                                                params.height += 50
                                                radioButton.layoutParams = params
                                                val layerDrawable = LayerDrawable(arrayOf(drawable))
                                                radioButton.background = layerDrawable
                                                bindingImage.showImageView.setImageBitmap(imageBitmap)
                                                bindingImage.executePendingBindings()
                                            } else {
                                                params.width -= 50
                                                params.height -= 50
                                                radioButton.layoutParams = params
                                                val layerDrawable = LayerDrawable(arrayOf(drawable))
                                                radioButton.background = layerDrawable
                                            }
                                        }
                                        radioButton.layoutParams = params
                                        radioButton.background = layerDrawable
                                        radioButton.setOnClickListener {
                                            bindingImage.showImageView.setImageBitmap(imageBitmap)
                                            bindingImage.executePendingBindings()
                                        }
                                        radioGroup.addView(radioButton)
                                        bindingImage.executePendingBindings()
                                    }
                                    bindingImage.root.setOnTouchListener(object : OnSwipeTouchListener(context),
                                        View.OnTouchListener {
                                        override fun onSwipeRight(): Boolean {
                                            if (radioGroup.checkedRadioButtonId < radioGroup.childCount - 1) {
                                                (radioGroup[radioGroup.checkedRadioButtonId + 1] as RadioButton).isChecked = true
                                            } else
                                                (radioGroup[0] as RadioButton).isChecked = true
                                            return true
                                        }

                                        override fun onSwipeLeft(): Boolean {
                                            if (radioGroup.checkedRadioButtonId > 0)
                                                (radioGroup[radioGroup.checkedRadioButtonId - 1] as RadioButton).isChecked = true
                                            else
                                                (radioGroup[radioGroup.childCount - 1] as RadioButton).isChecked = true
                                            return true
                                        }
                                    })
                                    radioGroup.gravity = Gravity.CENTER
                                    (radioGroup.getChildAt(0) as RadioButton).isChecked = true

                                    bindingImage.footLinearLayout.addView(radioGroup)
                                    bindingImage.executePendingBindings()
                                    dialog.setContentView(bindingImage.root)
                                    showDialog(activity!!, dialog)
                                    dialog.setOnDismissListener {
                                        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                    }
                                    dialog.setOnCancelListener {
                                        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                    }
                                }
                            }
                            bindingImage.optionsOfFileDetail.setOnClickListener {
                                var stringList =
                                    activity!!.resources.getStringArray(R.array.favorites_optionsmenu_string_array).toMutableList()
                                val drawableList = activity!!.resources.obtainTypedArray(R.array.favorites_images_array)
                                val taskTypeList = TaskType.values().filter {
                                    it.value == TaskType.TOOLSTASK.value || it.value == TaskType.SHARETASK.value ||
                                            it.value == TaskType.GOTOFOLDER.value
                                }.toMutableList()
                                if (favoritesDao.getFavorite(item.filePath, item.fileName, item.fileType) != null) {
                                    stringList.add(activity!!.resources.getString(R.string.remove_favorite))
                                    taskTypeList.add(TaskType.REMOVEFAVORITETASK)
                                } else {
                                    stringList.add(activity!!.resources.getString(R.string.mark_as_favorite))
                                    taskTypeList.add(TaskType.MARKFAVORITETASK)
                                }
                                showFavoritesBottomDialog(activity!!.supportFragmentManager,
                                    rowBinding.root,
                                    dialog!!,
                                    item,
                                    stringList,
                                    drawableList,
                                    taskTypeList)
                            }
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
            setHasFixedSize(true)
        }

        return transitionGridAdapter
    }

    fun showOptionsBottomDialog(
        supportFragmentManager: FragmentManager,
        transitionList: ArrayList<TransitionModel>,
    ) {
        try {
            val list = arrayListOf<OptionsModel>()
            val adapter = OptionsAdapter()
            val title =
                if (transitionList.size > 1) transitionList.size.toString() + "  " + context!!.getString(R.string.item) else transitionList.get(
                    0).fileName
            optionsBottomDialog = OptionsBottomDialog(adapter, title, transitionList)
            val stringList = context.resources.getStringArray(R.array.options_menu_string_array).toList()
            val drawableList = context.resources.obtainTypedArray(R.array.imagesArray)
            val taskTypeList = TaskType.values().copyOfRange(0, stringList.size).toList()
            val taskList = arrayListOf(
                ToolsTask(),
                ShareTask(context, transitionList, selectedRowList),
                MarkFavoriteTask(transitionList),
                CreateFolderWithSelectionTask(supportFragmentManager, transitionList, folderPath.value!!, favoritesDao),
                RenameTask(supportFragmentManager, transitionList, favoritesDao),
                DeleteTask(supportFragmentManager, transitionList),
                MoveTask(context, optionsBottomDialog, transitionList),
                CopyTask(context, optionsBottomDialog, transitionList),
                DuplicateTask(this, transitionList, selectedRowList)
            )
            when (transitionList.size) {
                0, 1 -> {
                    transitionList.forEach {
                        when (it.fileType) {
                            FileType.FOLDER.type -> {
                                taskTypeList.forEachIndexed { index, s ->
                                    if (s.value != TaskType.SHARETASK.value && s.value != TaskType.TOOLSTASK.value) {
                                        if (it.isFavorite && s.value == TaskType.MARKFAVORITETASK.value) {
                                            list += OptionsModel(drawableList.getDrawable(index)!!,
                                                context.getString(R.string.remove_favorite),
                                                taskList[index])
                                        } else {
                                            list += OptionsModel(drawableList.getDrawable(index)!!,
                                                stringList[index].toString(),
                                                taskList[index])
                                        }
                                    }
                                }
                            }
                            else -> {
                                taskTypeList.forEachIndexed { index, s ->
                                    if (it.isFavorite && s.value == TaskType.MARKFAVORITETASK.value) {
                                        list += OptionsModel(drawableList.getDrawable(index)!!,
                                            context.getString(R.string.remove_favorite),
                                            taskList[index])
                                    } else {
                                        list += OptionsModel(drawableList.getDrawable(index)!!,
                                            stringList.get(index).toString(),
                                            taskList[index])
                                    }
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
                                list += OptionsModel(drawableList.getDrawable(index)!!,
                                    stringList[index].toString(),
                                    taskList[index])
                        }
                    } else {
                        taskTypeList.forEachIndexed { index, s ->
                            if (s.value != TaskType.RENAMETASK.value && s.value != TaskType.MARKFAVORITETASK.value)
                                list += OptionsModel(drawableList.getDrawable(index)!!,
                                    stringList[index].toString(),
                                    taskList[index])
                        }
                    }
                }
            }
            adapter.setListener(object :
                BaseViewBindingRecyclerViewAdapter.ClickListener<OptionsModel, RowOptionsItemBinding> {
                override fun onItemClick(
                    item: OptionsModel,
                    position: Int,
                    rowBinding: RowOptionsItemBinding,
                ) {
                    rowBinding.optionsBottomLinearLayout.setOnClickListener {
                        item.task.doTask(this@TransitionFragmentViewModel)
                        optionsBottomDialog.dismiss()
                        sendItemsChangedSate(true)
                        sendLongListenerActivated(false)
                    }
                }
            })

            adapter.setItems(list)

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

        setViewVisibility(binding.transitionToolbarMenu.root,
            !longListenerActivated.value!!)
        setViewVisibility(binding.multipleSelectionMenu.root, longListenerActivated.value!!)

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
                        activity,
                        activity.layoutInflater,
                        binding.transitionRecylerView,
                        transitionFragment.transitionGridAdapter,
                        getFilesFromPath(path, app.getFilterState()))
                }
                LayoutState.GRID_LAYOUT.value -> {
                    app.setLayoutState(LayoutState.LIST_LAYOUT.value)
                    binding.transitionToolbarMenu.layoutImageView.setImageDrawable(context.getDrawable(
                        R.drawable.icon_grid))
                    setAdapter(context, activity!!, activity!!.layoutInflater,
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
        setCreatefolderAnimations(binding)
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

    fun shareItemsList(context: Context, transitionList: ArrayList<TransitionModel>) {
        val uriArrayList = arrayListOf<Uri>()
        transitionList.forEach {
            uriArrayList += FileProvider.getUriForFile(context!!,
                BuildConfig.APPLICATION_ID + ".provider",
                File(it.filePath))
        }
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setType("*/*")
        intent.putExtra(Intent.EXTRA_STREAM, uriArrayList)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
        context.startActivity(intent)
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

    fun setViewVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setCreatefolderAnimations(binding: FragmentTransitionBinding) {
        val slideOutUp: AnimatorSet = AnimationX().getNewAnimatorSet()
        val slideInDown: AnimatorSet = AnimationX().getNewAnimatorSet()
        val slideOutUpAnimator = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                setViewVisibility(binding.createFolderButton, false)
            }

            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationRepeat(animation: Animator?) = Unit
        }
        val slideInDownAnimator = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                setViewVisibility(binding.createFolderButton, true)
            }

            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationRepeat(animation: Animator?) = Unit
        }
        val animatorSetSlideInDown = AnimationXUtils.slideInDown(binding.createFolderButton, slideInDown)
        val animatorSetSlideOutUp = AnimationXUtils.slideOutUp(binding.createFolderButton, slideOutUp)
        binding.transitionRecylerView.apply {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.canScrollVertically(1) && dy > 0) {
                        if (binding.createFolderButton.isVisible) {
                            binding.createFolderButton.animationStart(300, animatorSetSlideOutUp, slideOutUpAnimator)
                        }
                        //scrolled to BOTTOM
                    } else if (recyclerView.canScrollVertically(-1) && dy < 0) {
                        if (!getLongListenerActivatedMutableLiveData().value!!)
                            if (!binding.createFolderButton.isVisible) {
                                binding.createFolderButton.animationStart(300, animatorSetSlideInDown, slideInDownAnimator)
                            }
                        //scrolled to TOP
                    }
                }
            })
        }
    }

    fun showDialog(activity: Activity, dialog: Dialog) {
        activity.window.statusBarColor = ContextCompat.getColor(activity, R.color.black)
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.cancel()
            }
            return@setOnKeyListener false
        }
        dialog.show()
    }

    fun getItemsChangedStateMutableLiveData() = this.itemsChangedState
    fun getSelectedRowSizeMutableLiveData() = this.selectedRowSize
    fun getLongListenerActivatedMutableLiveData() = this.longListenerActivated
    fun getSelectedRowList() = this.selectedRowList
    fun getFilterStateMutableLiveData() = this.filterState
    fun getNoDataStateMutableLiveData() = this.noDataState
    fun getFolderPathMutableLiveData() = this.folderPath
}