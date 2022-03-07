package jed.choi.seatreservation.mapper

import jed.choi.domain.entity.MyUserStateEntity
import jed.choi.seatreservation.model.MySeatUiState

fun MySeatUiState.toUserStateEntity() = MyUserStateEntity(
    myName, mySeat, mySession
)


fun MyUserStateEntity.toSeatUiState() = MySeatUiState(
    myName, mySeat, mySession
)