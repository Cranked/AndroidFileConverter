package com.cranked.androidfileconverter.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.cranked.androidcorelibrary.extension.openAppPermissionPage
import com.cranked.androidcorelibrary.ui.base.BaseDaggerActivity
import com.cranked.androidfileconverter.BuildConfig
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.ActivityMainBinding
import com.cranked.androidfileconverter.ui.model.NavigationModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.system.exitProcess

class MainActivity : BaseDaggerActivity<MainViewModel, ActivityMainBinding>(
    MainViewModel::class.java,
    R.layout.activity_main
) {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController(R.id.nav_host_fragment)
        setupBottomNavMenu(navController)
        (applicationContext as FileConvertApp).appComponent.bindMainActivity(this)
        viewModel.init(this)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.setupWithNavController(navController)
        navControllerListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) ||
                super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun initViewModel(viewModel: MainViewModel) {

    }


    override fun createLiveData() {
        this.viewModel.barIconsVisibleState.observe(
            this
        ) { t ->
            binding.root.findViewById<ImageView>(R.id.searchImageView).apply {
                visibility = if (t!!.searchState) View.VISIBLE else View.GONE
            }
            binding.root.findViewById<ImageView>(R.id.gridimageView).apply {
                visibility = if (t!!.gridState) View.VISIBLE else View.GONE
            }
            binding.root.findViewById<ImageView>(R.id.userImageView).apply {
                visibility = if (t!!.userState) View.VISIBLE else View.GONE
            }
        }
    }

    fun navControllerListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home_dest -> viewModel.setImageViewsState(NavigationModel(true, false, true))
                R.id.tools_dest -> viewModel.setImageViewsState(NavigationModel(true, true, true))
                R.id.camera_dest -> viewModel.setImageViewsState(
                    NavigationModel(
                        false,
                        false,
                        true
                    )
                )
                R.id.settings_dest -> viewModel.setImageViewsState(
                    NavigationModel(
                        false,
                        false,
                        false
                    )
                )
            }
        }
    }

    override fun onBindingClear(binding: ActivityMainBinding) {
        viewModel.onCleared()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
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
                ) { dialog, which ->
                    openAppPermissionPage()
                }
                builder.setNegativeButton(
                    getString(R.string.ok)
                ) { dialog, which ->
                    dialog.dismiss()
                    exitProcess(0)
                }
                builder.show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        onBindingClear(binding)
    }

}