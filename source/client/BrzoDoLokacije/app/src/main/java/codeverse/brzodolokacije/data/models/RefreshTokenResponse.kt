package codeverse.brzodolokacije.data.models

data class RefreshTokenResponse(val jwtToken: String,
                                val refreshToken: String)