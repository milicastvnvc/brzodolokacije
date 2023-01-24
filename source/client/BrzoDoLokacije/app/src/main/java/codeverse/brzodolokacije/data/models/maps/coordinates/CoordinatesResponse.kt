package codeverse.brzodolokacije.data.models.maps.coordinates

import codeverse.brzodolokacije.data.models.maps.input.Feature

data class CoordinatesResponse(
    val attribution: String,
    val features: List<Feature>,
    val query: List<String>,
    val type: String
)