package jed.choi.seatreservation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import dagger.hilt.android.lifecycle.HiltViewModel
import jed.choi.domain.usecase.AddUserMessage
import jed.choi.domain.usecase.GetUserMessage
import jed.choi.domain.usecase.RemoveUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserMessage: GetUserMessage,
    private val removeUserMessage: RemoveUserMessage,
    private val addUserMessage: AddUserMessage,
) : ViewModel() {
//    private val _mySeatUiState = MutableStateFlow(MySeatUiState("Jed Choi", UserState.LOGGED_IN, mySession = UserSession()))
//    val mySeatUiState : StateFlow<MySeatUiState>
//        get() = _mySeatUiState

    val userMessage = getUserMessage.invoke().distinctUntilChanged()
    val slidePanelState = MutableStateFlow(PanelState.COLLAPSED)


    init {
//        TODO: update slidePanelState depending on mySeatUiState
//        slidePanelState.update { PanelState.HIDDEN }
    }

    fun addRandomUserMessage() {
        viewModelScope.launch {
            addUserMessage.invoke("No Internet connection")
        }
    }

    fun userMessageShown(messageId: Long) {
        Log.i("SnackBar", "userMessageShown: $messageId")
        viewModelScope.launch {
            removeUserMessage.invoke(messageId)
        }
    }

}
