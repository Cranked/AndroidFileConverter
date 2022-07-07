package com.cranked.androidfileconverter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.cranked.androidcorelibrary.ui.base.BaseDaggerFragment
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.FavoritesAdapter
import com.cranked.androidfileconverter.adapter.FavoritesAdapterViewModel
import com.cranked.androidfileconverter.databinding.FragmentHomeBinding
import javax.inject.Inject

@SuppressWarnings("unchecked")
class HomeFragment :
    BaseDaggerFragment<HomeFragmentViewModel, FragmentHomeBinding>(HomeFragmentViewModel::class.java) {
    @Inject
    lateinit var favoritesAdapterViewModel: FavoritesAdapterViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewDataBinding(inflater, container)
        initViewModel(viewModel)
        (this.activity!!.application as FileConvertApp).appComponent.bindHomeFragment(this)
        return binding.root
    }

    override fun getViewDataBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?
    ): FragmentHomeBinding {
        return DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, parent, false)
    }

    override fun initViewModel(viewModel: HomeFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fav_adapter = FavoritesAdapter()
        fav_adapter.setItems(favoritesAdapterViewModel.favoritesList)
        binding.favoritesRecylerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter=fav_adapter
        }
    }

}