package codeverse.brzodolokacije.ui.nav

//import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import codeverse.brzodolokacije.ui.addpost.AddPostScreen
import codeverse.brzodolokacije.ui.chat.ChatScreen
import codeverse.brzodolokacije.ui.comment.Comment
import codeverse.brzodolokacije.ui.components.BottomBarScreen
import codeverse.brzodolokacije.ui.favourites.Favourites
import codeverse.brzodolokacije.ui.home.Home
import codeverse.brzodolokacije.ui.image.ImageScreen
import codeverse.brzodolokacije.ui.maps.MapScreen
import codeverse.brzodolokacije.ui.post.detail.Details
import codeverse.brzodolokacije.ui.profile.EditProfileScreen
import codeverse.brzodolokacije.ui.profile.ProfileScreen
//import codeverse.brzodolokacije.ui.search.SearchScreen
import codeverse.brzodolokacije.utils.Routes

//import com.example.mapsproject.MapScreen
//import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Search.route
    ) {
        composable(route = "${Routes.PROFILE}") {
            ProfileScreen(navController)
        }
        composable(route = "${Routes.PROFILE}/{profileId}") {
            ProfileScreen(navController)
        }
        composable(route = Routes.EDIT_PROFILE) {
            EditProfileScreen(navController)
        }
        composable(
            "${Routes.DETAILS}/{postDetailId}",
//            enterTransition = { _, _ ->
//                slideInHorizontally(
//                    initialOffsetX = { 300 },
//                    animationSpec = tween(
//                        durationMillis = 300,
//                        easing = FastOutSlowInEasing
//                    )
//                ) + fadeIn(animationSpec = tween(300))
//            },
//            exitTransition = { _, _ ->
//                slideOutHorizontally(
//                    targetOffsetX = { 300 },
//                    animationSpec = tween(
//                        durationMillis = 300,
//                        easing = FastOutSlowInEasing
//                    )
//                ) + fadeOut(animationSpec = tween(300))
//            },
            arguments = listOf(navArgument("postDetailId") { type = NavType.IntType })
        ) {
            Details(navController, it.arguments?.getInt("postDetailId") ?: 0)
        }
        composable(route = "${Routes.MAPS}") {
            MapScreen(navController, {}, {})
        }
        composable(route = "${Routes.MAPS}/{mapsEnum}") {
            MapScreen(navController, {}, {})
        }
        composable(route = "${Routes.MAPS}/{mapsEnum}/{mapParametar}") {
            MapScreen(navController, {}, {})
        }
        composable(route = "${Routes.MAPS}/{mapsEnum}/{latitude},{longitude}") {
            MapScreen(navController, {}, {})
        }
        composable(route = BottomBarScreen.Search.route) {
            //SearchScreen(navController)
            Home(navController)
        }
        composable(route = BottomBarScreen.Search.route+"/{tag}") {
            //SearchScreen(navController)
            Home(navController)
        }
        composable(route = BottomBarScreen.Search.route+"/{searchLatitude},{searchLongitude},{placeName}") {
            //SearchScreen(navController)
            Home(navController)
        }
        composable(route = BottomBarScreen.AddPost.route) {
            AddPostScreen(navController)
        }
//        composable(route = BottomBarScreen.Reals.route) {
//            VideoScreen()
//        }
        composable(route = Routes.CHAT) {
            ChatScreen()
        }
        composable(route = Routes.FAVOURITES) {
            Favourites(navController)
        }
        composable(route = Routes.IMAGE) {
            ImageScreen(navController)
        }
//        /{replayOnComment}/{parentId}/{user}
        composable("${Routes.COMMENT}/{commentDetailId}",
            arguments = listOf(navArgument("commentDetailId") { type = NavType.IntType }),
//            arguments = listOf(navArgument("replayOnComment") { type = NavType.BoolType }),
//            arguments = listOf(navArgument("parentId") { type = NavType.IntType }),
//            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) {
            Comment(navController,it.arguments?.getInt("commentDetailId") ?: 0,
//                it.arguments?.getBoolean("replayOnComment") ?: false,
//                it.arguments?.getLong("parentId") ?: 0,it.arguments?.getString("user") ?: ""
            )
        }
    }
}