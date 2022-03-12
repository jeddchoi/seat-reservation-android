package jed.choi.ui_seat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jed.choi.ui_core.Authorizable
import jed.choi.ui_core.FeatureBaseFragment
import jed.choi.ui_seat.databinding.SeatMasterFragmentBinding

@AndroidEntryPoint
class SeatMasterFragment : FeatureBaseFragment<SeatMasterFragmentBinding, SeatMasterViewModel>() {
    companion object {
        fun newInstance() = SeatMasterFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.viewModel = viewModel
    }

    override val viewModel: SeatMasterViewModel by viewModels()

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SeatMasterFragmentBinding = SeatMasterFragmentBinding.inflate(inflater, container, false)

    override fun setupUi() {
        super.setupUi()
        dataBinding.buttonTestNavigation.setOnClickListener {
            (requireActivity() as Authorizable).navigateToPlaceHolder()
        }

    }

    override fun scrollToTop() {
//        TODO("Not yet implemented")
    }
}