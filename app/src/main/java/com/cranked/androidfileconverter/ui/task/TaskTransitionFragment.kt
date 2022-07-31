package com.cranked.androidfileconverter.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidcorelibrary.utility.FileUtils
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.task.TaskListAdapter
import com.cranked.androidfileconverter.data.database.dao.FavoritesDao
import com.cranked.androidfileconverter.databinding.FragmentTaskTransitionBinding
import com.cranked.androidfileconverter.ui.transition.toTransitionList
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.junk.Path
import com.cranked.androidfileconverter.utils.junk.Title
import javax.inject.Inject

class TaskTransitionFragment @Inject constructor( private val favoritesDao: FavoritesDao) :
    BaseDaggerFragment<TaskTransitionFragmentViewModel, FragmentTaskTransitionBinding>(TaskTransitionFragmentViewModel::class.java) {
    lateinit var path: String
    val app by lazy {
        activity!!.application as FileConvertApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        arguments?.let {
            onBundle(it)
        }
        return binding.root
    }

    override fun getViewDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): FragmentTaskTransitionBinding {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_task_transition, parent, false)
    }

    override fun initViewModel(viewModel: TaskTransitionFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun onBundle(bundle: Bundle) {
        path = bundle.getString(Constants.DESTINATION_PATH_ACTION).toString()
        viewModel.sendPath(path)
    }

    override fun createLiveData(viewLifecycleOwner: LifecycleOwner) {
        viewModel.getPathMutableLiveData().observe(viewLifecycleOwner) {
            val title = it.split("/").filter { it.isNotEmpty() }.last()
            app.rxBus.send(Title(title))
            app.rxBus.send(Path(it))
            val list = FileUtils.getFolderFiles(it, 1, 1).filter { it.isDirectory }.toTransitionList(favoritesDao)
            viewModel.setAdapter(context!!, binding.taskTransitionRecylerView, TaskListAdapter(), list.toMutableList())
        }
    }

    override fun createListeners() {
        activity!!.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.root.findNavController().navigateUp()
            }
        })
    }
}