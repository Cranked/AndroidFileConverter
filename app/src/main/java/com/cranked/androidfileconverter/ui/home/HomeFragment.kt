package com.cranked.androidfileconverter.ui.home

import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.FavoritesAdapterViewModel
import com.cranked.androidfileconverter.adapter.favorites.FavoritesAdapter
import com.cranked.androidfileconverter.adapter.recentfile.RecentFileAdapter
import com.cranked.androidfileconverter.adapter.recentfile.RecentFileAdapterViewModel
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.data.database.dao.ProcessedFilesDao
import com.cranked.androidfileconverter.data.database.entity.FavoriteFile
import com.cranked.androidfileconverter.data.database.entity.RecentFile
import com.cranked.androidfileconverter.databinding.FragmentHomeBinding
import com.cranked.androidfileconverter.databinding.RowFavoriteAdapterItemBinding
import com.cranked.androidfileconverter.databinding.RowRecentfileItemBinding
import com.cranked.androidfileconverter.databinding.ShowImageLayoutBinding
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.OnSwipeTouchListener
import com.cranked.androidfileconverter.utils.enums.FileType
import com.cranked.androidfileconverter.utils.enums.TaskType
import com.cranked.androidfileconverter.utils.file.FileUtility
import com.cranked.androidfileconverter.utils.image.BitmapUtils
import com.cranked.androidfileconverter.utils.junk.ToolbarState
import java.io.File
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
    var dialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        app.rxBus.send(ToolbarState(true))
        app.appComponent.bindHomeFragment(this)
        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
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
                        val bindingImage =
                            ShowImageLayoutBinding.inflate(layoutInflater)
                        when (item.fileType) {
                            FileType.FOLDER.type -> {
                                viewModel.goToTransitionFragmentWithIntent(it, item.path)
                            }
                            FileType.PNG.type, FileType.JPG.type -> {
                                bindingImage.backShowImageView.setOnClickListener { dialog!!.dismiss() }
                                val bitmap = BitmapFactory.decodeFile(item.path)
                                bindingImage.showImageView.setImageBitmap(bitmap)
                                dialog = Dialog(activity!!, R.style.fullscreenalert)
                                dialog!!.setContentView(bindingImage.root)
                                viewModel.showDialog(activity!!, dialog!!)
                                dialog!!.setOnCancelListener {
                                    activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                }
                                dialog!!.setOnDismissListener {
                                    activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                }
                                bindingImage.executePendingBindings()
                            }
                            FileType.PDF.type -> {
                                rowBinding.favImage.visibility = View.GONE

                                bindingImage.backShowImageView.setOnClickListener {
                                    dialog!!.cancel()
                                }
                                dialog = Dialog(activity!!, R.style.fullscreenalert)

                                val showImageBitmap = BitmapUtils.getImageOfPdf(activity!!, File(item.path), 0)
                                bindingImage.showImageView.setImageBitmap(BitmapUtils.getRoundedBitmap(resources, showImageBitmap, 10f))
                                val list = BitmapUtils.pdfToBitmap(binding.root.context, File(item.path))
                                val radioGroup = RadioGroup(bindingImage.root.context)
                                radioGroup.orientation = RadioGroup.HORIZONTAL
                                radioGroup.clearCheck()
                                list.forEachIndexed { index, imageBitmap ->
                                    val drawable =
                                        BitmapUtils.getRoundedBitmap(resources, imageBitmap, 10f).toDrawable(resources)
                                    val layerDrawable = LayerDrawable(arrayOf(drawable))
                                    var params = LinearLayout.LayoutParams(100,
                                        100)
                                    params.setMargins(0, 0, 0, 0)
                                    val radioButton = RadioButton(binding.root.context)
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
                                    radioButton.background = drawable
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
                                dialog!!.setContentView(bindingImage.root)
                                viewModel.showDialog(activity!!, dialog!!)
                                dialog!!.setOnDismissListener {
                                    rowBinding.favImage.visibility = View.VISIBLE
                                    activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                }
                                dialog!!.setOnCancelListener {
                                    activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    activity!!.window.statusBarColor = activity!!.getColor(R.color.primary_color)
                                }
                            }
                        }
                        bindingImage.optionsOfFileDetail.setOnClickListener {
                            val stringList =
                                activity!!.resources.getStringArray(R.array.favorites_optionsmenu_string_array).toMutableList()
                            val drawableList = activity!!.resources.obtainTypedArray(R.array.favorites_images_array)
                            val taskTypeList = TaskType.values().filter {
                                it.value == TaskType.TOOLSTASK.value || it.value == TaskType.SHARETASK.value ||
                                        it.value == TaskType.REMOVEFAVORITETASK.value || it.value == TaskType.GOTOFOLDER.value
                            }.toList()
                            if (favoritesDao.getFavorite(item.path, item.fileName, item.fileType) != null) {
                                stringList.add(activity!!.resources.getString(R.string.remove_favorite))
                            } else {
                                stringList.add(activity!!.resources.getString(R.string.mark_as_favorite))
                            }
                            viewModel.showFavoritesBottomDialog(activity!!.supportFragmentManager,
                                this@HomeFragment,
                                dialog!!,
                                item,
                                stringList,
                                drawableList,
                                taskTypeList)
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