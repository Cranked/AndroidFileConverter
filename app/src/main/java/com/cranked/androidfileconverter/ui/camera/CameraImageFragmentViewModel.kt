package com.cranked.androidfileconverter.ui.camera

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.adapter.photo.PhotoStaggeredAdapter
import com.cranked.androidfileconverter.databinding.RowImageStaggeredItemBinding
import com.cranked.androidfileconverter.ui.model.ImagePreview
import com.cranked.androidfileconverter.ui.model.PhotoFile
import com.cranked.androidfileconverter.utils.AnimationX
import com.cranked.androidfileconverter.utils.AnimationXUtils
import com.cranked.androidfileconverter.utils.animation.animationStart
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import javax.inject.Inject

class CameraImageFragmentViewModel @Inject constructor() : BaseViewModel() {
    private val imagePath = MutableLiveData<PhotoFile>()

    fun sendImagePath(value: PhotoFile) {
        imagePath.postValue(value)
    }
    fun getImagePathMutableLiveData() = this.imagePath
    fun setAdapter(
        context: Context,
        recylerView: RecyclerView,
        photoStaggeredAdapter: PhotoStaggeredAdapter,
        list: MutableList<ImagePreview>,
    ): PhotoStaggeredAdapter {
        photoStaggeredAdapter.apply {
            setItems(list)
            setListener(object : BaseViewBindingRecyclerViewAdapter.ClickListener<ImagePreview, RowImageStaggeredItemBinding> {
                override fun onItemClick(item: ImagePreview, position: Int, rowBinding: RowImageStaggeredItemBinding) {

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
}