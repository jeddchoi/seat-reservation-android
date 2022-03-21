package jed.choi.domain.entity

import jed.choi.domain.MapBlock
import jed.choi.domain.PositionInSection


data class SeatEntity(
    val name: String? = "Unknown",
    val number: Int? = null, // minor in BLE Packet
    val macAddress: String? = null, // EC:60:5E:E5:34:AC
    val bleUuid: String? = null, // 1f4ae6a0-0037-2758-9001-271297420001
    val state: SeatState? = SeatState.IDLE,
    val isCloseToWall: Boolean? = false,
    val isCloseToWindow: Boolean? = false,
    val isSoloFriendly: Boolean? = false,  // 혼자 앉기 좋은지
    override val positionInSection: PositionInSection? = PositionInSection(),
) : MapBlock {

//    val storeId : String?
//    val sectionId : String?
//    val seatId : String?
//
//    init {
//        val splitArr = path?.split("/")
//        storeId = splitArr?.get(0)
//        sectionId = splitArr?.get(1)
//        seatId = splitArr?.get(2)
//    }

    fun toMap(): Map<String, Any?> = mapOf(
        NAME to name,
        STATE to state,
        IS_CLOSE_TO_WALL to isCloseToWall,
        IS_CLOSE_TO_WINDOW to isCloseToWindow,
        IS_SOLO_FRIENDLY to isSoloFriendly,
        POSITION_IN_SECTION to positionInSection,
    )


    companion object {
        const val NAME = "name"
        const val PATH = "sectionId"
        const val STATE = "state"
        const val IS_CLOSE_TO_WALL = "isCloseToWall"
        const val IS_CLOSE_TO_WINDOW = "isCloseToWindow"
        const val IS_SOLO_FRIENDLY = "isSoloFriendly"
        const val POSITION_IN_SECTION = "positionInSection"
    }
}

enum class SeatState {
    IDLE,
    RESERVED,
    ACQUIRED,
    AWAY,
    NOT_USED,
}
