/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codeverse.brzodolokacije.ui.favourites


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.post.CustomAlert
import codeverse.brzodolokacije.ui.search.components.SearchBar
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.dangerColor
import codeverse.brzodolokacije.utils.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Favourites(navController: NavController,
               favouritesViewModel: FavouritesViewModel = hiltViewModel()) {

    val pagingState = favouritesViewModel.paginationState
    val searchFieldState = remember { favouritesViewModel.searchTerm }
    val searchFavourites = favouritesViewModel::searchFavourites
    val likeState = favouritesViewModel.stateLikeUser.value
    val dislikeDialogOpen = remember { favouritesViewModel._dislikeDialogOpen }

    val refreshScope = rememberCoroutineScope()
    var refreshing by favouritesViewModel.isRefreshing

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(500)
        favouritesViewModel.refresh()
        refreshing = false
    }

    val pullRefreshState = rememberPullRefreshState(refreshing, { refresh() })

    val context = LocalContext.current

    Scaffold { padding ->

        val pad = padding

        if (likeState.error.isNotBlank()){
            Toast.makeText(context, likeState.error, Toast.LENGTH_LONG).show()
        }
        if (dislikeDialogOpen.value){
            CustomAlert(onDismiss = favouritesViewModel::onDissmisDialog,
                onConfirm = favouritesViewModel::onConfirmDialog,
                text = "Da li sigurno želite da izbacite korisnika iz liste omiljenih?")
        }

        Box(Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
            if (pagingState.error != null && pagingState.error!!.isNotBlank()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
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
            if (pagingState.endReached && pagingState.items.size == 0) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Nema lajkovanih korisnika")
                }
            }
            LazyColumn {
                item {
                    TopBar(navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item{
                    SearchBar(searchFieldState,searchFavourites, text = "Pretražite omiljene korisnike")
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(pagingState.items.size) { index ->

                    val user = pagingState.items[index]

                    if(index >= pagingState.items.size - 1 && !pagingState.endReached && !pagingState.isLoading){
                        favouritesViewModel.loadItems()
                    }

                    ItemUserCard(
                        user,
                        onItemClicked = { user ->
                            navController.navigate(Routes.PROFILE+"/"+user.id)
                        },
                        onLikeClick = {
                            dislikeDialogOpen.value = true
                            favouritesViewModel._userToDislike.value = user
                        }
                    )

                }
                item{
                    if (pagingState.isLoading){
                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.Center){
                            Loader(true)
                        }
                    }
                }
//        itemsIndexed(userList) { index, user ->
//            ItemUserCard(
//                user,
//                onItemClicked = { user ->
//                    navController.navigate(Routes.PROFILE+"/"+user.id)
//                }
//            )
//        }

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
