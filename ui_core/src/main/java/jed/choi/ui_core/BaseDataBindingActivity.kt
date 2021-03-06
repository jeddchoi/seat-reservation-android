package jed.choi.ui_core

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseDataBindingActivity<VB : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {
    protected val TAG = this::class.simpleName
    protected lateinit var dataBinding: VB

    protected abstract val viewModel: VM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = getBinding(layoutInflater)
        setupUi()
        dataBinding.lifecycleOwner = this
        observeViewModel()
    }

    abstract fun getBinding(inflater: LayoutInflater): VB
    abstract fun setupUi()
    abstract fun observeViewModel()
}