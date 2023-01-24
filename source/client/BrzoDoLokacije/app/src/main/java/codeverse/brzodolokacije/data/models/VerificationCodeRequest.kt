package codeverse.brzodolokacije.data.models

data class VerificationCodeRequest(val email: String,
                                   val isForgotPassword: Boolean)

