package codeverse.brzodolokacije.utils

sealed class Result<T>(val data: T? = null, val message: String? = null, val redirect: Boolean = false) {
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: String, data: T? = null, redirect: Boolean = false) : Result<T>(data, message, redirect)
    class Loading<T>(data: T? = null) : Result<T>(data)
}