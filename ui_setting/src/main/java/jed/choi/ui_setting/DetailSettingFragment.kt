package jed.choi.ui_setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jed.choi.ui_core.FeatureBaseFragment
import jed.choi.ui_core.di.HiltExample
import jed.choi.ui_setting.databinding.DetailSettingFragmentBinding
import javax.inject.Inject


@AndroidEntryPoint
class DetailSettingFragment : FeatureBaseFragment<DetailSettingFragmentBinding, DetailSettingViewModel>() {

    @Inject
    lateinit var example: HiltExample

    companion object {
        fun newInstance() = DetailSettingFragment()
    }
    override val viewModel: DetailSettingViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.i(TAG, "onCreateView: ${example.counter}")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DetailSettingFragmentBinding = DetailSettingFragmentBinding.inflate(inflater, container, false)

    override fun scrollToTop() {
        TODO("Not yet implemented")
    }

}