package codeverse.brzodolokacije.data.repository

import codeverse.brzodolokacije.data.apis.PostApi
import codeverse.brzodolokacije.data.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject


class PostRepository @Inject constructor(
    private val postApi: PostApi
) {

    suspend fun addPost(newPost: NewPost): Response<CustomResponse<Long>> {
        return postApi.addPost(newPost)
    }

    suspend fun uploadImages(postId: Long, files: MutableList<File>) : Response<CustomResponse<Unit>>{

        val imagesParts: ArrayList<MultipartBody.Part> = arrayListOf()

        for (index in 0 until files.size) {

            val file: File = files[index]

            imagesParts.add(MultipartBody.Part.createFormData(
                "files",
                file.name,
                file.asRequestBody()
            ))
        }

        val image: MultipartBody.Part


//        image = MultipartBody.Part.createFormData("image",
//            file.getName(),
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
//                file));
//        image = MultipartBody.Part.createFormData(
//            "image",
//            file.name,
//            file.asRequestBody()
//        )
////
//        image = MultipartBody.Part
//            .createFormData(
//                name = "file",
//                filename = files[0]!!.name,
//                body = files[0]!!.asRequestBody()
//            )
//        val requestFile: RequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
//
//        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return postApi.uploadImages(postId, imagesParts.toList())
    }

    suspend fun postImages(files : List<File>): Response<CustomResponse<Long?>> {

        val imagesParts: Array<MultipartBody.Part?> = arrayOfNulls<MultipartBody.Part>(files.size)

        for (index in 0 until files.size) {

            val file: File = files[index]

            imagesParts[index] = MultipartBody.Part.createFormData(
                "image",
                file.name,
                file.asRequestBody()
            )
        }
        return postApi.postImages(1, imagesParts)
    }

    suspend fun getPostById(postId: Long) : Response<CustomResponse<PostItem>>
    {
        return postApi.getPostById(postId)
    }
    suspend fun getPostsByUser(userId: Long, pageIndex: Int = 0, pageSize: Int = 20) : Response<UsersPostResponse> {
        return postApi.getPostsByUser(userId, pageIndex, pageSize)
    }

    suspend fun getPostsBySearch(placeName: String, longitude: Double? = null,
                                 latitude: Double? = null, tags: MutableList<String>? = null,
                                 sortBy: Int = 0, sortOrder: Int = 1,
                                 pageIndex: Int = 0, pageSize: Int = 20) : Response<MutableList<PostItem>>{

        return postApi.getPostsBySearch(placeName,longitude,latitude,tags,sortBy,sortOrder,pageIndex,pageSize)
    }

    suspend fun deletePost(postId: Long) : Response<CustomResponse<Unit>> {
        return postApi.deletePost(postId)
    }

    suspend fun getCoordinatesByUser(userId: Long) : Response<ArrayList<UserCoordinateResponse>> {
        return postApi.getCoordinatesByUser(userId)
    }

    suspend fun addComment(newComment: NewComment): Response<CustomResponse<Unit>> {
        return postApi.addComment(newComment)
    }

    suspend fun deleteComment(commentId: Long): Response<CustomResponse<Unit>> {
        return postApi.deleteComment(commentId)
    }
    suspend fun updateComment(updatedComment: UpdatedComment): Response<CustomResponse<Unit>> {
        return postApi.updateComment(updatedComment)
    }

    suspend fun addRate(ratePost: RatePost) : Response<CustomResponse<Double>>{
        return postApi.addRate(ratePost)
    }

    suspend fun deleteRate(postId: Long) : Response<CustomResponse<Double>>{
        return postApi.deleteRate(postId)
    }
}