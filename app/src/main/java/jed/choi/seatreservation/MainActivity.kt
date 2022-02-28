package jed.choi.seatreservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint
import jed.choi.ui_core.BaseDataBindingActivity
import jed.choi.ui_core.ScrollableToTop
import jed.choi.seatreservation.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : BaseDataBindingActivity<ActivityMainBinding, MainViewModel>() {
    override val viewModel: MainViewModel by viewModels()
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        // This needed to be here before super.onCreate()
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(dataBinding.root)
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this

        setupUi()
    }

    private fun setupUi() {
        navHostFragment = dataBinding.fragmentContainer.getFragment()
        navController = navHostFragment.navController

        setupBottomNavigation()
        setupMySeatPanel()
    }

    private fun setupMySeatPanel() {
        dataBinding.apply {
            panelEntireLayout.addPanelSlideListener(object: SlidingUpPanelLayout.PanelSlideListener{
                override fun onPanelSlide(panel: View?, slideOffset: Float) {
                    when {
                        slideOffset < 0.0f -> { // hide or reveal
//                            layoutCollapsed.alpha = 1 + offset
//                            layoutExpanded.root.alpha = -offset
                        }
                        slideOffset == 0.0f -> {
//                            layoutCollapsed.alpha = 1.0f
//                            layoutExpanded.root.visibility = View.GONE
//                            layoutExpanded.root.alpha = 0.0f
                            bottomNavigation.translationY = 0.0f
                        }
                        slideOffset == 1.0f -> {
//                            layoutCollapsed.visibility = View.GONE
//                            layoutCollapsed.alpha = 0.0f
//                            layoutExpanded.root.alpha = 1.0f
                            bottomNavigation.translationY = 1.0f * bottomNavigation.height
                        }
                        0.0f < slideOffset && slideOffset < 1.0f -> {
                            bottomNavigation.translationY = slideOffset * bottomNavigation.height
//                            if (layoutCollapsed.visibility != View.VISIBLE) layoutCollapsed.visibility = View.VISIBLE
//                            if (layoutExpanded.root.visibility != View.VISIBLE) layoutExpanded.root.visibility = View.VISIBLE
//                            layoutCollapsed.alpha = 1 - slideOffset
//                            layoutExpanded.root.alpha = slideOffset
                        }
                    }
                }

                override fun onPanelStateChanged(
                    panel: View?,
                    previousState: SlidingUpPanelLayout.PanelState?,
                    newState: SlidingUpPanelLayout.PanelState?
                ) {
                }

            })
        }

    }


    private fun setupBottomNavigation() {
        dataBinding.bottomNavigation.setupWithNavController(navController)

        dataBinding.bottomNavigation.setOnItemReselectedListener {
            (navHostFragment.childFragmentManager.fragments.firstOrNull() as? ScrollableToTop)?.scrollToTop()
        }
    }

    override fun getBinding(inflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflater)

}


object BindingAdapter {
    @JvmStatic
    @BindingAdapter("slideState")
    fun setSlideState(view: SlidingUpPanelLayout, newValue: SlidingUpPanelLayout.PanelState?) {
        // Important to break potential infinite loops.
        val old = view.panelState
        if (old != newValue && newValue != null) {
            view.panelState = newValue
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["slideStateAttrChanged"], requireAll = false)
    fun bindSlideState(view: SlidingUpPanelLayout, listener: InverseBindingListener) {
        val newListener =
            object : SlidingUpPanelLayout.PanelSlideListener {
                override fun onPanelSlide(panel: View?, slideOffset: Float) {
                }

                override fun onPanelStateChanged(
                    panel: View?,
                    previousState: SlidingUpPanelLayout.PanelState?,
                    newState: SlidingUpPanelLayout.PanelState?
                ) {
                    if (previousState != newState && newState != SlidingUpPanelLayout.PanelState.DRAGGING) {
                        listener.onChange()
                    }
                }
            }
        view.addPanelSlideListener(newListener)
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "slideState", event = "slideStateAttrChanged")
    fun getSlideState(view: SlidingUpPanelLayout): SlidingUpPanelLayout.PanelState {
        return view.panelState
    }
}