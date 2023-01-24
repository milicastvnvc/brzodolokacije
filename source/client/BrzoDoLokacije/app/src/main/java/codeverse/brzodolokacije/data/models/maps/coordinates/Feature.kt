package codeverse.brzodolokacije.data.models.maps.coordinates

data class Feature(
    val address: String,
    val bbox: List<Double>,
    val center: List<Double>,
    val context: List<Context>,
    val id: String,
    val language: String,
    val language_en: String,
    val language_sr: String,
    val place_name: String,
    val place_name_en: String,
    val place_name_sr: String,
    val place_type: List<String>,
    val properties: Properties,
    val relevance: Double,
    val text: String,
    val text_en: String,
    val text_sr: String,
    val type: String
)