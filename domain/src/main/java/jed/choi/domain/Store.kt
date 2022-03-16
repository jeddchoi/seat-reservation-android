package jed.choi.domain

data class Store(
    val name: String? = "Unknown",
    val latitude: String? = null, // 00-37-27-58-90
    val longitude: String? = null, // 01-27-12-97-42
    val sections: List<Section>? = emptyList()
)
