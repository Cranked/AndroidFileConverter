package com.cranked.androidfileconverter.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.cranked.androidcorelibrary.viewmodel.BaseViewModel
import com.cranked.androidfileconverter.FileConvertApp
import com.cranked.androidfileconverter.ui.languages.LanguagesActivity
import com.cranked.androidfileconverter.utils.Constants
import javax.inject.Inject

class SettingsFragmentViewModel @Inject constructor(
    private val mContext: Context,
) : BaseViewModel() {
    val app = (mContext as FileConvertApp)
    val selectedLanguage = Constants.languagesKeyValue.get(app.getLanguage())

    fun goToLanguagesActivity(activity: Activity) {
        val intent = Intent(mContext, LanguagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
        activity.finish()
    }
}