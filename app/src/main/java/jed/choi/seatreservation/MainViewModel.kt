package jed.choi.seatreservation

import androidx.lifecycle.ViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import jed.choi.domain.UserSession
import jed.choi.domain.UserState
import jed.choi.seatreservation.uistate.MySeatUiState
import jed.choi.ui_core.UserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.*

class MainViewModel(

) : ViewModel() {
    private val _mySeatUiState = MutableStateFlow(MySeatUiState("Jed Choi", UserState.LOGGED_IN, mySession = UserSession()))
    val mySeatUiState : StateFlow<MySeatUiState>
        get() = _mySeatUiState

    val slidePanelState = MutableStateFlow(PanelState.COLLAPSED)
    init {
//        TODO: update slidePanelState depending on mySeatUiState
//        slidePanelState.update { PanelState.HIDDEN }
        _mySeatUiState.update { currentUiState ->
            val messages = currentUiState.userMessages + UserMessage(
                id = UUID.randomUUID().mostSignificantBits,
                message = "No Internet connection"
            )
            currentUiState.copy(userMessages = messages)
        }
    }

    fun userMessageShown(messageId: Long) {
        _mySeatUiState.update { currentUiState ->
            val messages = currentUiState.userMessages.filterNot { it.id == messageId }
            currentUiState.copy(userMessages = messages)
        }
    }
}
