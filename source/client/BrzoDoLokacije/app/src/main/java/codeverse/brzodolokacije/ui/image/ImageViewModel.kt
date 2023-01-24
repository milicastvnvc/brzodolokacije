package codeverse.brzodolokacije.ui.image

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.managers.ImageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(savedStateHandle: SavedStateHandle, private val imageManager: ImageManager): ViewModel() {

    val resource = mutableStateOf<String?>(null)

    init{
//        savedStateHandle.get<String>("imagePath")?.let { imagePath ->
//            resource.value = Constants.BASE_URL + imagePath
//        }

        resource.value = Constants.BASE_URL + imageManager.getImage()
    }
}