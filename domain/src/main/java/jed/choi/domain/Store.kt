package jed.choi.domain

data class Store(
    val name: String = "Unknown",
    val sections: List<Section> = emptyList()
)
