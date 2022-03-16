package jed.choi.domain

import jed.choi.domain.entity.SeatEntity
import jed.choi.domain.entity.SeatState

data class Section(
    val name: String = "Unknown",
    val number: Int? = null, // major in BLE Packet
    val seats: List<SeatEntity>? = emptyList(),
    val width: Int? = null,
    val height: Int? = null
) {
    val maxSeatCount: Int
        get() = seats?.size ?: 0
    val availableSeatCount: Int
        get() = seats?.filter { it.state == SeatState.IDLE }?.size ?: 0
}
