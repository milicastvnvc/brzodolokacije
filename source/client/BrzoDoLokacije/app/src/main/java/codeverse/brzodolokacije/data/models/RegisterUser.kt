package codeverse.brzodolokacije.data.models

data class RegisterUser(val username: String,
                        val password: String,
                        val email: String,
                        val firstName: String,
                        val lastName: String)