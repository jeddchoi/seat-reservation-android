package jed.choi.seatreservation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import dagger.hilt.android.AndroidEntryPoint
import jed.choi.seatreservation.databinding.ActivityMainBinding
import jed.choi.ui_core.Authorizable
import jed.choi.ui_core.BaseViewBindingActivity


@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<ActivityMainBinding, MainViewModel>(), Authorizable {
    override val viewModel: MainViewModel by viewModels()

    private val signInLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result: FirebaseAuthUIAuthenticationResult? ->

        Log.i(TAG, "signin result: $result")
    }

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


    override fun startSignIn() {
        Log.i(TAG, "startSignIn")


        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder() // ... options ...
            .setIsSmartLockEnabled(false)
            .setAvailableProviders(
                listOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build()
                )
            )
            .build()
        signInLauncher.launch(signInIntent)
    }


    override fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
//            .addOnCompleteListener {
//
//            }
    }
}
