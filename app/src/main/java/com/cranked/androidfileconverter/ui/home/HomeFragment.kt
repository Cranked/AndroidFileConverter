package com.cranked.androidfileconverter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.dialog.BaseDialog
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.FavoritesAdapter
import com.cranked.androidfileconverter.adapter.FavoritesAdapterViewModel
import com.cranked.androidfileconverter.adapter.recentfile.RecentFileAdapter
import com.cranked.androidfileconverter.adapter.recentfile.RecentFileAdapterViewModel
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.data.database.dao.ProcessedFilesDao
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.data.database.entity.RecentFile
import com.cranked.androidfileconverter.databinding.FragmentHomeBinding
import com.cranked.androidfileconverter.databinding.RowFavoriteAdapterItemBinding
import com.cranked.androidfileconverter.databinding.RowRecentfileItemBinding
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.enums.FileType
import com.cranked.androidfileconverter.utils.file.FileUtility
import com.cranked.androidfileconverter.utils.junk.ToolbarState
import javax.inject.Inject


class HomeFragment @Inject constructor() :
    BaseDaggerFragment<HomeFragmentViewModel, FragmentHomeBinding>(HomeFragmentViewModel::class.java) {
    val TAG = this::class.java.toString().substringAfterLast(".")

    @Inject
    lateinit var favoritesAdapterViewModel: FavoritesAdapterViewModel

    @Inject
    lateinit var recentFileAdapterViewModel: RecentFileAdapterViewModel

    @Inject
    lateinit var favoritesDao: FavoritesDao

    @Inject
    lateinit var processedFilesDao: ProcessedFilesDao
    val app by lazy {
        activity!!.application as FileConvertApp
    }
    var favoritesAdapter: FavoritesAdapter = FavoritesAdapter(R.layout.row_favorite_adapter_item)
    var recentFileAdapter = RecentFileAdapter()
    lateinit var dialog: BaseDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        app.rxBus.send(ToolbarState(true))
        app.appComponent.bindHomeFragment(this)

        return binding.root
    }

    override fun getViewDataBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
    ): FragmentHomeBinding {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, parent, false)
    }

    override fun initViewModel(viewModel: HomeFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            val favoritesList = favoritesDao.getAll()
            viewModel.storageModel = FileUtility.getMenuFolderSizes(context!!, processedFilesDao)
            viewModel.setFavoritesState(favoritesList.isNotEmpty())
            favoritesAdapter = favoritesAdapterViewModel.setAdapter(this.context!!,
                binding.favoritesRecylerView,
                favoritesAdapter,
                favoritesList
            )
            favoritesAdapter.setListener(object :
                BaseViewBindingRecyclerViewAdapter.ClickListener<FavoriteFile, RowFavoriteAdapterItemBinding> {
                override fun onItemClick(
                    item: FavoriteFile,
                    position: Int,
                    rowBinding: RowFavoriteAdapterItemBinding,
                ) {
                    rowBinding.favoriteLinearLayout.setOnClickListener {
                        when (item.fileType) {
                            FileType.FOLDER.type -> {
                                viewModel.goToTransitionFragmentWithIntent(it, item.path)
                            }
                            FileType.PNG.type, FileType.JPG.type -> {
                                val view = layoutInflater.inflate(R.layout.show_image_layout, null)
                                dialog = BaseDialog(activity!!, view, R.style.fullscreenalert)
                                viewModel.showDialog(activity!!, dialog, view, item.path)
                            }
                        }
                    }
                }
            })
            favoritesAdapter.setLongClickListener(object :
                BaseViewBindingRecyclerViewAdapter.LongClickListener<FavoriteFile, RowFavoriteAdapterItemBinding> {
                override fun onItemLongClick(item: FavoriteFile, position: Int, rowBinding: RowFavoriteAdapterItemBinding) {
                    rowBinding.favoriteLinearLayout.setOnLongClickListener {
                        viewModel.showFavoritesBottomDialog(activity!!.supportFragmentManager, it, item)
                        return@setOnLongClickListener true
                    }
                }
            })
            recentFileAdapter = recentFileAdapterViewModel.setAdapter(
                this.context!!, binding.recentFileRecylerView,
                recentFileAdapter, recentFileAdapterViewModel.recentFileList
            )
            recentFileAdapter.setListener(object :
                BaseViewBindingRecyclerViewAdapter.ClickListener<RecentFile, RowRecentfileItemBinding> {
                override fun onItemClick(
                    item: RecentFile,
                    position: Int,
                    rowBinding: RowRecentfileItemBinding,
                ) {
                    rowBinding.recentFileLinearLayout.setOnClickListener {
                        viewModel.goToTransitionFragmentWithIntent(it, item.path)
                    }
                }
            })
        } catch (e: Exception) {
            LogManager.log(TAG, e)
        }
    }


    override fun createLiveData(viewLifecycleOwner: LifecycleOwner) {
        viewModel.getFavItemsChangedMutableLiveData().observe(viewLifecycleOwner) {
            val list = favoritesDao.getAll()
            favoritesAdapter.setItems(list)
        }
    }
}