package codeverse.brzodolokacije.ui.profile

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.*
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.data.models.maps.MapUsage
import codeverse.brzodolokacije.data.paginator.ScreenState
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.favourites.Favourites
import codeverse.brzodolokacije.ui.home.components.icon
import codeverse.brzodolokacije.ui.post.CustomAlert
import codeverse.brzodolokacije.ui.search.components.SearchByCoordinatesButton
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.dangerColor
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import codeverse.brzodolokacije.ui.theme.likeColor
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Routes
import codeverse.brzodolokacije.utils.helpers.NumberHelper
import coil.compose.rememberImagePainter



@OptIn(ExperimentalMaterialApi::class)
@Composable
//@Preview
fun ProfileScreen(navController: NavController,
                  profileViewModel: ProfileViewModel = hiltViewModel()){
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }

    var user: User? = null
    val context = LocalContext.current

    val userState = profileViewModel.userState.value
    val postsState = profileViewModel.postsState.value
    val likeState = profileViewModel.stateLikeUser.value
    val isLikedIndicator by remember { profileViewModel._isLikedIndicator }
    var posts = remember { profileViewModel._posts }

    val pagingState = profileViewModel.paginationState

    if (userState.success){
        user = userState.data
    }
    else if (userState.error.isNotBlank()){
        Toast.makeText(context,userState.error,Toast.LENGTH_LONG).show()
        navController.navigateUp()
    }

    user?.let {

        if (likeState.error.isNotBlank()){
            Toast.makeText(context, likeState.error, Toast.LENGTH_LONG).show()
        }

        val profileImage: Painter = rememberImagePainter(Constants.BASE_URL + user.profilePicturePath)
        Column(modifier = Modifier.fillMaxSize()) {

            TopBar(navController = navController,
                profileViewModel= profileViewModel,
                name = user.username,
                modifier = Modifier.padding(10.dp),
                isLiked = isLikedIndicator,
                profileViewModel._isMe)

            if (profileViewModel._openDialogLogout.value){
                CustomAlert(onDismiss = {
                                        profileViewModel._openDialogLogout.value = false
                },
                onConfirm = {
                    profileViewModel.logout()
                    context.startActivity(Intent(context, LoginActivity::class.java))
                },
                text = "Da li sigurno želite da se odjavite?")
            }

            Spacer(modifier = Modifier.height(4.dp))

            var postsNum: Int = 0
            var avgRating: Double = 0.0
            var numOfRatings: Int = 0
            if(postsState.data != null){
                postsNum = postsState.data.numberOfPosts
                avgRating = postsState.data.avgRating
                numOfRatings = postsState.data.numberOfRatings

            }
            ProfileSection(user.firstName + " " + user.lastName,
                profileImage = profileImage, postsNum = postsNum,
                avgRating = avgRating, numOfRatings = numOfRatings)

            Spacer(modifier = Modifier.height(25.dp))
            ButtonSection(navController, modifier = Modifier.fillMaxWidth(), user, profileViewModel)
            Spacer(modifier = Modifier.height(25.dp))
//        HiglightSection(
//            highlights = listOf(
//                StoryHighlight(image = painterResource(id = R.drawable.barcelona), text = "Japan"),
//                StoryHighlight(image = painterResource(id = R.drawable.barcelona), text = "Hawaii"),
//                StoryHighlight(image = painterResource(id = R.drawable.barcelona), text = "Barcelona"),
//                StoryHighlight(image = painterResource(id = R.drawable.barcelona), text = "Munich"),
//                StoryHighlight(image = painterResource(id = R.drawable.barcelona), text = "NewYork"),
//                //StoryHighlight(image = painterResource(id = R.drawable.telegram), text = "Telegeram"),
//                ),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp)
//        )
            Spacer(modifier = Modifier.height(10.dp))
            PostTabView(
                imageWithTexts = listOf(
                    StoryHighlight(image = painterResource(id = R.drawable.ic_grid), text = "Objave"),
                    //StoryHighlight(image = painterResource(id = R.drawable.ic_outlined_favorite), text = "Omiljeni"),
                    //StoryHighlight(image = painterResource(id = R.drawable.ic_outline_location_on_24), text = "Mapa")
//                StoryHighlight(image = painterResource(id = R.drawable.profile), text = "Profile"),
                )
            ) {
                selectedTabIndex = it
            }
            when(selectedTabIndex) {
                0 -> PostSection(
                    posts = posts.value,
                    modifier = Modifier.fillMaxWidth(),
                    profileViewModel = profileViewModel,
                    pagingState = pagingState,
                    navController = navController
                )
//                1 -> FavoriteSection(
//                    modifier = Modifier.fillMaxWidth(),
//                    navController = navController
//                )
//            2 ->
//
//                MapSection(posts = posts.value,
//                viewModel = profileViewModel,
//                navController = navController)
            }
        }
    }

}

@Composable
fun TopBar(navController: NavController,
    profileViewModel: ProfileViewModel,
    name: String,
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    isMe: MutableState<Boolean>
){
    var expanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start){
            if(!isMe.value){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = lightBackgroundColor,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navController.navigateUp()
                        }
                )
                Spacer(modifier = Modifier.width(15.dp))
            }
            Text(
                text = name,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier=Modifier.padding(start = 24.dp))
        if(!isMe.value){
            val owner: Painter
            if (isLiked){
                owner = painterResource(id = R.drawable.ic_filled_favorite)
            }
            else{
                owner = painterResource(id = R.drawable.ic_outlined_favorite)
            }
            Icon(
                modifier = Modifier
                    .icon()
                    .clickable {
                        profileViewModel.likeUser(!isLiked)

                    },
                painter = owner,
                contentDescription = "",
                tint = if(isLiked) likeColor else lightBackgroundColor
            )
        }

        if(isMe.value){
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End){

                Icon(Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = lightBackgroundColor,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            navController.navigate(Routes.EDIT_PROFILE)
                        }
                )
                Spacer(modifier = Modifier.width(15.dp))
                Icon(Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = lightBackgroundColor,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            profileViewModel._openDialogLogout.value = true
                })

            }

        }


    }

}


@Composable
fun ProfileSection(
    name: String,
    modifier: Modifier = Modifier,
    profileImage: Painter,
    postsNum: Int,
    avgRating: Double,
    numOfRatings: Int
){
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            ProfileStat(numberText = NumberHelper.roundDoubleNumber(avgRating).toString(), text = "Ocena",modifier = Modifier
                .size(100.dp)
                .weight(3f))

            RoundImage(
                profileImage = profileImage,
                modifier = Modifier
                    .size(100.dp)
                    .weight(4f)

            )
            ProfileStat(numberText = numOfRatings.toString(), text = "Br.ocena",modifier = Modifier
                .size(100.dp)
                .weight(3f))
        }
        Spacer(modifier = Modifier.width(15.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            ProfileStat(numberText = postsNum.toString(), text = "Objava")
        }
        Spacer(modifier = Modifier.width(10.dp))
        ProfileDescription(
            displayName = name,
            //description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer t ",
            //url = "https://www.instagram.com/",
            //followedBy = listOf("sadasd","sdadsadas","dsadasdasd", "DSADAS"),
            //otherCount = 14
        )
    }
}

@Composable
fun RoundImage(
    modifier: Modifier = Modifier,
    profileImage: Painter
){
    Image(
        painter = profileImage,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(width = 1.dp, color = lightBackgroundColor, shape = CircleShape)
            .padding(3.dp)
            .clip(CircleShape)
    )

}

//@Composable
//fun StatSection (modifier: Modifier = Modifier){
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceAround,
//        modifier = modifier
//    ){
//        ProfileStat(numberText = "601", text = "Posts")
//        ProfileStat(numberText = "96.8K", text = "Followers")
//        ProfileStat(numberText = "71", text = "Following")
//    }
//}

@Composable
fun ProfileStat(
    numberText: String, // ISPRAVITI OVO
    text: String,
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = numberText,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = text)
    }
}

@Composable
fun ProfileDescription(
    displayName: String,
    //description: String,
    //url: String,
    //followedBy: List<String>,
    //otherCount: Int
){
    val letterSpacing = 0.5.sp
    val lineHeight = 20.sp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = displayName,
            fontWeight = FontWeight.Bold,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight,
            fontSize = 18.sp
        )
//        Text(
//            text = description,
//            letterSpacing = letterSpacing,
//            lineHeight = lineHeight
//        )
//        Text(
//            text = url,
//            color = Color(0xFF3D3D91),
//            letterSpacing = letterSpacing,
//            lineHeight = lineHeight
//        )
//        if(followedBy.isNotEmpty()){
//            Text(
//                text = buildAnnotatedString {
//                    val boldStyle = SpanStyle(
//                        color = Color.Black,
//                        fontWeight = FontWeight.Bold
//                    )
//                    append("Followed by ")
//
//                    followedBy.forEachIndexed {
//                        index,
//                        name -> pushStyle(boldStyle)
//                        append(name)
//                        pop()
//                        if(index < followedBy.size - 1){
//                            append(", ")
//                        }
//                    }
//                    if(otherCount > 2){
//                        append(" and ")
//                        pushStyle(boldStyle)
//                        append("$otherCount others")
//                    }
//                },
//                letterSpacing = letterSpacing,
//                lineHeight = lineHeight
//
//            )
//        }

    }
}

@Composable
fun ButtonSection(
    navController: NavController,
    modifier: Modifier = Modifier,
    user: User,
    viewModel: ProfileViewModel
) {
    val minWidth = 95.dp
    val height = 35.dp

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {

        SearchByCoordinatesButton(vector =R.drawable.explore_outlined , text = "Mapa") {
            navController.navigate(Routes.MAPS + "/" + MapUsage.ShowMany.ordinal + "/" + user.id)
        }
//        ActionButton(
//            text = "Mapa",
//            modifier = Modifier
//                .defaultMinSize(minWidth = minWidth)
//                .height(height)
//                .clickable {
//                    navController.navigate(Routes.MAPS + "/" + MapUsage.ShowMany.ordinal + "/" + user.id)
//                }
//        )
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null
){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = lightBackgroundColor,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(6.dp)
    ){
        if(text != null){
            Text(text = text)
        }
        if(icon != null){
            Icon(imageVector = icon, contentDescription = null, tint = lightBackgroundColor)
        }
    }
}

@Composable
fun FavoriteSection(modifier: Modifier = Modifier,
                    navController: NavController)
{
    Favourites(navController)

}

@Composable
fun HiglightSection(
    modifier: Modifier = Modifier,
    highlights: List<StoryHighlight>
){
    LazyRow(modifier = modifier){
        items(highlights.size){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(end = 15.dp)
            ) {
//                RoundImage(
//                    modifier = Modifier.size(70.dp),
//                    //profileImage = profileImage
//                )
                Text(
                    text = highlights[it].text,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PostTabView(
    modifier: Modifier = Modifier,
    imageWithTexts: List<StoryHighlight>,
    onTabSelected: (selectedIndex: Int) -> Unit
){
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    var inactiveColor = Color(0xFF777777)
    TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = Color.Transparent,
        contentColor = lightBackgroundColor,
        modifier = modifier
    ) {
        imageWithTexts.forEachIndexed {
            index, item ->
            Tab(
                selected = selectedTabIndex == index,
                selectedContentColor = Color.White,
                unselectedContentColor = inactiveColor,
                onClick = {
                    selectedTabIndex = index
                    onTabSelected(index)
                }
            ) {
                Icon(
                    painter = item.image, //painterResource(id = R.drawable.ic_grid),
                    contentDescription = item.text,//"Posts",
                    tint = if(selectedTabIndex == index) Color.White else inactiveColor,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(20.dp)
                )
            }
        }

    }
}

@Composable
fun PostSection(
    posts: MutableList<PostItem>,
    modifier: Modifier = Modifier,
    pagingState: ScreenState<PostItem>,
    navController: NavController,
    profileViewModel: ProfileViewModel
){

    if (pagingState.error != null && pagingState.error!!.isNotBlank()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
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
            Text("Nema objava")
        }


    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.scale(1.01f)

    ) {
        var painter: Painter

        items(pagingState.items.size) { index ->

            val post = pagingState.items[index]

            if(index >= pagingState.items.size - 1 && !pagingState.endReached && !pagingState.isLoading){
                profileViewModel.loadItems()
            }

            painter = rememberImagePainter(Constants.BASE_URL + post.resources[0].path)
            //painters.add(it, rememberImagePainter(Constants.BASE_URL + posts[it].resources[0].path))
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .border(width = 1.dp, color = backgroundColor)
                    .clickable {
                        navController.navigate(Routes.DETAILS + "/${post.id}")
                    }
            )
        }
        item{
            if (pagingState.isLoading){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.Center){
                    Loader(true)
                }
            }
        }
//        items(posts.size){
//                painter = rememberImagePainter(Constants.BASE_URL + posts[it].resources[0].path)
//                //painters.add(it, rememberImagePainter(Constants.BASE_URL + posts[it].resources[0].path))
//                Image(
//                    painter = painter,
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .aspectRatio(1f)
//                        .border(width = 1.dp, color = backgroundColor)
//                        .clickable {
//                            navController.navigate(Routes.DETAILS + "/${posts[it].id}")
//                        }
//                )
//        }
    }
}

