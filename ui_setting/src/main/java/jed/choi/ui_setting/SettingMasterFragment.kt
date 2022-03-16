package jed.choi.ui_setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jed.choi.ui_core.FeatureBaseFragment
import jed.choi.ui_core.di.HiltExample
import jed.choi.ui_setting.databinding.SettingMasterFragmentBinding
import javax.inject.Inject

@AndroidEntryPoint
class SettingMasterFragment :
    FeatureBaseFragment<SettingMasterFragmentBinding, SettingMasterViewModel>() {

    @Inject
    lateinit var example: HiltExample

    companion object {
        fun newInstance() = SettingMasterFragment()
    }

    override val viewModel: SettingMasterViewModel by viewModels()


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SettingMasterFragmentBinding = SettingMasterFragmentBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.goDetailButton.setOnClickListener {
            findNavController().navigate(SettingMasterFragmentDirections.actionMasterFragmentToDetailFragment())
        }
    }

    override fun scrollToTop() {
//        TODO("Not yet implemented")
    }
}