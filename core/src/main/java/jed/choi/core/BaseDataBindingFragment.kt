package jed.choi.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseDataBindingFragment<VB : ViewDataBinding, VM : ViewModel> : Fragment() {
    private var _dataBinding: VB? = null
    protected val dataBinding: VB
        get() = _dataBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _dataBinding = getBinding()
        return dataBinding.root
    }

    override fun onDestroyView() {
        _dataBinding = null
        super.onDestroyView()
    }

    abstract fun getBinding() : VB
}