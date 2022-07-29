package com.cranked.androidfileconverter.ui.task

import android.os.Bundle
import androidx.navigation.findNavController
import com.cranked.androidcorelibrary.extension.showToast
import com.cranked.androidcorelibrary.ui.base.BaseDaggerActivity
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.ActivityTaskBinding
import com.cranked.androidfileconverter.ui.main.MainActivity
import com.cranked.androidfileconverter.ui.transition.TransitionModel
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.LogManager
import com.cranked.androidfileconverter.utils.enums.TaskType
import com.cranked.androidfileconverter.utils.junk.Path
import com.cranked.androidfileconverter.utils.junk.Title
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class TaskActivity @Inject constructor() :
    BaseDaggerActivity<TaskActivityViewModel, ActivityTaskBinding>(TaskActivityViewModel::class.java, R.layout.activity_task) {
    private val app by lazy {
        (application as FileConvertApp)
    }
    private val TAG = TaskActivity::class.java.name.toString()
    lateinit var disposable: Disposable
    private var taskType: Int = 0
    private lateinit var list: List<TransitionModel>
    lateinit var path: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let {
            onBundle(it)
        }
        initRxBus()
    }

    override fun initViewModel(viewModel: TaskActivityViewModel) {
        binding.viewModel = viewModel
    }

    override fun onBundle(bundle: Bundle) {
        try {
            taskType = bundle.get(Constants.FILE_TASK_TYPE) as Int
            list = bundle.getParcelableArrayList<TransitionModel>(Constants.SELECTED_LIST) as ArrayList<TransitionModel>
        } catch (e: Exception) {
            LogManager.log(TAG, e.toString())
        }
    }

    fun initRxBus() {
        disposable = app.rxBus.toObservable().subscribe {
            when (it) {
                is Title -> {
                    binding.taskToolbar.title = it.title
                }
                is Path -> {
                    path = it.path
                }
            }
        }
    }

    override fun createListeners() {
        try {
            binding.taskToolbar.setNavigationOnClickListener {
                if (findNavController(R.id.nav_task_fragment).backQueue.size > 2) {
                    findNavController(R.id.nav_task_fragment).navigateUp()
                } else {
                    startActivity(MainActivity::class.java, true)
                }
            }
            when (taskType) {
                TaskType.COPYTASK.value -> {
                    binding.taskCancelOkLayout.okButton.text = baseContext.getString(R.string.copy)
                }
                TaskType.MOVETASK.value -> {
                    binding.taskCancelOkLayout.okButton.text = baseContext.getString(R.string.move)
                }
            }
            binding.taskCancelOkLayout.cancelButton.setOnClickListener {
                startActivity(MainActivity::class.java, true)
            }
            binding.taskCancelOkLayout.okButton.setOnClickListener {
                path.notNull {
                    if (list.size > 0)
                        when (taskType) {
                            TaskType.MOVETASK.value -> {
                                if (viewModel.moveFiles(list, path))
                                    if (list.size > 1)
                                        showToast(baseContext.getString(R.string.files_moved, list.size))
                                    else
                                        showToast(baseContext.getString(R.string.file_moved, list.size))
                            }
                            TaskType.COPYTASK.value -> {
                                if (viewModel.copyFiles(list, path))
                                    if (list.size > 1)
                                        showToast(baseContext.getString(R.string.files_copied, list.size))
                                    else
                                        showToast(baseContext.getString(R.string.file_copied, list.size))
                            }
                        }
                    startActivity(MainActivity::class.java, true)
                }
            }
        } catch (e: Exception) {
            LogManager.log(TAG, e.toString())
        }
    }

    fun Any?.notNull(f: () -> Unit) {
        if (this != null) {
            f()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onBindingClear(binding)
        disposable.dispose()
    }
}