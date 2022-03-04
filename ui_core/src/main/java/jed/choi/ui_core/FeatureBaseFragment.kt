package jed.choi.ui_core

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

/**
 * Bottom Navigation Feature base fragment
 * which is sub of [FeatureContainerFragment]
 */
abstract class FeatureBaseFragment<VB : ViewDataBinding, VM : ViewModel> : BaseDataBindingFragment<VB, VM>(), ScrollableToTop {

    override fun setupUi() {
        // TODO: remove this
    }

    override fun observeViewModel() {
        // TODO: remove this
   }
}