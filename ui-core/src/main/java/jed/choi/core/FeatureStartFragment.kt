package jed.choi.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

abstract class FeatureStartFragment<VB : ViewDataBinding, VM : ViewModel> : BaseDataBindingFragment<VB, VM>(), ScrollableToTop {
    private var _featureNavController: NavController? = null
    protected val featureNavController: NavController
        get() = _featureNavController!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _featureNavController = getNavController()
        setupToolbar()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        _featureNavController = null
        super.onDestroyView()
    }


    // TODO: make these functions abstract
    open fun getNavController(): NavController = findNavController()
    open fun setupToolbar() {}
}