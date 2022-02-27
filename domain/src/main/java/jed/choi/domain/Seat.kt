package jed.choi.domain

data class Seat(
    val name: String,
    val section: Section,
    val state: SeatState = SeatState.IDLE,
    val isCloseToWall: Boolean,
    val isCloseToWindow: Boolean,
    val isSoloFriendly: Boolean,  // 혼자 앉기 좋은지
    override val positionInSection: PositionInSection,
) : MapBlock

enum class SeatState {
    IDLE,
    RESERVED,
    ACQUIRED,
    AWAY,
    AWAY_INTENTIONAL,
    NOT_USED,
}
