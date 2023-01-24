package codeverse.brzodolokacije


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.ui.components.BottomBarScreen
import codeverse.brzodolokacije.ui.home.components.icon
import codeverse.brzodolokacije.ui.nav.BottomNavGraph
import codeverse.brzodolokacije.ui.theme.*
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Routes
import coil.compose.rememberImagePainter

//import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val sectionState = remember { mutableStateOf(BottomBarScreen.Search) }
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController, mainViewModel) }
    ) {
            innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        Crossfade(
            modifier = modifier,
            targetState = sectionState.value
        ){
            BottomNavGraph(navController = navController)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, mainViewModel: MainViewModel) {
    val screens = listOf(
        BottomBarScreen.Search,
        BottomBarScreen.AddPost,
        BottomBarScreen.Favourite,
        BottomBarScreen.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(backgroundColor = secondaryColor) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController,
                currentUser = mainViewModel.current_user
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    currentUser: User? = null
) {
    val selectedIcons = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val selected = screen == currentDestination
    BottomNavigationItem(

        icon = {
            if (screen == BottomBarScreen.Profile) {
                BottomBarProfile(selected, currentUser)
            }
            else{
                if(selectedIcons){
                    Icon(
                        imageVector = ImageVector.vectorResource(id = screen.icon_filled),
                        contentDescription = "Navigation Icon",
                    )
                }
                else {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = screen.icon),
                        contentDescription = "Navigation Icon",
                    )
                }
            }
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = unselectedBottomBarScreen,
        selectedContentColor = selectedBottomBarScreen,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}


@Composable
private fun BottomBarProfile(isSelected: Boolean, currentUser: User?) {
    val shape = CircleShape

    val borderModifier = if (isSelected) {
        Modifier
            .border(
                color = Color.LightGray,
                width = 1.dp,
                shape = shape
            )
    } else Modifier

    val padding = if (isSelected) 3.dp else 0.dp

    Box(
        modifier = borderModifier
    ) {
        Box(
            modifier = Modifier
                .icon()
                .padding(padding)
                .background(color = Color.LightGray, shape = shape)
                .clip(shape)
        ) {
            Image(
                painter = rememberImagePainter(Constants.BASE_URL + currentUser!!.profilePicturePath),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}








