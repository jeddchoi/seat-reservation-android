package jed.choi.ui_setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jed.choi.ui_core.FeatureBaseFragment
import jed.choi.ui_core.di.HiltExample
import jed.choi.ui_setting.databinding.SettingDetailFragmentBinding
import javax.inject.Inject


@AndroidEntryPoint
class SettingDetailFragment : FeatureBaseFragment<SettingDetailFragmentBinding, SettingDetailViewModel>() {

    @Inject
    lateinit var example: HiltExample

    companion object {
        fun newInstance() = SettingDetailFragment()
    }
    override val viewModel: SettingDetailViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SettingDetailFragmentBinding = SettingDetailFragmentBinding.inflate(inflater, container, false)

    override fun scrollToTop() {
//        TODO("Not yet implemented")
    }

}