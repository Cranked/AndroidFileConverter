package com.cranked.androidfileconverter.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.cranked.androidcorelibrary.ui.base.BaseActivity
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.databinding.ActivityMainBinding
import com.cranked.androidfileconverter.ui.model.NavigationModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(
    MainViewModel::class.java,
    R.layout.activity_main
) {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val appComponent = DaggerAppComponent.builder().application(application)!!.build()
//        appComponent!!.inject(this)
        navController = findNavController(R.id.nav_host_fragment)
        setupBottomNavMenu(navController)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
    override fun onDestroy() {
        super.onDestroy()
        onBindingClear(binding)
    }
}