package jed.choi.domain.entity

data class UserEntity(
    val id: String? = null,
    val name: String? = null,
    val emailAddress: String? = null,
    val profilePhotoUrl: String? = null,
    val isLoggedIn: Boolean = false
)