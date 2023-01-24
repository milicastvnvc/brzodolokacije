package codeverse.brzodolokacije.data.models


data class CustomResponse<T>(
    val success : Boolean,
    val errors:MutableList<String>,
    val actionData : T,
    val hasErrors : Boolean)
