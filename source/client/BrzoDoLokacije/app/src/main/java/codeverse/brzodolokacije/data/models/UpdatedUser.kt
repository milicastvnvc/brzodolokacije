package codeverse.brzodolokacije.data.models

data class UpdatedUser(val id: Long = 0,
                       val username: String,
                       val firstName: String,
                       val lastName: String,
                       val changeProfilePicture: Boolean = false,
                       var profilePictureBase64: String? = null)
