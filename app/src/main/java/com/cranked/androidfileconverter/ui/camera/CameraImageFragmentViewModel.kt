package com.cranked.androidfileconverter.ui.camera

import android.animation.Animator
import android.animation.AnimatorSet
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.BuildConfig
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.options.OptionsAdapter
import com.cranked.androidfileconverter.adapter.photo.PhotoStaggeredAdapter
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.databinding.RowImageStaggeredItemBinding
import com.cranked.androidfileconverter.databinding.RowOptionsItemBinding
import com.cranked.androidfileconverter.dialog.options.GoToFolderTask
import com.cranked.androidfileconverter.dialog.options.ShareTask
import com.cranked.androidfileconverter.dialog.takenphoto.TakenPhotoOptionsDialog
import com.cranked.androidfileconverter.ui.model.ImagePreview
import com.cranked.androidfileconverter.ui.model.OptionsModel
import com.cranked.androidfileconverter.ui.model.PhotoFile
import com.cranked.androidfileconverter.utils.AnimationX
import com.cranked.androidfileconverter.utils.AnimationXUtils
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.animation.animationStart
import com.cranked.androidfileconverter.utils.enums.TaskType
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import java.io.File
import javax.inject.Inject

class CameraImageFragmentViewModel @Inject constructor(private val favoritesDao: FavoritesDao) : BaseViewModel() {
    private val imagePath = MutableLiveData<PhotoFile>()
    private val TAG = this::class.java.toString().substringAfterLast(".")

    fun sendImagePath(value: PhotoFile) {
        imagePath.postValue(value)
    }

    fun getImagePathMutableLiveData() = this.imagePath
    fun setAdapter(
        activity: FragmentActivity,
        layoutInflater: LayoutInflater,
        recylerView: RecyclerView,
        dialog: Dialog,
        photoStaggeredAdapter: PhotoStaggeredAdapter,
        list: MutableList<ImagePreview>,
    ): PhotoStaggeredAdapter {
        photoStaggeredAdapter.apply {
            setItems(list)
            setListener(object : BaseViewBindingRecyclerViewAdapter.ClickListener<ImagePreview, RowImageStaggeredItemBinding> {
                override fun onItemClick(item: ImagePreview, position: Int, rowBinding: RowImageStaggeredItemBinding) {
                    rowBinding.imagePreviewLinearLayout.setOnClickListener {
                        val view = layoutInflater.inflate(R.layout.show_image_layout, null)
                        view.findViewById<ImageView>(R.id.backShowImageView)
                            .setOnClickListener {
                                dialog.dismiss()
                            }
                        view.findViewById<ImageView>(R.id.optionsOfFileDetail).setOnClickListener {
                            var stringList =
                                activity.resources.getStringArray(R.array.taken_photo_preview_array_string).toMutableList()
                            val drawableList = activity.resources.obtainTypedArray(R.array.taken_photo_preview_array)
                            var taskTypeList = TaskType.values().filter {
                                it.value == TaskType.SHARETASK.value || it.value == TaskType.GOTOFOLDER.value
                            }.toMutableList()
                            showTakenPhotoOptionsBottomDialog(activity.supportFragmentManager,
                                it.rootView.context,
                                rowBinding.root,
                                dialog,
                                item,
                                stringList,
                                drawableList,
                                taskTypeList)
                        }
                        val bitmap = BitmapFactory.decodeFile(item.path)
                        val imageView = view.findViewById<ImageView>(R.id.showImageView)
                        imageView.setImageBitmap(bitmap)
                        dialog.setContentView(view)
                        showDialog(activity, dialog)
                        dialog.setOnCancelListener {
                            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                            activity.window.statusBarColor = activity.getColor(R.color.primary_color)
                        }
                        dialog.setOnDismissListener {
                            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                            activity.window.statusBarColor = activity.getColor(R.color.primary_color)
                        }

                    }
                }
            })
        }
        recylerView.apply {
            adapter = photoStaggeredAdapter
            layoutManager =
                StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
        return photoStaggeredAdapter
    }

    fun showTakenPhotoOptionsBottomDialog(
        supportFragmentManager: FragmentManager,
        context: Context,
        view: View,
        dialog: Dialog,
        imagePreview: ImagePreview,
        stringList: List<String>,
        drawableList: TypedArray,
        taskTypeList: List<TaskType>,
    ) {
        try {
            var takenPhotoOptionsDialog: TakenPhotoOptionsDialog? = null
            val list = arrayListOf<OptionsModel>()
            val taskList = arrayListOf(ShareTask(context, imagePreview),
                GoToFolderTask(imagePreview, view, dialog))

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
                        item.task.doTask(this@CameraImageFragmentViewModel)
                        takenPhotoOptionsDialog!!.dismiss()
                    }
                }
            })
            takenPhotoOptionsDialog = TakenPhotoOptionsDialog(optionsAdapter, imagePreview.fileName)
            takenPhotoOptionsDialog.show(supportFragmentManager, "TakenPhotoOptionsBottomDialog")
        } catch (e: Exception) {
            LogManager.log(TAG, e.toString())
        }
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

    fun goToTransitionFragmentWithIntent(@IdRes actionId: Int, view: View, path: String) {
        try {
            val bundle = Bundle()
            bundle.putString(Constants.DESTINATION_PATH_ACTION, path)
            view.findNavController().navigate(actionId, bundle)
        } catch (e: Exception) {
            LogManager.log(TAG, e)
        }
    }

    fun shareItemsList(context: Context, fileList: ArrayList<ImagePreview>) {
        val uriArrayList = arrayListOf<Uri>()
        fileList.forEach {
            uriArrayList += FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider",
                File(it.path))
        }
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_STREAM, uriArrayList)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.")
        context.startActivity(intent)
    }
}