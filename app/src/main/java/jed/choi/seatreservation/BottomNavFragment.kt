package jed.choi.seatreservation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint
import jed.choi.seatreservation.BottomNavViewModel.Event
import jed.choi.seatreservation.databinding.BottomNavFragmentBinding
import jed.choi.seatreservation.databinding.PanelMySeatCollapsedBinding
import jed.choi.seatreservation.databinding.PanelMySeatExpandedBinding
import jed.choi.ui_core.BaseDataBindingFragment
import jed.choi.ui_core.ScrollableToTop
import jed.choi.ui_core.repeatOnStarted
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val googleSignInAccount = task.getResult(ApiException::class.java)
                    googleSignInAccount?.apply {
                        idToken?.let { idToken ->
                            viewModel.onSignInWithGoogle(idToken)
                        }
                    }
                } catch (e: ApiException) {
                    print(e.message)
                }
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                Log.i(TAG, "onRequestPermissionsResult: Permission granted by user")

            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Log.i(TAG, "onRequestPermissionsResult: Permission denied by user")
            }
        }

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
//            apply transition depending on SlidingUpPanel offset
        dataBinding.panelEntireLayout.addPanelSlideListener(object :
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
                        dataBinding.bottomNavigation.translationY = 0.0f
                    }
                    slideOffset == 1.0f -> {
                        panelCollapsed.root.visibility = View.GONE
                        panelCollapsed.root.alpha = 0.0f
                        panelExpanded.root.alpha = 1.0f
                        dataBinding.bottomNavigation.translationY =
                            1.0f * dataBinding.bottomNavigation.height
                    }
                    0.0f < slideOffset && slideOffset < 1.0f -> {
                        dataBinding.bottomNavigation.translationY =
                            slideOffset * dataBinding.bottomNavigation.height
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


    private fun setupBottomNavigation() {
        dataBinding.bottomNavigation.setupWithNavController(navHostFragment.navController)
        dataBinding.bottomNavigation.setOnItemReselectedListener {
            (navHostFragment.childFragmentManager.fragments.firstOrNull() as? ScrollableToTop)?.scrollToTop()
        }
    }

    override fun observeViewModel() {
        repeatOnStarted {
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
                viewModel.eventFlow.collect { event -> handleEvent(event) }
            }
        }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            Event.ClickSignInWithGoogle -> {
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }
            Event.ClickSignOut -> viewModel.onSignOut()
            is Event.ClickReserveSeat -> setupPermissions {
                viewModel.onReserveSeat(event.seatPath)
            }
            Event.ClickStartUsing -> viewModel.onStartUsing()
            Event.ClickLeaveAwaySeat -> viewModel.onLeaveAwaySeat()
            Event.ClickResumeUsingSeat -> viewModel.onResumeUsingSeat()
            Event.ClickStartBusiness -> setupPermissions { viewModel.onStartBusiness() }
            Event.ClickStopBusiness -> setupPermissions { viewModel.onStopBusiness() }
            Event.ClickStopUsing -> viewModel.onStopUsing()
            Event.ClickUserCheckTimeout -> viewModel.onUserCheckTimeout()
        }
    }

    private fun setupPermissions(afterGrantedAction: () -> Unit) {

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                afterGrantedAction()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.

                val builder = AlertDialog.Builder(requireContext())

                val dialog =
                    builder.setMessage("Permission to access microphone is required for this app to record audio")
                        .setTitle("Permission Required")
                        .setPositiveButton("OK") { dialog, id ->
                            Log.i(TAG, "setupPermissions: Clicked OK")
                            requestPermissionLauncher.launch(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        }
                        .setNegativeButton("Cancel") { dialog, id ->
                            Log.i(TAG, "setupPermissions: Clicked Cancel")
                        }.create()
                dialog.show()

            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
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