package jed.choi.domain

data class Seat(
    val name: String = "Unknown",
    val section: Section = Section(),
    val state: SeatState = SeatState.IDLE,
    val isCloseToWall: Boolean = false,
    val isCloseToWindow: Boolean = false,
    val isSoloFriendly: Boolean = false,  // 혼자 앉기 좋은지
    override val positionInSection: PositionInSection = PositionInSection(),
) : MapBlock

enum class SeatState {
    IDLE,
    RESERVED,
    ACQUIRED,
    AWAY,
    NOT_USED,
}
