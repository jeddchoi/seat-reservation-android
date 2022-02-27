package jed.choi.domain

/**
 * represents a block that shows grid of store section map
 */
interface MapBlock {
    val positionInSection: PositionInSection
}

data class PositionInSection(
    val x: Int,
    val y: Int
)
