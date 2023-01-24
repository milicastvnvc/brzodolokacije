package codeverse.brzodolokacije.data.models

data class LoginResponse(val user: User,
                         val token: String,
                         val refreshToken: String)
