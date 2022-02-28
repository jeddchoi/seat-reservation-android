package jed.choi.seatreservation

import androidx.lifecycle.ViewModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import jed.choi.domain.MySeatUiState
import jed.choi.domain.UserSession
import jed.choi.domain.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    val _mySeatUiState = MutableStateFlow(MySeatUiState("Jed Choi", UserState.LOGGED_IN, mySession = UserSession()))
    val mySeatUiState : StateFlow<MySeatUiState>
        get() = _mySeatUiState

    val slidePanelState = MutableStateFlow(PanelState.COLLAPSED)
    val slidePanelOffset = MutableStateFlow(0f)

//    init {
//        slidePanelState.update { PanelState.HIDDEN }
//    }

}
