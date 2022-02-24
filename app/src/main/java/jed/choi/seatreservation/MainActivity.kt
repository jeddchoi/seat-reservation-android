package jed.choi.seatreservation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import jed.choi.core.BaseViewBindingActivity
import jed.choi.core.ScrollableToTop
import jed.choi.seatreservation.databinding.ActivityMainBinding

class MainActivity : BaseViewBindingActivity<ActivityMainBinding, MainViewModel>() {
    override val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        // This needed to be here before super.onCreate()
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navHostFragment = viewBinding.fragmentContainer.getFragment<NavHostFragment>()
        val navController = navHostFragment.navController
        viewBinding.bottomNavigation.setupWithNavController(navController)

        viewBinding.bottomNavigation.setOnItemReselectedListener {
            (navHostFragment.childFragmentManager.fragments.firstOrNull() as? ScrollableToTop)?.scrollToTop()
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }


    override fun getBinding(inflater: LayoutInflater): ActivityMainBinding = ActivityMainBinding.inflate(inflater)
}