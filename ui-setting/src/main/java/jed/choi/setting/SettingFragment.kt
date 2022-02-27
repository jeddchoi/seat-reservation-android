package jed.choi.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import jed.choi.core.BaseViewBindingFragment
import jed.choi.setting.databinding.SettingFragmentBinding

class SettingFragment : BaseViewBindingFragment<SettingFragmentBinding, SettingViewModel>() {
    override val viewModel: SettingViewModel by viewModels()

    companion object {
        fun newInstance() = SettingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SettingFragmentBinding = SettingFragmentBinding.inflate(inflater, container, false)

    override fun scrollToTop() {
        TODO("Not yet implemented")
    }

}