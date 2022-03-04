package jed.choi.domain

data class Section(
    val name: String = "Unknown",
    val store: Store = Store(),
    val seats: List<Seat> = emptyList(),
    val width: Int = 0,
    val height: Int = 0
) {
    val maxSeatCount: Int
        get() = seats.size
    val availableSeatCount: Int
        get() = seats.filter { it.state == SeatState.IDLE }.size
}
