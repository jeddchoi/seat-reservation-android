package jed.choi.seatreservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import jed.choi.domain.usecase.AddUserMessage
import jed.choi.domain.usecase.GetUserMessage
import jed.choi.domain.usecase.GetUserState
import jed.choi.domain.usecase.RemoveUserMessage
import jed.choi.seatreservation.mapper.toSeatUiState
import jed.choi.seatreservation.model.MySeatUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BottomNavViewModel @Inject constructor(
    private val addUserMessage: AddUserMessage,
    private val getUserMessage: GetUserMessage,
    private val removeUserMessage: RemoveUserMessage,
    private val getUserState: GetUserState,
) : ViewModel() {

    private val _mySeatUiState = MutableStateFlow(MySeatUiState("Unknown"))
    val mySeatUiState = _mySeatUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUserState.invoke().collect {
                _mySeatUiState.value = it.toSeatUiState()
            }
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


}
