package codeverse.brzodolokacije.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.ui.addpost.tags.AddTagDialog
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.home.components.icon
import codeverse.brzodolokacije.ui.search.SearchScreen
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.dangerColor
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import codeverse.brzodolokacije.utils.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalFoundationApi
@Composable
fun Home(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {

    val searchPostsState = homeViewModel.stateSearchPosts.value
    val pagingState = homeViewModel.paginationState

    val context = LocalContext.current
    var refreshing by homeViewModel.isRefreshing

    var openCard = remember {
      mutableStateOf(false)
    }

    val refreshScope = rememberCoroutineScope()

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(500)
        homeViewModel.refresh()
        refreshing = false
    }

    val pullRefreshState = rememberPullRefreshState(refreshing, { refresh() })

    Scaffold(
        topBar = { Toolbar(navController) }) {

//        val posts by remember { homeViewModel.posts }

        //val stories by StoriesRepository.observeStories()

        if (homeViewModel._tagDialogOpen.value) {
            AddTagDialog(
                onDismiss = homeViewModel::onDismissTagDialog,
                onConfirm = homeViewModel::onConfirmTagDialog,
                toastTagLocationSuccess = Toast.makeText(
                    context,
                    "Uspešno",
                    Toast.LENGTH_SHORT
                ),
                tagLocation = homeViewModel._tags.value
            )
        }
        Box(Modifier.pullRefresh(pullRefreshState)) {
            LazyColumn {

                item {
//        //StoriesSection(stories)
                    Divider()
                    SearchScreen(
                        navController,
                        searchPosts = homeViewModel::search,
                        placeName = homeViewModel._placeName,
                        longitude = homeViewModel._longitude,
                        latitude = homeViewModel._latitude,
                        sortOrder = homeViewModel._sortOrder,
                        sortBy = homeViewModel._sortBy,
                        tags = homeViewModel._tags,
                        deleteTag = homeViewModel::deleteTag,
                        openTagDialog = homeViewModel._tagDialogOpen,
                        deleteCoordinates = homeViewModel::deleteCoordinates
                    )
                    Divider()

                }

                item{
                    if (pagingState.error != null && pagingState.error!!.isNotBlank()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = pagingState.error!!,
                                fontSize = 20.sp,
                                color = dangerColor,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
                item{
                    if (pagingState.endReached && pagingState.items.size == 0) {
                        Column(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Ne postoje objave na ovoj lokaciji", fontSize = 20.sp)
                        }

                    }
                }
                items(pagingState.items.size) { index ->

                    val post = pagingState.items[index]

                    if (index >= pagingState.items.size - 1 && !pagingState.endReached && !pagingState.isLoading) {
                        homeViewModel.loadItems()
                    }

                    Post(post, navController)
                }
                item {
                    if (pagingState.isLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Loader(true)
                        }
                    }
                }

            }
            PullRefreshIndicator(
                refreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter),
                backgroundColor = backgroundColor
            )
        }

    }
}

@Composable
private fun Toolbar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 3.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.dark_logo),
                contentDescription = "brzo_do_lokacije"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
private fun Post(
    post: PostItem,
    navController: NavController
) {

    PostView(post, navController)
}

//@Composable
//private fun StoriesSection(stories: List<Story>) {
//  Column {
//    StoriesList(stories)
//    Spacer(modifier = Modifier.height(10.dp))
//  }
//}

//@Composable
//private fun StoriesList(stories: List<Story>) {
//  LazyRow {
//    itemsIndexed(stories) { index, story ->
//
//      if (index == 0) {
//        Spacer(modifier = Modifier.width(6.dp))
//      }
//
//      Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.padding(vertical = 5.dp, horizontal = 6.dp)
//      ) {
//        StoryImage(imageUrl = story.image)
//        Spacer(modifier = Modifier.height(5.dp))
//        Text(story.name, style = MaterialTheme.typography.caption)
//      }
//
//      if (index == stories.size.minus(1)) {
//        Spacer(modifier = Modifier.width(6.dp))
//      }
//    }
//  }
//}

