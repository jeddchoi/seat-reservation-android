package jed.choi.domain

sealed class NonSeat(
    override val positionInSection: PositionInSection
) : MapBlock {
    data class Table(
        val seatDirections: List<Direction>,
    )
    class Wall

    class Space
}
