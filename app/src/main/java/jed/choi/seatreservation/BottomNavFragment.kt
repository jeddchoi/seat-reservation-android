package jed.choi.seatreservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint
import jed.choi.seatreservation.databinding.BottomNavFragmentBinding
import jed.choi.seatreservation.databinding.PanelMySeatCollapsedBinding
import jed.choi.seatreservation.databinding.PanelMySeatExpandedBinding
import jed.choi.seatreservation.model.showMyStatePanel
import jed.choi.ui_core.BaseDataBindingFragment
import jed.choi.ui_core.ScrollableToTop
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomNavFragment : BaseDataBindingFragment<BottomNavFragmentBinding, BottomNavViewModel>() {
    override val viewModel: BottomNavViewModel by viewModels()
    private val navHostFragment: NavHostFragment
        get() = dataBinding.fragmentContainer.getFragment()
    private val panelExpanded: PanelMySeatExpandedBinding
        get() = dataBinding.panelMySeatExpanded
    private val panelCollapsed: PanelMySeatCollapsedBinding
        get() = dataBinding.panelMySeatCollapsed

    private lateinit var snackbar: Snackbar

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): BottomNavFragmentBinding = BottomNavFragmentBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.viewModel = viewModel
    }

    override fun setupUi() {

        setupBottomNavigation()
        setupMySeatPanel()

        snackbar = Snackbar.make(
            dataBinding.root,
            "",
            Snackbar.LENGTH_SHORT
        ).apply {
            setAction(R.string.dismiss) { } // no-op
            anchorView =
                if (viewModel.slidePanelState.value == SlidingUpPanelLayout.PanelState.COLLAPSED) dataBinding.panelMySeat
                else dataBinding.bottomNavigation
        }
    }

    private fun setupMySeatPanel() {
        dataBinding.apply {
//            apply transition depending on SlidingUpPanel offset
            panelEntireLayout.addPanelSlideListener(object :
                SlidingUpPanelLayout.PanelSlideListener {
                override fun onPanelSlide(panel: View?, slideOffset: Float) {
                    when {
                        slideOffset < 0.0f -> { // hide or reveal
                            panelCollapsed.root.alpha = 1 + slideOffset
                            panelExpanded.root.alpha = -slideOffset
                        }
                        slideOffset == 0.0f -> {
                            panelCollapsed.root.alpha = 1.0f
                            panelExpanded.root.visibility = View.GONE
                            panelExpanded.root.alpha = 0.0f
                            bottomNavigation.translationY = 0.0f
                        }
                        slideOffset == 1.0f -> {
                            panelCollapsed.root.visibility = View.GONE
                            panelCollapsed.root.alpha = 0.0f
                            panelExpanded.root.alpha = 1.0f
                            bottomNavigation.translationY = 1.0f * bottomNavigation.height
                        }
                        0.0f < slideOffset && slideOffset < 1.0f -> {
                            bottomNavigation.translationY = slideOffset * bottomNavigation.height
                            if (panelCollapsed.root.visibility != View.VISIBLE) panelCollapsed.root.visibility =
                                View.VISIBLE
                            if (panelExpanded.root.visibility != View.VISIBLE) panelExpanded.root.visibility =
                                View.VISIBLE
                            panelCollapsed.root.alpha = 1 - slideOffset
                            panelExpanded.root.alpha = slideOffset
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
        dataBinding.bottomNavigation.setupWithNavController(navHostFragment.navController)
        dataBinding.bottomNavigation.setOnItemReselectedListener {
            (navHostFragment.childFragmentManager.fragments.firstOrNull() as? ScrollableToTop)?.scrollToTop()
        }
    }

    override fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.userMessage.collect() {
                        it?.let { userMessage ->
                            snackbar.apply {
                                setText(userMessage.message)
                                addCallback(object :
                                    BaseTransientBottomBar.BaseCallback<Snackbar>() {

                                    override fun onDismissed(
                                        transientBottomBar: Snackbar?,
                                        event: Int
                                    ) {
                                        super.onDismissed(transientBottomBar, event)
                                        // Once the message is displayed and
                                        // dismissed, notify the ViewModel.
                                        viewModel.userMessageShown(userMessage.id)
                                        transientBottomBar?.removeCallback(this)
                                    }
                                })
                            }.show()
                        }
                    }
                }

                launch {
                    viewModel.mySeatUiState.collect() {
                        if (it.showMyStatePanel) {
                            viewModel.showMyStatePanel()
                        } else {
                            viewModel.hideMyStatePanel()
                        }
                    }
                }
            }
        }
    }
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