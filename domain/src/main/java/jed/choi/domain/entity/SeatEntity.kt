package jed.choi.domain.entity

import jed.choi.domain.MapBlock
import jed.choi.domain.PositionInSection


data class SeatEntity(
    val name: String? = "Unknown",
    val path: String? = null,
    val number: Int? = null, // minor in BLE Packet
    val macAddress: String? = null, // EC:79:0D:56:92:08
    val bleUuid: String? = null, // 59-00-02-15-1f-4a-e6-a0-00-37-27-58-90-01-27-12-97-42-00-01-00-00-00-01-c3
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
        PATH to path,
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
