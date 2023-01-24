package codeverse.brzodolokacije.data.models

data class ResetPasswordData(val email: String,
                             val password : String,
                             val passwordConfirmation: String,
                             val token: String)
