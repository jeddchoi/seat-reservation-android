package jed.choi.seatreservation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {
    private val _navigateToPlaceHolder = MutableStateFlow(Unit)
    val navigateToPlaceHolder : StateFlow<Unit>
        get() = _navigateToPlaceHolder

    fun navigateToPlaceHolder() {
        _navigateToPlaceHolder.update { }
    }
}
