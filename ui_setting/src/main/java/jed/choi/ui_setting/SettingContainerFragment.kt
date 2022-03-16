package jed.choi.ui_setting

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import jed.choi.ui_core.FeatureContainerFragment
import jed.choi.ui_setting.databinding.SettingContainerFragmentBinding

class SettingContainerFragment : FeatureContainerFragment<SettingContainerFragmentBinding, SettingContainerViewModel>() {
    override val viewModel: SettingContainerViewModel by viewModels()

    companion object {
        fun newInstance() = SettingContainerFragment()
    }



    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SettingContainerFragmentBinding = SettingContainerFragmentBinding.inflate(inflater, container, false)

    override fun getNavController(): NavController = dataBinding.fragmentContainerSetting.getFragment<NavHostFragment>().navController

    override fun scrollToTop() {
    }


    override fun setupToolbar() {
        val appBarConfiguration = AppBarConfiguration(featureNavController.graph)
        dataBinding.toolbarFeature.setupWithNavController(featureNavController, appBarConfiguration)
    }


}