package jed.choi.seatreservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import jed.choi.domain.Seat
import jed.choi.domain.UserSession
import jed.choi.domain.UserState
import jed.choi.domain.usecase.AddUserMessage
import jed.choi.domain.usecase.GetUserMessage
import jed.choi.domain.usecase.GetUserState
import jed.choi.domain.usecase.RemoveUserMessage
import jed.choi.seatreservation.model.MySeatUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BottomNavViewModel @Inject constructor(
    private val addUserMessage: AddUserMessage,
    private val getUserMessage: GetUserMessage,
    private val removeUserMessage: RemoveUserMessage,
    private val getUserState: GetUserState,
) : ViewModel() {

    private val _mySeatUiState = MutableStateFlow(
        MySeatUiState(
            myState = UserState.LOGGED_IN,
            mySeat = Seat(),
            mySession = UserSession()
        )
    )
    val mySeatUiState: StateFlow<MySeatUiState>
        get() = _mySeatUiState

    val userMessage = getUserMessage.invoke().distinctUntilChanged()
    val slidePanelState = MutableStateFlow(SlidingUpPanelLayout.PanelState.COLLAPSED)



    /*
     for progress indicator
     */
    private val _maxProgress = MutableStateFlow(100)
    val maxProgress : StateFlow<Int>
        get() = _maxProgress

    private val _progress = MutableStateFlow(0)
    val progress : StateFlow<Int>
        get() = _progress

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

    fun setProgress(value: Int) {
        _progress.update { value }
    }
}
