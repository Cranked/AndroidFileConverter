package com.cranked.androidfileconverter.ui.languages

import android.content.Context
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cranked.androidcorelibrary.adapter.BaseViewBindingRecyclerViewAdapter
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.adapter.language.LanguageAdapter
import com.cranked.androidfileconverter.databinding.RowLanguageItemBinding
import javax.inject.Inject

class LanguageActivityViewModel @Inject constructor() : BaseViewModel() {

    val selectedLanguage = MutableLiveData<String>()
    fun setToolbar(toolbar: Toolbar, title: String) {
        toolbar.title = title
    }

    fun toListLanguageModel(
        hashMap: HashMap<String, String>,
        selectedLang: String,
    ): List<LanguageModel> {
        return hashMap.keys.map {
            if (it.equals(selectedLang))
                LanguageModel(hashMap.get(it).toString(), true, it)
            else
                LanguageModel(hashMap.get(it).toString(), false, it)
        }.toList()
    }


    fun setAdapter(
        context: Context,
        recylerView: RecyclerView,
        languageAdapter: LanguageAdapter,
        list: MutableList<LanguageModel>,
    ) {
        languageAdapter.apply {
            setItems(list)
            setListener(object :
                BaseViewBindingRecyclerViewAdapter.ClickListener<LanguageModel, RowLanguageItemBinding> {
                override fun onItemClick(
                    item: LanguageModel,
                    position: Int,
                    rowBinding: RowLanguageItemBinding,
                ) {
                    rowBinding.languageLinearLayout.setOnClickListener {
                        if (!rowBinding.languageRadioButton.isChecked) {
                            sendSelectedLanguage(item.shortName)
                        }
                    }
                    rowBinding.languageRadioButton.setOnClickListener {
                        sendSelectedLanguage(item.shortName)
                    }
                }
            })
        }
        recylerView.apply {
            adapter = languageAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun sendSelectedLanguage(selectedLang: String) {
        selectedLanguage.postValue(selectedLang)
    }
}