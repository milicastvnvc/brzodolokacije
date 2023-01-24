package codeverse.brzodolokacije.ui.post.slider

import androidx.lifecycle.ViewModel
import codeverse.brzodolokacije.utils.managers.ImageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageSliderViewModel @Inject constructor(private val imageManager: ImageManager) : ViewModel() {

    fun setImage(image: String){
        imageManager.saveImage(image)
    }
}