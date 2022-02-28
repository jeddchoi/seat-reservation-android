package jed.choi.setting

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import jed.choi.core.FeatureStartFragment
import jed.choi.setting.databinding.SettingFragmentBinding

class SettingFragment : FeatureStartFragment<SettingFragmentBinding, SettingViewModel>() {
    override val viewModel: SettingViewModel by viewModels()

    companion object {
        fun newInstance() = SettingFragment()
    }



    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SettingFragmentBinding = SettingFragmentBinding.inflate(inflater, container, false)

    override fun getNavController(): NavController = dataBinding.settingFragmentContainer.getFragment<NavHostFragment>().navController

    override fun scrollToTop() {
        Log.i(TAG, "scrollToTop")
    }


    override fun setupToolbar() {
        val appBarConfiguration = AppBarConfiguration(featureNavController.graph)
        dataBinding.toolbarSetting.setupWithNavController(featureNavController, appBarConfiguration)
    }


}