package jed.choi.seatreservation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import jed.choi.domain.usecase.*
import jed.choi.domain.usecase.auth.SignInWithGoogle
import jed.choi.domain.usecase.auth.SignOut
import jed.choi.domain.usecase.message.AddUserMessage
import jed.choi.domain.usecase.message.GetUserMessage
import jed.choi.domain.usecase.message.RemoveUserMessage
import jed.choi.seatreservation.model.MySeatUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BottomNavViewModel @Inject constructor(
    private val addUserMessage: AddUserMessage,
    private val getUserMessage: GetUserMessage,
    private val removeUserMessage: RemoveUserMessage,
    private val getUserSession: GetUserSession,
    private val signInWithGoogle: SignInWithGoogle,
    private val signOut: SignOut,
) : ViewModel() {

    private val _mySeatUiState = MutableStateFlow(MySeatUiState("Unknown"))
    val mySeatUiState = _mySeatUiState.asStateFlow()

    init {
        viewModelScope.launch {
//            getUserSession.invoke().collect {
//                when (it) {
//                    is Response.Failure -> {
//                        Log.i(TAG, "Response is failure: ${it.errorMessage}")}
//                    Response.Loading -> {
//                        Log.i(TAG, "Response is loading")
//                    }
//                    is Response.Success -> _mySeatUiState.value = it.data.toSeatUiState()
//                }
//
//            }
        }
    }


    val userMessage = getUserMessage.invoke().distinctUntilChanged()
    val slidePanelState = MutableStateFlow(SlidingUpPanelLayout.PanelState.COLLAPSED)


    /*
     for progress indicator
     */
    val progress = mySeatUiState.map {
        if (it.showRemainingTime)
            it.mySession.remainingProgressPermillage
        else
            it.mySession.elapsedProgressPermillage
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)


    fun testUserMessage() {
        viewModelScope.launch {
            addUserMessage.invoke("No Internet connection")
        }
    }

    fun userMessageShown(messageId: Long) {
        viewModelScope.launch {
            removeUserMessage.invoke(messageId)
        }
    }

    fun showMyStatePanel() {
        slidePanelState.update {
            SlidingUpPanelLayout.PanelState.COLLAPSED
        }
    }

    fun hideMyStatePanel() {
        slidePanelState.update {
            SlidingUpPanelLayout.PanelState.HIDDEN
        }
    }


    fun signInWithGoogle(idToken: String) {
        Log.i(TAG, "signInWithGoogle: idToken = $idToken")
        viewModelScope.launch {
            signInWithGoogle.invoke(idToken).collect() {
                Log.i(TAG, "signInWithGoogle: $it")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOut.invoke().collect() {
                Log.i(TAG, "signOut: $it")
            }
        }
    }



    companion object {
        const val TAG = "BottomNavViewModel"
    }

}
