package jed.choi.ui_core

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

/**
 * Bottom Navigation Feature starting fragment
 * which may hold fragmentContainerView
 */
abstract class FeatureStartFragment<VB : ViewDataBinding, VM : ViewModel> : BaseDataBindingFragment<VB, VM>(), ScrollableToTop {
    private var _featureNavController: NavController? = null
    protected val featureNavController: NavController
        get() = _featureNavController!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _featureNavController = getNavController()
        setupToolbar()
    }

    override fun onDestroyView() {
        _featureNavController = null
        super.onDestroyView()
    }


    // TODO: make these functions abstract
    open fun getNavController(): NavController = findNavController()
    open fun setupToolbar() {}


    override fun setupUi() {
        // TODO: remove this
    }

    override fun observeViewModel() {
        // TODO: remove this
    }
}