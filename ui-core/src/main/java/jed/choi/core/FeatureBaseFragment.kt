package jed.choi.core

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class FeatureBaseFragment<VB : ViewDataBinding, VM : ViewModel> : BaseDataBindingFragment<VB, VM>(), ScrollableToTop {

}