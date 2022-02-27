package jed.choi.domain

data class Section(
    val name: String,
    val store: Store,
    val seats: List<Seat>,
    val width: Int,
    val height: Int
) {
    val maxSeatCount: Int
        get() = seats.size
    val availableSeatCount: Int
        get() = seats.filter { it.state == SeatState.IDLE }.size
}
