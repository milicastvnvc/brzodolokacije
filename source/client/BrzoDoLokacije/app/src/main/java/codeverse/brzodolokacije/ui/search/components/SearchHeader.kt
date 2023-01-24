package codeverse.brzodolokacije.ui.search.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.data.models.maps.MapUsage
import codeverse.brzodolokacije.utils.Routes

@Composable
fun SearchHeader(navController: NavController,
                 searchFieldState: MutableState<String>,
                 searchPosts: () -> Unit){
    Box {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .offset(0.dp, (-15).dp),
            painter = painterResource(id = R.drawable.bg_main),
            contentDescription = "Header Background",
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(searchFieldState, searchPosts, text = "Unesite lokaciju")

            Spacer(modifier = Modifier.height(15.dp))

            SearchByCoordinatesButton(
                vector = R.drawable.explore_outlined,
                text = "Pretražite na mapi",
                onClick = {
                    navController.navigate(Routes.MAPS + "/${MapUsage.Search.ordinal}")
                }
            )
        }
    }
}