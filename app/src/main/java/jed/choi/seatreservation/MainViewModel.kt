package jed.choi.seatreservation

import androidx.lifecycle.ViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import jed.choi.domain.UserSession
import jed.choi.domain.UserState
import jed.choi.domain.entity.UserMessageEntity
import jed.choi.seatreservation.model.MySeatUiState
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
        addRandomUserMessage()
    }

    fun addRandomUserMessage() {
        _mySeatUiState.update { currentUiState ->
            val messages = currentUiState.userMessages + UserMessageEntity(
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
