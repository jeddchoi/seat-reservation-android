package jed.choi.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import jed.choi.core.BaseViewBindingFragment
import jed.choi.setting.databinding.DetailSettingFragmentBinding

class DetailSettingFragment : BaseViewBindingFragment<DetailSettingFragmentBinding, DetailSettingViewModel>() {

    companion object {
        fun newInstance() = DetailSettingFragment()
    }
    override val viewModel: DetailSettingViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DetailSettingFragmentBinding = DetailSettingFragmentBinding.inflate(inflater, container, false)

}