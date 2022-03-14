package jed.choi.domain.entity

import java.util.*


const val NAME = "name"
const val EMAIL_ADDRESS = "emailAddress"
const val PROFILE_PHOTO_URL = "profilePhotoUrl"
const val CREATED_AT = "createdAt"

data class UserEntity(
    val name: String? = null,
    val emailAddress: String? = null,
    val profilePhotoUrl: String? = null,
    val createdAt: Date? = null,
)