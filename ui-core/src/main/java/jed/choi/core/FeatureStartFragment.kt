package jed.choi.core

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class FeatureStartFragment<VB : ViewDataBinding, VM : ViewModel> : BaseDataBindingFragment<VB, VM>(), ScrollableToTop {

}