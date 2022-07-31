package com.cranked.androidfileconverter.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.BuildConfig
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.options.OptionsAdapter
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.data.database.dao.ProcessedFilesDao
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.databinding.RowOptionsItemBinding
import com.cranked.androidfileconverter.dialog.favorite.FavoriteOptionsBottomDialog
import com.cranked.androidfileconverter.ui.model.OptionsModel
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.enums.FileType
import com.cranked.androidfileconverter.utils.enums.TaskType
import com.cranked.androidfileconverter.utils.file.FileUtility
import java.io.File
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val context: Context,
    private val favoritesDao: FavoritesDao,
    processedFilesDao: ProcessedFilesDao,
    private val mContext: Context,
) :
    BaseViewModel() {
    val TAG = HomeFragmentViewModel::class.java.name
    private val favItemsChanged = MutableLiveData<Boolean>()
    val sdCardState = FileUtils.isSdCardMounted(mContext)
    var storageModel = FileUtility.getMenuFolderSizes(mContext, processedFilesDao)
    private var favoritesList = favoritesDao.getAll()
    var favoritesState = favoritesList.isNotEmpty()
    private lateinit var favoriteOptionsBottomDialog: FavoriteOptionsBottomDialog

    fun goToTransitionFragmentWithIntent(view: View, path: String) {
        val bundle = Bundle()
        bundle.putString(Constants.DESTINATION_PATH_ACTION, path)
        view.findNavController()
            .navigate(R.id.action_home_dest_to_transition_fragment, bundle)
    }

    fun internalStoragePath(view: View) =
        goToTransitionFragmentWithIntent(view, FileUtility.getInternalStoragePath())

    fun sdCardPathFolder(view: View) =
        goToTransitionFragmentWithIntent(view, FileUtility.getSdCarPath(view.context))

    fun downloadsPathFolder(view: View) =
        goToTransitionFragmentWithIntent(view, FileUtility.getDownloadsPath())

    fun fileTransformerPathFolder(view: View) =
        goToTransitionFragmentWithIntent(view, FileUtility.getFileTransformerPath())

    fun processedFolderPath(view: View) =
        goToTransitionFragmentWithIntent(view, FileUtility.getProcessedPath())

    @JvmName("setFavoritesState1")
    fun setFavoritesState(value: Boolean) {
        favoritesState = value
    }

    fun showFavoritesBottomDialog(supportFragmentManager: FragmentManager, view: View, favoriteFile: FavoriteFile) {
        try {
            val list = arrayListOf<OptionsModel>()
            val stringList = context.resources.getStringArray(R.array.favorites_optionsmenu_string_array).toList()
            val drawableList = context.resources.obtainTypedArray(R.array.favorites_images_array)
            val taskTypeList = TaskType.values().filter {
                it.value == TaskType.TOOLSTASK.value || it.value == TaskType.SHARETASK.value ||
                        it.value == TaskType.REMOVEFAVORITETASK.value || it.value == TaskType.GOTOFOLDER.value
            }.toList()
            val optionsAdapter = OptionsAdapter()
            when (favoriteFile.fileType) {
                FileType.FOLDER.type -> {
                    taskTypeList.forEachIndexed { index, s ->
                        if (s.value == TaskType.REMOVEFAVORITETASK.value)
                            list += OptionsModel(drawableList.getDrawable(index)!!, stringList.get(index).toString(), s.value)
                    }
                }
                else -> {
                    taskTypeList.forEachIndexed { index, s ->
                        if (s.value == TaskType.TOOLSTASK.value ||
                            s.value == TaskType.SHARETASK.value ||
                            s.value == TaskType.REMOVEFAVORITETASK.value ||
                            s.value == TaskType.GOTOFOLDER.value
                        )
                            list += OptionsModel(drawableList.getDrawable(index)!!, stringList.get(index).toString(), s.value)
                    }
                }
            }
            optionsAdapter.setItems(list)
            optionsAdapter.setListener(object : BaseViewBindingRecyclerViewAdapter.ClickListener<OptionsModel, RowOptionsItemBinding> {
                override fun onItemClick(item: OptionsModel, position: Int, rowBinding: RowOptionsItemBinding) {
                    rowBinding.root.setOnClickListener {
                        when (item.tasktype) {
                            TaskType.SHARETASK.value -> {
                                shareItemsList(context, arrayListOf(favoriteFile))
                            }
                            TaskType.GOTOFOLDER.value -> {
                                val path = favoriteFile.path.substring(0, favoriteFile.path.lastIndexOf("/")) + File.separator
                                goToTransitionFragmentWithIntent(view, path)
                            }
                            TaskType.TOOLSTASK.value -> {

                            }
                            TaskType.REMOVEFAVORITETASK.value -> {
                                favoritesDao.delete(favoriteFile)
                                getFavItemsChangedMutableLiveData().postValue(true)
                            }
                        }
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

    fun shareItemsList(context: Context, transitionList: ArrayList<FavoriteFile>) {
        val uriArrayList = arrayListOf<Uri>()
        transitionList.forEach {
            uriArrayList += FileProvider.getUriForFile(context!!,
                BuildConfig.APPLICATION_ID + ".provider",
                File(it.path))
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

    fun getFavItemsChangedMutableLiveData() = this.favItemsChanged
}
