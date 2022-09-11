package com.cranked.androidfileconverter.ui.camera

import android.animation.Animator
import android.animation.AnimatorSet
import android.app.Dialog
import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.options.OptionsAdapter
import com.cranked.androidfileconverter.adapter.photo.PhotoAdapter
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.databinding.RowOptionsItemBinding
import com.cranked.androidfileconverter.databinding.RowTakenPhotoBinding
import com.cranked.androidfileconverter.dialog.DeleteDialog
import com.cranked.androidfileconverter.dialog.options.DeleteTask
import com.cranked.androidfileconverter.dialog.options.GoToFolderTask
import com.cranked.androidfileconverter.dialog.options.RenameTask
import com.cranked.androidfileconverter.dialog.options.ToolsTask
import com.cranked.androidfileconverter.dialog.takenphoto.TakenPhotoOptionsDialog
import com.cranked.androidfileconverter.ui.model.OptionsModel
import com.cranked.androidfileconverter.ui.model.PhotoFile
import com.cranked.androidfileconverter.ui.model.toTransitionModel
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.AnimationX
import com.cranked.androidfileconverter.utils.AnimationXUtils
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.animation.animationStart
import com.cranked.androidfileconverter.utils.enums.TaskType
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CameraFragmentViewModel @Inject constructor(private val context: Context, private val favoritesDao: FavoritesDao) :
    BaseViewModel() {
    private val TAG = this::class.java.toString().substringAfterLast(".")
    lateinit var dialog: Dialog
    private val itemsChangedState = MutableLiveData<Boolean>()


    lateinit var takenPhotoOptionsDialog: TakenPhotoOptionsDialog
    fun setAdapter(
        context: Context,
        activity: FragmentActivity,
        recylerView: RecyclerView,
        photoAdapter: PhotoAdapter,
        list: MutableList<PhotoFile>,
        dialog: Dialog,
    ): PhotoAdapter {
        photoAdapter.apply {
            setItems(list)
            setListener(object : BaseViewBindingRecyclerViewAdapter.ClickListener<PhotoFile, RowTakenPhotoBinding> {
                override fun onItemClick(item: PhotoFile, position: Int, rowBinding: RowTakenPhotoBinding) {
                    rowBinding.takePhotoLinearLayout.setOnClickListener {
                        goWithIntentToCameraImageFragment(R.id.action_camera_dest_to_cameraImageFragment, it, item)
                    }
                    rowBinding.takenPhotoOptions.setOnClickListener {
                        var stringList =
                            activity.resources.getStringArray(R.array.taken_photo_menu_array).toList()
                        val drawableList = activity.resources.obtainTypedArray(R.array.taken_photo_images_array)
                        var taskTypeList = TaskType.values().filter {
                            it.value == TaskType.TOOLSTASK.value || it.value == TaskType.RENAMETASK.value ||
                                    it.value == TaskType.DELETETASK.value || it.value == TaskType.GOTOFOLDER.value
                        }.toList()

                        showTakenPhotoOptionsBottomDialog(activity.supportFragmentManager,
                            rowBinding.root,
                            dialog,
                            item,
                            stringList,
                            drawableList,
                            taskTypeList)
                    }
                }
            })
        }
        recylerView.apply {
            adapter = photoAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        return photoAdapter
    }

    fun goWithIntentToCameraImageFragment(@IdRes actionId: Int, view: View, photoFile: PhotoFile) {
        try {
            val bundle = Bundle()
            bundle.putParcelable(Constants.IMAGE_CONTENT, photoFile)
            view.findNavController().navigate(actionId, bundle)
        } catch (e: Exception) {
            LogManager.log(TAG, e)
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun goToTransitionFragmentWithIntent(@IdRes actionId: Int, view: View, path: String) {
        try {
            val bundle = Bundle()
            bundle.putString(Constants.DESTINATION_PATH_ACTION, path)
            view.findNavController().navigate(actionId, bundle)
        } catch (e: Exception) {
            LogManager.log(TAG, e)
        }
    }

    fun getImagePath(path: String) = path + SimpleDateFormat("yyyyMMdd_HHmmss").format(
        Date())

    fun showTakenPhotoOptionsBottomDialog(
        supportFragmentManager: FragmentManager,
        view: View,
        dialog: Dialog,
        photoFile: PhotoFile,
        stringList: List<String>,
        drawableList: TypedArray,
        taskTypeList: List<TaskType>,
    ) {
        try {
            val list = arrayListOf<OptionsModel>()
            val taskList = arrayListOf(ToolsTask(),
                RenameTask(supportFragmentManager, arrayListOf(photoFile.toTransitionModel()), favoritesDao),
                DeleteTask(supportFragmentManager, arrayListOf(photoFile.toTransitionModel())),
                GoToFolderTask(photoFile, view, dialog))

            val optionsAdapter = OptionsAdapter()
            taskTypeList.forEachIndexed { index, s ->
                list += OptionsModel(drawableList.getDrawable(index)!!,
                    stringList[index],
                    taskList[index])
            }

            optionsAdapter.setItems(list)
            optionsAdapter.setListener(object : BaseViewBindingRecyclerViewAdapter.ClickListener<OptionsModel, RowOptionsItemBinding> {
                override fun onItemClick(item: OptionsModel, position: Int, rowBinding: RowOptionsItemBinding) {
                    rowBinding.root.setOnClickListener {
                        item.task.doTask(this@CameraFragmentViewModel)
                        takenPhotoOptionsDialog.dismiss()
                    }
                }
            })
            takenPhotoOptionsDialog = TakenPhotoOptionsDialog(optionsAdapter, photoFile.fileName)
            takenPhotoOptionsDialog.show(supportFragmentManager, "TakenPhotoOptionsBottomDialog")
        } catch (e: Exception) {
            LogManager.log(TAG, e.toString())
        }
    }

    fun sendItemsChangedSate(value: Boolean) {
        itemsChangedState.postValue(value)
    }

    fun setTakePhotoAnimationsWithRecyclerView(view: View, recylerView: RecyclerView) {
        val slideOutUp: AnimatorSet = AnimationX().getNewAnimatorSet()
        val slideInDown: AnimatorSet = AnimationX().getNewAnimatorSet()
        val slideOutUpAnimator = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                BitmapUtils.setViewVisibility(view, false)
            }

            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationRepeat(animation: Animator?) = Unit
        }
        val slideInDownAnimator = object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                BitmapUtils.setViewVisibility(view, true)
            }

            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationRepeat(animation: Animator?) = Unit
        }
        val animatorSetSlideInDown = AnimationXUtils.slideInDown(view, slideInDown)
        val animatorSetSlideOutUp = AnimationXUtils.slideOutUp(view, slideOutUp)
        recylerView.apply {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.canScrollVertically(1) && dy > 0) {
                        if (view.isVisible) {
                            view.animationStart(300, animatorSetSlideOutUp, slideOutUpAnimator)
                        }
                        //scrolled to BOTTOM
                    } else if (recyclerView.canScrollVertically(-1) && dy < 0) {
                            if (!view.isVisible) {
                                view.animationStart(300, animatorSetSlideInDown, slideInDownAnimator)
                            }
                        //scrolled to TOP
                    }
                }
            })
        }
    }

    fun showDeleteFialog(
        supportFragmentManager: FragmentManager,
        list: ArrayList<TransitionModel>,
    ) {
        val deleteDialogFragment = DeleteDialog(this@CameraFragmentViewModel, list, favoritesDao)
        deleteDialogFragment.show(supportFragmentManager, "DeleteDialogFragment")
    }

    fun getItemsChangedStateMutableLiveData() = this.itemsChangedState

}