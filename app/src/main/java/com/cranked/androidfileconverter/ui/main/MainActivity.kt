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
        navControllerListener()
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav?.setupWithNavController(navController)

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
        this.viewModel.searchVisibleState.observe(
            this
        ) { t ->
            binding.root.findViewById<ImageView>(R.id.searchImageView).apply {
                visibility = if (t) View.VISIBLE else View.GONE
            }
        }
        this.viewModel.gridVisibleState.observe(
            this
        ) { t ->
            binding.root.findViewById<ImageView>(R.id.gridimageView).apply {
                visibility = if (t) View.VISIBLE else View.GONE
            }
        }
        this.viewModel.userVisibleState.observe(
            this
        ) { t ->
            binding.root.findViewById<ImageView>(R.id.userImageView).apply {
                visibility = if (t) View.VISIBLE else View.GONE
            }
        }
    }
    fun navControllerListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home_dest -> viewModel.setImageViewsState(true, false, true)
                R.id.tools_dest -> viewModel.setImageViewsState(true, true, true)
                R.id.camera_dest -> viewModel.setImageViewsState(false, false, true)
                R.id.settings_dest -> viewModel.setImageViewsState(false, false, false)
            }
        }
    }
}