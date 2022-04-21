package jed.choi.domain.entity

data class StoreEntity(
    val name: String? = "Unknown",
    val latitude: String? = null, // 00-37-27-58-90
    val longitude: String? = null, // 01-27-12-97-42
    val sections: List<SectionEntity>? = emptyList()
)
