package codeverse.brzodolokacije.ui.post.detail

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.data.models.Comment
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.ui.addpost.tags.TagsScreen
import codeverse.brzodolokacije.ui.comment.UpdateComment
import codeverse.brzodolokacije.ui.comment.showChildComment
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.post.CustomAlert
import codeverse.brzodolokacije.ui.post.component.PostInfoCard
import codeverse.brzodolokacije.ui.post.component.OwnerCard
import codeverse.brzodolokacije.ui.post.slider.ImageSlider
import codeverse.brzodolokacije.ui.theme.*
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Routes
import codeverse.brzodolokacije.utils.helpers.DateHelper
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Details(navController: NavController, id: Int, detailsViewModel: DetailsViewModel = hiltViewModel()) {

    val postState = detailsViewModel.postState.value
    val deleteState = detailsViewModel.deletedState.value
    val deleteDialogOpen =  remember { detailsViewModel._deleteDialogOpen }

    val context = LocalContext.current

    val post = detailsViewModel.post
    val backdropState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val offset by backdropState.offset
    val value = backdropState.currentValue
    val halfHeightDp = LocalConfiguration.current.screenHeightDp / 14
    val halfHeightPx = with(LocalDensity.current) { halfHeightDp.dp.toPx() }

    LaunchedEffect(backdropState) {
        backdropState.reveal()
    }


//    BackdropScaffold(
//        scaffoldState = backdropState,
//        frontLayerScrimColor = Color.Unspecified,
//        peekHeight = 0.dp,
//        headerHeight = halfHeightDp.dp,
//        appBar = {
//            // Top app bar
//        },
//        backLayerBackgroundColor = Color.Transparent,
//        backLayerContent = {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .alpha(offset / halfHeightPx)
//            )
//            {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("") },
                        backgroundColor = backgroundColor,
                        contentColor = lightBackgroundColor,
                        navigationIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp, 24.dp)
                                    .clickable {
                                        navController.navigateUp()
                                    },
                                tint = lightBackgroundColor
                            )
                        },
                        actions = {
                            //brisanje objave
                            if(detailsViewModel.isMe()){
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp, 24.dp)
                                        .clickable {
                                            deleteDialogOpen.value = true
                                        },
                                    tint = dangerColor
                                )
                            }
                        }
                    )
                },

                content = {
                    if (deleteDialogOpen.value){
                        CustomAlert(onDismiss = detailsViewModel::onDissmisDialog,
                            onConfirm = detailsViewModel::onConfirmDialog,
                            text = "Da li sigurno želite da obrišete objavu?")
                    }
                    if(deleteState.isLoading){
                        Loader(isDisplayed = true)
                    }
                    else if (deleteState.success){
                        Toast.makeText(context, "Uspešno izbrisana objava", Toast.LENGTH_LONG).show()
                        navController.navigate(Routes.SEARCH) {
                            launchSingleTop = true
                        }
                    }
                    else{
                        postState.data?.let{
                            DetailsView(navController, it, detailsViewModel)
                        }
                    }
                }
            )
        }
//                           },
//        frontLayerContent = {
//            Box(modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 8.dp)) {
//                val listState = rememberLazyListState()
//                Text(
//                    modifier = Modifier.padding(16.dp),
//                    text = "${postState.data?.comments?.size} komentara"
//                )
//                postState.data?.let {
//                    ContentInColumn(
//                        navController = navController,
//                        backdropState = backdropState,
//                        halfHeightPx = halfHeightPx,
//                        listState = listState,
//                        post = it,
//                        detailsViewModel = detailsViewModel
//                    )
//                }
//
//            }
//        }
//    )
//}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun DetailsView(
    navController: NavController,
    post: PostItem,
    detailsViewModel: DetailsViewModel
) {
    

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {

        // Basic details
        item {
            post.apply {

                ImageSlider(navController,post = post, openImage = true)
                Spacer(modifier = Modifier.height(0.dp))
                PostInfoCard(navController, post, detailsViewModel::performRate, detailsViewModel._myRate, detailsViewModel::_avgRating)

            }
        }
        item {
            post.apply {

//                Title(title = "Tagovi")
//                Spacer(modifier = Modifier.height(16.dp))
                if(post.tags.size > 0){
                    Spacer(modifier = Modifier.height(15.dp))
                    TagsScreen(navController = navController, tagLocation = post.tags.toMutableList(), isEditable = false, deleteTag = {})
                }

            }
        }

        // My story details
        item {
            post.apply {

                Spacer(modifier = Modifier.height(5.dp))
//                Title(title = "Detaljan opis")
                if (post.description != null){
                    Divider(modifier = Modifier.padding(16.dp))
                    Text(
                        text = post.description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 16.dp, 0.dp),
                        color = lightBackgroundColor,
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Start
                    )
                }

                Divider(modifier = Modifier.padding(16.dp))

                var text : String = ""
                var comment : Comment? = null
                if(post.comments.isEmpty())  text = "Nema komentara. Dodajte svoj. "
                else { comment = post.comments[0]
                    text = "Pogledaj još "+post.comments.size + " komentara"}
                Column(modifier = Modifier
                    .padding(16.dp,0.dp,16.dp,0.dp)) {
                    if(comment!= null) {
                        Box() {
                            Column(
                                modifier = Modifier
                                    .padding(0.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row() {
                                        Image(
                                            painter = rememberImagePainter(Constants.BASE_URL + comment!!.createdBy.profilePicturePath),
                                            contentDescription = "profile-image",
                                            modifier = Modifier
                                                .size(35.dp)
                                                .clip(CircleShape)
                                        )
                                        Column() {
                                            Text(
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                text = "${comment.createdBy.username}",
                                                modifier = Modifier.padding(start = 8.dp),
                                                style = MaterialTheme.typography.subtitle2,
                                                textAlign = TextAlign.Center
                                            )
                                            Text(
                                                fontSize = 10.sp,
                                                text = "${DateHelper.formatApiDate(comment.createdDate)}",
                                                modifier = Modifier.padding(start = 8.dp),
                                                style = MaterialTheme.typography.subtitle2,
                                                textAlign = TextAlign.Center
                                            )

                                        }
                                    }
                                }
                                Spacer(
                                    modifier = Modifier
                                        .height(8.dp)
                                )
                                Text(
                                    text = "${comment.text}",
                                    fontSize = 12.sp,
//                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.subtitle2
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(8.dp)
                                )
                            }
                        }
                    }

                        ClickableText(text = AnnotatedString("${text}"), style = TextStyle(
                            color = primaryColor,
                            fontSize = 15.sp
                        ), onClick = {
                            navController.navigate("${Routes.COMMENT}/" + id)
                        })

                    }
            }
            val scaffoldState = rememberBackdropScaffoldState(
                BackdropValue.Concealed
            )
        }

        item{
            Spacer(modifier = Modifier.height(24.dp))
        }

        if(!detailsViewModel.isMe()){
            // Owner info
            item {
                post.apply {

                    Title(title = "Informacije o korisniku")
                    Spacer(modifier = Modifier.height(16.dp))
                    post.createdBy.apply {
                        OwnerCard(navController,
                            post.createdBy,
                            username,
                            firstName+" "+lastName,
                            Constants.BASE_URL+profilePicturePath)
                    }
                }
            }
        }
    }
}

@Composable
fun Title(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 0.dp, 0.dp),
        color = lightBackgroundColor,
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.W600,
        textAlign = TextAlign.Start
    )
}
