package jed.choi.core

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {
    protected val TAG = this::class.simpleName
    protected lateinit var viewBinding: VB

    protected abstract val viewModel: VM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = getBinding(layoutInflater)
    }


    abstract fun getBinding(inflater: LayoutInflater): VB
}