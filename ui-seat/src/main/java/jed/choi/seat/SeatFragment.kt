package jed.choi.seat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import jed.choi.core.FeatureStartFragment
import jed.choi.seat.databinding.SeatFragmentBinding

class SeatFragment : FeatureStartFragment<SeatFragmentBinding, SeatViewModel>() {

    companion object {
        fun newInstance() = SeatFragment()
    }

    override val viewModel: SeatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): SeatFragmentBinding =
        SeatFragmentBinding.inflate(inflater, container, false)

    override fun scrollToTop() {
        Log.i(TAG, "scrollToTop")
    }
}