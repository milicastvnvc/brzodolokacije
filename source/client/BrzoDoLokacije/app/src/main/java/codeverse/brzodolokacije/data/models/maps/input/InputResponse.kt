package codeverse.brzodolokacije.data.models.maps.input

data class InputResponse(
    val attribution: String,
    val features: MutableList<Feature>,
    val query: List<String>,
    val type: String
)