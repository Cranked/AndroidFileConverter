package com.cranked.androidfileconverter.ui.camera

import android.app.Activity
import android.content.Context
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
import javax.inject.Inject

class CameraImageFragmentViewModel @Inject constructor(private val mContext: Context) : BaseViewModel() {
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
                    println("hebele")
                }
            })
        }
        recylerView.apply {
            adapter = photoStaggeredAdapter
            layoutManager =
                StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        }
        return photoStaggeredAdapter
    }

    fun showDialog(activity: Activity, path: String) {

    }
}