package jed.choi.domain

import jed.choi.domain.entity.SeatEntity
import jed.choi.domain.entity.SeatState

data class Section(
    val name: String = "Unknown",
    val storeId: String? = null,
    val seats: List<SeatEntity> = emptyList(),
    val width: Int = 0,
    val height: Int = 0
) {
    val maxSeatCount: Int
        get() = seats.size
    val availableSeatCount: Int
        get() = seats.filter { it.state == SeatState.IDLE }.size
}
