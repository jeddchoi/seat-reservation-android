package jed.choi.seatreservation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import jed.choi.domain.usecase.ObserveUserState
import jed.choi.domain.usecase.auth.SignInWithGoogle
import jed.choi.domain.usecase.auth.SignOut
import jed.choi.domain.usecase.message.AddUserMessage
import jed.choi.domain.usecase.message.GetUserMessage
import jed.choi.domain.usecase.message.RemoveUserMessage
import jed.choi.domain.usecase.seat.*
import jed.choi.domain.usecase.session.UserCheckTimeout
import jed.choi.seatreservation.mapper.toSeatUiState
import jed.choi.seatreservation.model.MySeatUiState
import jed.choi.seatreservation.model.ProgressDisplayMode
import jed.choi.ui_core.MutableEventFlow
import jed.choi.ui_core.asEventFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BottomNavViewModel @Inject constructor(
    private val addUserMessage: AddUserMessage,
    private val getUserMessage: GetUserMessage,
    private val removeUserMessage: RemoveUserMessage,
    private val observeUserState: ObserveUserState,
    private val signInWithGoogle: SignInWithGoogle,
    private val signOut: SignOut,
    private val reserveSeat: ReserveSeat,
    private val startUsing: StartUsingSeat,
    private val stopUsing: StopUsingSeat,
    private val startBusiness: StartBusiness,
    private val stopBusiness: StopBusiness,
    private val leaveAwaySeat: LeaveAwaySeat,
    private val resumeUsingSeat: ResumeUsingSeat,
    private val userCheckTimeout: UserCheckTimeout,
) : ViewModel() {

    private val _mySeatUiState = MutableStateFlow(MySeatUiState("Unknown"))
    val mySeatUiState = _mySeatUiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeUserState.invoke().collect {
                Log.e(TAG, "UserState $it ${it.mySession?.isValidState()}")
                _mySeatUiState.value = it.toSeatUiState()
            }
        }
    }

    /*
     for progress indicator
     */
    val progress = mySeatUiState.transformLatest { uiState ->
        uiState.mySession?.let { userSession ->
            while (true) {
                when (uiState.progressDisplayMode) {
                    ProgressDisplayMode.REMAINING_TIME_PERMILLAGE -> {
                        emit(userSession.remainingProgressPermillage)
                        delay(100L)
                    }
                    ProgressDisplayMode.ELAPSED_TIME_PERMILLAGE -> {
                        emit(userSession.remainingProgressPermillage)
                        delay(100L)
                    }
                    ProgressDisplayMode.NONE -> break // no - op
                }
            }
        }
        emit(0)
    }.onEach {
        Log.i(TAG, "progress: $it")
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)


    val userMessage = getUserMessage.invoke().distinctUntilChanged()

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

    val slidePanelState = MutableStateFlow(SlidingUpPanelLayout.PanelState.COLLAPSED)

    init {
        viewModelScope.launch {
            mySeatUiState.collect() {
                showMyStatePanel()
//                if (it.showMyStatePanel) {
//                    showMyStatePanel()
//                } else {
//                    hideMyStatePanel()
//                }
            }
        }
    }

    fun showMyStatePanel() {
        slidePanelState.update {
            when (it) {
                SlidingUpPanelLayout.PanelState.EXPANDED,
                SlidingUpPanelLayout.PanelState.COLLAPSED -> it
                else -> SlidingUpPanelLayout.PanelState.COLLAPSED
            }
        }
    }

    fun hideMyStatePanel() {
        slidePanelState.update {
            SlidingUpPanelLayout.PanelState.HIDDEN
        }
    }

    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow = _eventFlow.asEventFlow()

    fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun signInWithGoogle() = event(Event.ClickSignInWithGoogle)

    fun signOut() = event(Event.ClickSignOut)

    fun reserveSeat() = event(Event.ClickReserveSeat("-MyB6poExcCX0Hbtubv1/sections/-MyB7tNK1siP7P5AEvzE/seats/-MyB7tNK1siP7P5AEvzE"))

    fun startUsing() = event(Event.ClickStartUsing)

    fun stopUsing() = event(Event.ClickStopUsing)

    fun startBusiness() = event(Event.ClickStartBusiness)

    fun stopBusiness() = event(Event.ClickStopBusiness)

    fun leaveAwaySeat() = event(Event.ClickLeaveAwaySeat)

    fun resumeUsingSeat() = event(Event.ClickResumeUsingSeat)

    fun userCheckTimeout() = event(Event.ClickUserCheckTimeout)


    fun onSignInWithGoogle(idToken: String) {
        viewModelScope.launch {
            signInWithGoogle.invoke(idToken).collect() {
                Log.e(TAG, "SignInWithGoogle Response : $it")
            }
        }
    }

    fun onSignOut() {
        viewModelScope.launch {
            signOut.invoke().collect() {
                Log.e(TAG, "SignOut Response : $it")
            }
        }
    }

    fun onReserveSeat(seatPath: String) {
        viewModelScope.launch {
            reserveSeat.invoke(seatPath).collect() {
                Log.e(TAG, "ReserveSeat Response : $it")
            }
        }
    }

    fun onStartUsing() {
        viewModelScope.launch {
            startUsing.invoke().collect {
                Log.e(TAG, "StartUsing Response : $it")
            }
        }
    }

    fun onStopUsing() {
        viewModelScope.launch {
            stopUsing.invoke().collect {
                Log.e(TAG, "StopUsing Response : $it")
            }
        }
    }

    fun onStartBusiness() {
        viewModelScope.launch {
            startBusiness.invoke().collect {
                Log.e(TAG, "StartBusiness Response : $it")
            }
        }
    }

    fun onStopBusiness() {
        viewModelScope.launch {
            stopBusiness.invoke().collect {
                Log.e(TAG, "StopBusiness Response : $it")
            }
        }
    }

    fun onLeaveAwaySeat() {
        viewModelScope.launch {
            leaveAwaySeat.invoke().collect {
                Log.e(TAG, "LeaveAwaySeat Response : $it")
            }
        }
    }

    fun onResumeUsingSeat() {
        viewModelScope.launch {
            resumeUsingSeat.invoke().collect {
                Log.e(TAG, "ResumeUsingSeat Response : $it")
            }
        }
    }

    fun onUserCheckTimeout() {
        viewModelScope.launch {
            userCheckTimeout.invoke().collect {
                Log.e(TAG, "UserCheckTimeout Response : $it")
            }
        }
    }


    sealed class Event {
        object ClickSignInWithGoogle : Event()
        object ClickSignOut : Event()
        data class ClickReserveSeat(val seatPath: String) : Event()
        object ClickStartUsing : Event()
        object ClickStopUsing : Event()
        object ClickStartBusiness : Event()
        object ClickStopBusiness : Event()
        object ClickLeaveAwaySeat : Event()
        object ClickResumeUsingSeat : Event()
        object ClickUserCheckTimeout : Event()
    }


    companion object {
        const val TAG = "BottomNavViewModel"
    }

}
