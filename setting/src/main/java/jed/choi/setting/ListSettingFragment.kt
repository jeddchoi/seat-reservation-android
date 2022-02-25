package jed.choi.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import jed.choi.core.BaseViewBindingFragment
import jed.choi.setting.databinding.ListSettingFragmentBinding

class ListSettingFragment : BaseViewBindingFragment<ListSettingFragmentBinding, ListSettingViewModel>() {

    companion object {
        fun newInstance() = ListSettingFragment()
    }
    override val viewModel: ListSettingViewModel by viewModels()


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ListSettingFragmentBinding = ListSettingFragmentBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.goDetailButton.setOnClickListener {
            findNavController().navigate(R.id.action_listSettingFragment_to_detailSettingFragment)
        }
    }
}