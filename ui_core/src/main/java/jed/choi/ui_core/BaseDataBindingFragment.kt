package jed.choi.ui_core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseDataBindingFragment<VB : ViewDataBinding, VM : ViewModel> : Fragment() {
    protected val TAG = this::class.simpleName

    private var _dataBinding: VB? = null
    protected val dataBinding: VB
        get() = _dataBinding!!

    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _dataBinding = getBinding(inflater, container)
        dataBinding.lifecycleOwner = viewLifecycleOwner

        return dataBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeViewModel()
    }

    override fun onDestroyView() {
        _dataBinding = null
        super.onDestroyView()
    }

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun setupUi()
    abstract fun observeViewModel()
}