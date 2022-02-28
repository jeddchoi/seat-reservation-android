package jed.choi.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import jed.choi.core.FeatureStartFragment
import jed.choi.dashboard.databinding.DashboardFragmentBinding

class DashboardFragment : FeatureStartFragment<DashboardFragmentBinding, DashboardViewModel>() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    override val viewModel: DashboardViewModel by viewModels()

    override fun scrollToTop() {
        TODO("Not yet implemented")
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DashboardFragmentBinding = DashboardFragmentBinding.inflate(inflater, container, false)



}