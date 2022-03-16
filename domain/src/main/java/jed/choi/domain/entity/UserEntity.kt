package jed.choi.domain.entity

import java.util.*


data class UserEntity(
    val name: String? = null,
    val emailAddress: String? = null,
    val profilePhotoUrl: String? = null,
    val createdAt: Date? = null,
) {


    fun toMap(): Map<String, Any?> = mapOf(
        NAME to name,
        EMAIL_ADDRESS to emailAddress,
        PROFILE_PHOTO_URL to profilePhotoUrl,
        CREATED_AT to createdAt,
    )

    companion object {
        const val NAME = "name"
        const val EMAIL_ADDRESS = "emailAddress"
        const val PROFILE_PHOTO_URL = "profilePhotoUrl"
        const val CREATED_AT = "createdAt"
    }
}