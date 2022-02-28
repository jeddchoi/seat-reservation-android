package jed.choi.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import jed.choi.core.FeatureStartFragment
import jed.choi.history.databinding.HistoryFragmentBinding

class HistoryFragment : FeatureStartFragment<HistoryFragmentBinding, HistoryViewModel>() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override val viewModel: HistoryViewModel by viewModels()


    override fun scrollToTop() {
        TODO("Not yet implemented")
    }


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HistoryFragmentBinding = HistoryFragmentBinding.inflate(inflater, container, false)

}