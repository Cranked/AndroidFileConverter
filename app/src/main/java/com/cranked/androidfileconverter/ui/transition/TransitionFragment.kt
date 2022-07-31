package com.cranked.androidfileconverter.ui.transition

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.BuildConfig
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.transition.TransitionGridAdapter
import com.cranked.androidfileconverter.adapter.transition.TransitionListAdapter
import com.cranked.androidfileconverter.databinding.FragmentTransitionBinding
import com.cranked.androidfileconverter.ui.main.MainViewModel
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.enums.LayoutState
import com.cranked.androidfileconverter.utils.junk.ToolbarState
import java.io.File
import javax.inject.Inject

class TransitionFragment @Inject constructor() :
    BaseDaggerFragment<TransitionFragmentViewModel, FragmentTransitionBinding>(
        TransitionFragmentViewModel::class.java
    ) {
    private val TAG = TransitionFragment::class.toString()
    private val app by lazy {
        activity!!.application as FileConvertApp
    }
    private val spinnerList by lazy {
        listOf(
            context!!.getString(R.string.sorting_a_to_z),
            context!!.getString(R.string.sorting_z_to_a),
            context!!.getString(R.string.sorting_newest_items),
            context!!.getString(R.string.sorting_oldest_items),
        )
    }

    @Inject
    lateinit var mainViewModel: MainViewModel
    lateinit var transitionListAdapter: TransitionListAdapter
    lateinit var transitionGridAdapter: TransitionGridAdapter

    lateinit var path: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        transitionListAdapter = TransitionListAdapter(viewModel)
        transitionGridAdapter = TransitionGridAdapter(viewModel)
        arguments?.let {
            onBundle(it)
        }
        app.rxBus.send(ToolbarState(false))
        viewModel.init(binding, this, activity!!, app, path, spinnerList)

        activity!!.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.backStack(binding.transitionToolbarMenu.backImageView)
                viewModel.sendLongListenerActivated(false)
                viewModel.getSelectedRowList().clear()
                setLayoutSate(app.getLayoutState(),
                    viewModel.getFilesFromPath(path, app.getFilterState()))
                binding.createFolderButton.visibility = View.VISIBLE
            }
        })
        return binding.root
    }

    override fun onBundle(bundle: Bundle) {
        path = bundle.getString(Constants.DESTINATION_PATH_ACTION).toString()
        viewModel.sendPath(path)
    }

    override fun getViewDataBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
    ): FragmentTransitionBinding {
        return DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_transition,
            parent,
            false
        )
    }


    override fun createLiveData(viewLifecycleOwner: LifecycleOwner) {
        viewModel.getFolderPathMutableLiveData().observe(viewLifecycleOwner)
        {
            val list = viewModel.getFilesFromPath(it!!, app.getFilterState())
            viewModel.sendNoDataState(list.size > 0)
            setLayoutSate(app.getLayoutState(), list)
        }
        viewModel.getNoDataStateMutableLiveData().observe(viewLifecycleOwner) {
            binding.emptyFolder.visibility = if (it) View.GONE else View.VISIBLE
            binding.noDataImageView.visibility = if (it) View.GONE else View.VISIBLE
            binding.emptyFolderDescription.visibility = if (it) View.GONE else View.VISIBLE
        }
        viewModel.getFilterStateMutableLiveData().observe(viewLifecycleOwner) {
            app.setFilterState(it)
            val list = viewModel.getFilesFromPath(path, app.getFilterState())
            transitionGridAdapter.setItems(list)
            transitionListAdapter.setItems(list)
        }
        viewModel.getItemsChangedStateMutableLiveData().observe(viewLifecycleOwner) {
            val list = viewModel.getFilesFromPath(path, app.getFilterState())
            transitionGridAdapter.setItems(list)
            transitionListAdapter.setItems(list)
        }
        viewModel.getLongListenerActivatedMutableLiveData().observe(viewLifecycleOwner) {
            viewModel.setViewVisibility(binding.multipleSelectionMenu.root, it)
            viewModel.setViewVisibility(binding.transitionToolbarMenu.root, !it)
            val list = viewModel.getFilesFromPath(path, app.getFilterState())
            transitionGridAdapter.setItems(list)
            transitionListAdapter.setItems(list)
            binding.createFolderButton.visibility = if (!it) View.VISIBLE else View.GONE
        }
        viewModel.getSelectedRowSizeMutableLiveData().observe(viewLifecycleOwner) {
            if (it == 0) {
                binding.createFolderButton.visibility = View.VISIBLE
                viewModel.sendLongListenerActivated(false)
            } else
                binding.createFolderButton.visibility = View.INVISIBLE
            binding.multipleSelectionMenu.selectedItemsMultiple.text = it.toString()
        }
        viewModel.getShareFilesMutableLiveData().observe(viewLifecycleOwner) {
            val uriArrayList = arrayListOf<Uri>()
            it.forEach {
                uriArrayList += FileProvider.getUriForFile(this.context!!, BuildConfig.APPLICATION_ID + ".provider", File(it.filePath))
            }
            val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.setType("*/*")
            intent.putExtra(Intent.EXTRA_STREAM, uriArrayList)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
            startActivity(intent)
            viewModel.getSelectedRowList().clear()
        }
    }

    fun setLayoutSate(state: Int, list: MutableList<TransitionModel>) {
        try {
            when (state) {
                LayoutState.LIST_LAYOUT.value -> {
                    binding.transitionToolbarMenu.layoutImageView.setImageDrawable(context!!.getDrawable(
                        R.drawable.icon_grid))
                    transitionListAdapter = viewModel.setAdapter(context!!,
                        binding.transitionRecylerView,
                        transitionListAdapter,
                        list)
                }
                LayoutState.GRID_LAYOUT.value -> {
                    binding.transitionToolbarMenu.layoutImageView.setImageDrawable(context!!.getDrawable(
                        R.drawable.icon_list))
                    transitionGridAdapter = viewModel.setAdapter(context!!,
                        binding.transitionRecylerView,
                        transitionGridAdapter,
                        list)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    override fun initViewModel(viewModel: TransitionFragmentViewModel) {
        binding.viewModel = viewModel
    }

}