package codeverse.brzodolokacije.ui.addpost

import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import codeverse.brzodolokacije.data.models.Image
import codeverse.brzodolokacije.data.models.NewPost
import codeverse.brzodolokacije.data.models.maps.input.Feature
import codeverse.brzodolokacije.domain.use_case.post.AddPostUseCase
import codeverse.brzodolokacije.domain.use_case.post.PostImagesUseCase
import codeverse.brzodolokacije.domain.use_case.post.UploadImagesUseCase
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Result
import codeverse.brzodolokacije.utils.helpers.ImageHelper
import codeverse.brzodolokacije.utils.managers.FeatureManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val postImagesUseCase: PostImagesUseCase,
    private val uploadImagesUseCase: UploadImagesUseCase,
    private val featureManager: FeatureManager,
    private val pathReader: IPathReader
): ViewModel(){

    private val _stateAddPost = mutableStateOf(MyState<Long>())
    val stateAddPost : State<MyState<Long>> = _stateAddPost

    private val _stateUploadImages = mutableStateOf(MyState<Unit>())
    val stateUploadImages : State<MyState<Unit>> = _stateUploadImages

    var _location = mutableStateOf<Feature?>(null)
    var _images = mutableStateOf(mutableListOf<Image>())
    var _imageBool = mutableStateOf(false)
    var _imageAlert = mutableStateOf("")
    var _mapOpen = mutableStateOf(false)
    var _tagDialogOpen = mutableStateOf(false)
    var _cameraOpen = mutableStateOf(false)
    var _description = mutableStateOf<String?>("")
    var _tagLocation = mutableStateOf(mutableListOf<String>())


    fun addPost(){

        if (_description.value!!.isBlank()) _description.value = null
        val newPost = NewPost(_description.value, _location.value!!.center[0],_location.value!!.center[1],
            _location.value!!.place_name,_location.value!!.text, _location.value!!.place_name_sr, _tagLocation.value)

        addPostUseCase(newPost).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _stateAddPost.value = MyState(success = true, data = result.data)
                    println("USPESNO!")
                    uploadImages(result.data!!)
                }
                is Result.Error -> {
                    _stateAddPost.value = MyState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    println(result.message)
                    println("GRESKA")
                }
                is Result.Loading -> {
                    _stateAddPost.value = MyState(isLoading = true)
                }
            } }.launchIn(viewModelScope)
    }

    fun uploadImages(postId: Long){

        val listOfFiles = mutableListOf<File>()
        for(image in _images.value){
            var file: File? = null

            if(image.isFromCamera){
                file = image.file
            }
            else{
                file = pathReader.getFileFromUri(image.uri)

            }
            if (file == null){
                println("File je null")
                break
            }

            println("Naziv fajla: " + file!!.name)
            println("Da li je fajl? " + file!!.isFile)
            println("Da li moze da se cita? " + file.canRead())
            listOfFiles.add(file)
        }

        if(listOfFiles.size != _images.value.size){
            println("Nije proslo sve kako treba")
            _imageAlert.value = "Greška sa fajlovima"
        }
        else {
            println("Proslo je sve kako treba")

            uploadImagesUseCase(postId, listOfFiles).onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _stateUploadImages.value =
                            MyState(success = true, data = result.data!!.actionData)
                        println("USPESNO!")
                    }
                    is Result.Error -> {
                        _imageAlert.value = result.message!!
                        _stateUploadImages.value = MyState(
                            error = result.message ?: "An unexpected error occured"
                        )
                        println("GRESKA")
                    }
                    is Result.Loading -> {
                        _stateUploadImages.value = MyState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun deleteLocation(){
        _location.value = null
    }

    fun onDismissMap(){
        _mapOpen.value = false;
    }

    fun onConfirmMap(feature: Feature){

        _location.value = feature
        _mapOpen.value = false;
    }
    fun onDismissTagDialog(){
        _tagDialogOpen.value = false;
    }

    fun onConfirmTagDialog(tag: String){

        _tagLocation.value.add(tag.trim())
        _tagDialogOpen.value = false;
    }

    fun deleteTag(tag: String){
        _tagLocation.value = _tagLocation.value.filter { it != tag }?.toMutableList()
    }

    fun onCameraImageSaved(uri: Uri, file: File){
        println("Pokupljena slika")
        _cameraOpen.value = false

        addImages(mutableListOf(uri), listOf(file))
    }

    fun addImages(tempImages: MutableList<Uri>, files: List<File>? = null){

        if(tempImages.size+_images.value.size>5)
            _imageBool.value = true
        else {
            if(files != null){ //salje se iz kamere

                for (index in 0 until files.size) {
                    val image = Image(tempImages[index],files[index],true)
                    _images.value.add(image)
                }
            }
            else{ //salje se iz galerije
                for (temp in tempImages){
                    if(temp != null){
                        val image = Image(temp,null,false)
                        _images.value.add(image)
                    }
                }
            }
        }
    }

    fun removeImage(index: Int){
        _images.value = _images.value.filterIndexed{ i,image -> i != index }?.toMutableList()
    }

}