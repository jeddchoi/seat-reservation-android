package jed.choi.ui_seat

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import jed.choi.ui_core.FeatureStartFragment
import jed.choi.ui_seat.databinding.SeatFragmentBinding

class SeatFragment : FeatureStartFragment<SeatFragmentBinding, SeatViewModel>() {

    companion object {
        fun newInstance() = SeatFragment()
    }

    override val viewModel: SeatViewModel by viewModels()


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): SeatFragmentBinding =
        SeatFragmentBinding.inflate(inflater, container, false)

    override fun scrollToTop() {
        Log.i(TAG, "scrollToTop")
    }
}