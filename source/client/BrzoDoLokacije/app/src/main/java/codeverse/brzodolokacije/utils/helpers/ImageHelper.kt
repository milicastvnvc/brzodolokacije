package codeverse.brzodolokacije.utils.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL


class ImageHelper {

    companion object{

//         suspend fun getImage(resource: String) : Bitmap? = withContext(Dispatchers.IO) {
//
//            var image: Bitmap? = null
//
//            if (resource.isNotBlank()){
//                try {
//                    val url = URL(Constants.BASE_URL + resource)
//
//                    val inputStream: InputStream = url.content as InputStream
//                    image = BitmapFactory.decodeStream(inputStream)
//
//                } catch (e: Exception) {
//
//                    println("Doslo je do greske")
//                    return@withContext null
//                }
//            }
//
//             return@withContext image
//        }

        fun fromBitmapToBase64(image: Bitmap?): String{
            var base64 = "";

            if(image != null){
                val baos = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object

                val b: ByteArray = baos.toByteArray()
                base64 = Base64.encodeToString(b, Base64.DEFAULT)
            }

            return base64;

        }
        fun getImageUri(inContext: Context, inImage: Bitmap, title : String ): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 95, bytes)
            val path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(),
                inImage,
                title,
                null
            )
            System.out.println(path)
            if(path!=null)
                    return Uri.parse(path)
            else return null
        }

    }

}