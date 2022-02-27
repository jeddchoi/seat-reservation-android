package jed.choi.seatreservation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import jed.choi.core.BaseViewBindingActivity
import jed.choi.core.ScrollableToTop
import jed.choi.seatreservation.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<ActivityMainBinding, MainViewModel>() {
    override val viewModel: MainViewModel by viewModels()
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        // This needed to be here before super.onCreate()
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        setupUi()
    }

    private fun setupUi() {
        navHostFragment = viewBinding.fragmentContainer.getFragment()
        navController = navHostFragment.navController

        setupBottomNavigation()
    }



    private fun setupBottomNavigation() {
        viewBinding.bottomNavigation.setupWithNavController(navController)

        viewBinding.bottomNavigation.setOnItemReselectedListener {
            (navHostFragment.childFragmentManager.fragments.firstOrNull() as? ScrollableToTop)?.scrollToTop()
        }
    }

    override fun getBinding(inflater: LayoutInflater): ActivityMainBinding = ActivityMainBinding.inflate(inflater)

}