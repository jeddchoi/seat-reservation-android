package jed.choi.ui_seat

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import jed.choi.ui_core.FeatureContainerFragment
import jed.choi.ui_seat.databinding.SeatContainerFragmentBinding

class SeatContainerFragment : FeatureContainerFragment<SeatContainerFragmentBinding, SeatContainerViewModel>() {

    companion object {
        fun newInstance() = SeatContainerFragment()
    }

    override val viewModel: SeatContainerViewModel by viewModels()




    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): SeatContainerFragmentBinding =
        SeatContainerFragmentBinding.inflate(inflater, container, false)

    override fun getNavController(): NavController = dataBinding.fragmentContainerSeat.getFragment<NavHostFragment>().navController

    override fun scrollToTop() {
        Log.i(TAG, "scrollToTop")
    }


    override fun setupToolbar() {
        val appBarConfiguration = AppBarConfiguration(featureNavController.graph)
        dataBinding.toolbarFeature.setupWithNavController(featureNavController, appBarConfiguration)
    }
}