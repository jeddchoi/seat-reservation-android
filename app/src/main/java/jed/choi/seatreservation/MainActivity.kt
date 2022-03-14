package jed.choi.seatreservation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jed.choi.seatreservation.databinding.ActivityMainBinding
import jed.choi.ui_core.Authorizable
import jed.choi.ui_core.BaseViewBindingActivity


@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<ActivityMainBinding, MainViewModel>(), Authorizable {
    override val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        // This needed to be here before super.onCreate()
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        setupUi()
    }


    override fun setupUi() {
    }

    override fun getBinding(inflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflater)

    override fun observeViewModel() {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.CREATED) {
//                launch {
//                }
//            }
//        }
    }

    override fun navigateToPlaceHolder() {

        viewBinding.mainContainer.findNavController()
            .navigate(BottomNavFragmentDirections.actionBottomNavFragmentToPlaceHolderFragment())
    }


}
