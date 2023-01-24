package codeverse.brzodolokacije.ui.addpost

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

interface IPathReader {
    fun getPathFromUri(contentUri: Uri): String?
    fun getFileFromUri(contentUri: Uri): File?
}

class PathReader(
    private val app: Application
) :IPathReader {

     override fun getPathFromUri(contentUri: Uri): String? {
        if(contentUri.scheme != "content") {
            return null
        }
         var fileName: String? = null
         app.contentResolver
              .query(
                  contentUri,
                  arrayOf(MediaStore.Images.ImageColumns.DISPLAY_NAME),
                  null,
                  null,
                  null,
              )?.use { cursor ->
                  val index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                  cursor.moveToFirst()
                 fileName = cursor.getString(index)
              }
         println("filePath je " + fileName)
         return fileName

         //         val projection = arrayOf(MediaStore.Images.Media.DATA)
//         val cursor: Cursor = managedQuery(uri, projection, null, null, null)
//         startManagingCursor(cursor)
//         val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//         cursor.moveToFirst()
//         val string = cursor.getString(column_index)
//         return string
    }


    override fun getFileFromUri(contentUri: Uri): File? {
        if(contentUri.scheme != "content") {
            return null
        }

        var fileName = ""
        app.contentResolver
            .query(
                contentUri,
                arrayOf(MediaStore.Images.ImageColumns.DISPLAY_NAME),
                null,
                null,
                null,
            )
            ?.use { cursor ->
                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                fileName = cursor.getString(index)
                cursor.close()
            }

        println("Ime fajla je " + fileName)

        val parcelFileDescriptor = app.contentResolver.openFileDescriptor(contentUri,"r", null)?: return null
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(app.cacheDir,fileName)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        //        val file = Uri.fromFile(
//            File(
//                app.cacheDir,
//                fileName
//            )
//        ).toFile()

        return file
    }

}