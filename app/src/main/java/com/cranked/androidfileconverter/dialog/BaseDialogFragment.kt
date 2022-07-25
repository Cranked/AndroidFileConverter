package com.cranked.androidfileconverter.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment<VDB : ViewDataBinding>(
    private val layoutId: Int,
) :
    DialogFragment(layoutId) {
    private lateinit var binding: VDB
    protected lateinit var dialog: DialogFragment
    protected abstract fun onBindingCreate(binding: VDB)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        hideKeyboard()
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val inf = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inf, layoutId, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindingCreate(binding)
        dialog = this
        view.parent?.let {
            (it as View).setBackgroundColor(Color.TRANSPARENT)
        }
        this.isCancelable = false
    }

    protected fun hideKeyboard() {
        activity?.currentFocus?.let {
            val inputManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }


    @JvmName("getDeleteDialog")
    fun getBaseDialog() = this.dialog

}