package codeverse.brzodolokacije.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.utils.Routes

sealed class BottomBarScreen(
    val route: String,
    val icon: Int,
    val icon_filled: Int,
) {
    object Home : BottomBarScreen(
        route = "home",
        icon = R.drawable.travel_explore_24px,
        icon_filled = R.drawable.travel_explore_fill
    )

    object Profile : BottomBarScreen(
        route = Routes.PROFILE,
        icon =  R.drawable.account_circle_24px_outlined,
        icon_filled = R.drawable.account_circle_24px_filled
    )

    object Search : BottomBarScreen(
        route = Routes.SEARCH,
        icon = R.drawable.travel_explore_24px,
        icon_filled = R.drawable.travel_explore_fill
    )
    object AddPost : BottomBarScreen(
        route = Routes.ADD_POST,
        icon = R.drawable.add_box_24px_outlined,
        icon_filled = R.drawable.add_box_24px_filled
    )
    object Favourite : BottomBarScreen(
        route = Routes.FAVOURITES,
        icon = R.drawable.ic_baseline_favorite_border_24,
        icon_filled = R.drawable.ic_baseline_favorite_24
    )
}
