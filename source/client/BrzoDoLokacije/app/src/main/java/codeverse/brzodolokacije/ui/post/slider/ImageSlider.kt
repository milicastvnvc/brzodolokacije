package codeverse.brzodolokacije.ui.post.slider

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Routes
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageSlider(navController: NavController, post: PostItem, openImage:Boolean = false, imageSliderViewModel: ImageSliderViewModel = hiltViewModel()){

    var painter: Painter
    val pagerState = rememberPagerState()

    HorizontalPager(
        count = post.resources.size,
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
    ) { page ->
        painter = rememberImagePainter(Constants.BASE_URL + post.resources[page].path)
        if(openImage){
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(346.dp)
                    .clickable {
                        imageSliderViewModel.setImage(post.resources[page].path)
                        navController.navigate(Routes.IMAGE)
                    },
                painter = painter,
                alignment = Alignment.CenterStart,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
        else{
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(346.dp),
                painter = painter,
                alignment = Alignment.CenterStart,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }

    }
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom){
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )
    }





}