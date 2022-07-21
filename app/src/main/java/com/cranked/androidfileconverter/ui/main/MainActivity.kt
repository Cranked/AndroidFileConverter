package com.cranked.androidfileconverter.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.cranked.androidcorelibrary.extension.openAppPermissionPage
import com.cranked.androidcorelibrary.ui.base.BaseDaggerActivity
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.ActivityMainBinding
import com.cranked.androidfileconverter.ui.model.NavigationModel
import com.cranked.androidfileconverter.utils.junk.ToolbarState
import io.reactivex.rxjava3.disposables.Disposable
import kotlin.system.exitProcess

class MainActivity : BaseDaggerActivity<MainViewModel, ActivityMainBinding>(
    MainViewModel::class.java,
    R.layout.activity_main
) {
    lateinit var navController: NavController
    lateinit var disposable: Disposable
    private val app by lazy {
        (application as FileConvertApp)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController(R.id.nav_host_fragment)
        setupBottomNavMenu(navController)
        app.appComponent.bindMainActivity(this)
        viewModel.init(this)
        viewModel.setupToolBar(this, binding.toolbar, true)
        initRxBus()
    }

    private fun setupBottomNavMenu(navController: NavController) {
        binding.bottomNav.setupWithNavController(navController)
        navControllerListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) ||
                super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun initViewModel(viewModel: MainViewModel) {
    }


    override fun createLiveData() {
        this.viewModel.barIconsVisibleState.observe(
            this
        ) { t ->
            binding.toolbar.menu.getItem(0).isVisible = t!!.searchState
            binding.toolbar.menu.getItem(1).isVisible = t.gridState
            binding.toolbar.menu.getItem(2).isVisible = t.userState
        }
    }

    fun navControllerListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home_dest -> {
                    viewModel.setImageViewsState(NavigationModel(true, false, true))
                }
                R.id.tools_dest -> viewModel.setImageViewsState(NavigationModel(true, true, true))
                R.id.camera_dest -> viewModel.setImageViewsState(
                    NavigationModel(
                        searchState = false,
                        gridState = false,
                        userState = true
                    )
                )
                R.id.settings_dest -> viewModel.setImageViewsState(
                    NavigationModel(
                        searchState = false,
                        gridState = false,
                        userState = false
                    )
                )
            }
        }
    }

    override fun onBindingClear(binding: ActivityMainBinding) {
        viewModel.onCleared()
    }

    fun initRxBus() {
        disposable = app.rxBus.toObservable().subscribe {
            when (it) {
                is ToolbarState -> viewModel.setupToolBar(this, binding.toolbar, it.state)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        viewModel.permissionTemp.onRequestPermissionsResult(this, requestCode, grantResults)
            .onGranted {
                viewModel.createFileConverterFolder()
            }.onDenied {
                viewModel.permissionTemp.request(this)
            }.onNeverAskAgain {
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                builder.setMessage(getString(R.string.necessary_permissions_description))
                builder.setPositiveButton(
                    getString(R.string.settings)
                ) { _, _ ->
                    openAppPermissionPage()
                }
                builder.setNegativeButton(
                    getString(R.string.ok)
                ) { dialog, _ ->
                    dialog.dismiss()
                    exitProcess(0)
                }
                builder.show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        onBindingClear(binding)
        disposable.dispose()
    }
}