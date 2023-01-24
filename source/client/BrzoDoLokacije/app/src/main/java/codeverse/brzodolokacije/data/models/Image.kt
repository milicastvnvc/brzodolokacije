package codeverse.brzodolokacije.data.models

import android.net.Uri
import java.io.File

data class Image(val uri: Uri, val file: File? = null, val isFromCamera: Boolean)
