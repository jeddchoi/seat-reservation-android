package jed.choi.ui_core

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

/**
 * Bottom Navigation Feature base fragment
 * which is sub of [FeatureStartFragment]
 */
abstract class FeatureBaseFragment<VB : ViewDataBinding, VM : ViewModel> : BaseDataBindingFragment<VB, VM>(), ScrollableToTop {

}