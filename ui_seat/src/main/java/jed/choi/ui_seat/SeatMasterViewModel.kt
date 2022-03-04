package jed.choi.ui_seat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jed.choi.domain.usecase.AddUserMessage
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeatMasterViewModel @Inject constructor(
    private val addUserMessage: AddUserMessage,
): ViewModel() {

    fun testUserMessage() {
        viewModelScope.launch {
            addUserMessage.invoke("No Internet connection")
        }
    }

}