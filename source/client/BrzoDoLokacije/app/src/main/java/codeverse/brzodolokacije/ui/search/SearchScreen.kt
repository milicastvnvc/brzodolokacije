package codeverse.brzodolokacije.ui.search

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.data.models.search.PostSortBy
import codeverse.brzodolokacije.data.models.search.SortOrder
import codeverse.brzodolokacije.ui.addpost.tags.TagsScreen
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.home.components.ExpandableCard
import codeverse.brzodolokacije.ui.home.components.icon
import codeverse.brzodolokacije.ui.profile.StoryHighlight
import codeverse.brzodolokacije.ui.search.components.SearchHeader
import codeverse.brzodolokacije.ui.search.data.CategoriesRepository
import codeverse.brzodolokacije.ui.theme.*
import codeverse.brzodolokacije.utils.helpers.NumberHelper
import kotlin.reflect.KFunction0

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel(),
    searchPosts: () -> Unit,
    placeName: MutableState<String>,
    longitude: MutableState<Double?>,
    latitude: MutableState<Double?>,
    sortOrder: MutableState<SortOrder>,
    sortBy: MutableState<PostSortBy>,
    tags: MutableState<MutableList<String>>,
    deleteTag: (tag: String) -> Unit,
    openTagDialog: MutableState<Boolean>,
    deleteCoordinates: (string: String) -> Unit
) {

    val searchPostsState = searchViewModel.stateSearchPosts.value
    val searchFieldState = remember { placeName }
    val sortOrder = remember { sortOrder }
    val sortBy = remember { sortBy }
    val tags = remember { tags }

    var openCard = remember {
        mutableStateOf(false)
    }

    //val data by viewModel.data.observeAsState(Menu(emptyList(), emptyList()))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (searchPostsState.isLoading) {
            Loader(true)
        } else {

            ExpandableCard(
                onCardArrowClick = {
                    openCard.value = !openCard.value
                    println("Kliknuo sam") },
                expanded = openCard.value,
                visibleContent = {
                    SearchHeader(navController, searchFieldState, searchPosts)
                },
                hiddenContent = {
                    SearchBody(navController, sortOrder, sortBy, tags, searchPosts, openTagDialog, deleteTag)
                }
            )

        }
    }

    if (longitude.value != null && latitude.value != null){
        Row(verticalAlignment = Alignment.CenterVertically){

                Text(text = "Koordinate:", modifier = Modifier.padding(top = 10.dp, start = 5.dp))
                Spacer(modifier = Modifier.width(5.dp))
                TagsScreen(
                    navController = navController,
                    tagLocation = mutableListOf(NumberHelper.roundDoubleNumberToThreeDecimals(latitude.value!!).toString() +", " + NumberHelper.roundDoubleNumberToThreeDecimals(longitude.value!!).toString()),
                    deleteTag = deleteCoordinates,
                    isEditable = true
                )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }

    if (tags.value.size > 0) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(text = "Tagovi:", modifier = Modifier.padding(top = 10.dp, start = 5.dp))
            Spacer(modifier = Modifier.width(5.dp))

            TagsScreen(
                navController = navController,
                tagLocation = tags.value!!,
                deleteTag = deleteTag,
                isEditable = true
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }


}

@Composable
fun SearchBody(
    navController: NavController,
    sortOrder: MutableState<SortOrder>,
    sortBy: MutableState<PostSortBy>,
    tags: MutableState<MutableList<String>>,
    searchPosts: () -> Unit,
    openTagDialog: MutableState<Boolean>,
    deleteTag: (tag: String) -> Unit
) {
    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        Text(
            text = "Najpopularniji tagovi:",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
    HiglightSection(
        highlights = listOf(
            StoryHighlight(
                image = painterResource(id = R.drawable.minimal_sea),
                text = "More",
                onClick = {
                    tags.value.add("more")
                    searchPosts()
                }
            ),
            StoryHighlight(
                image = painterResource(id = R.drawable.minimal_mountain),
                text = "Planina",
                onClick = {
                    tags.value.add("planina")
                    searchPosts()
                }
            ),
            StoryHighlight(
                image = painterResource(id = R.drawable.minimal_lake),
                text = "Jezero",
                onClick = {
                    tags.value.add("jezero")
                    searchPosts()
                }
            ),
            StoryHighlight(
                image = painterResource(id = R.drawable.minimal_river),
                text = "Reka",
                onClick = {
                    tags.value.add("reka")
                    searchPosts()
                }
            ),
            StoryHighlight(
                image = painterResource(id = R.drawable.minimal_city),
                text = "Grad",
                onClick = {
                    tags.value.add("grad")
                    searchPosts()
                }
            ),
        ),
        modifier = Modifier
            .fillMaxWidth()
    )


    // -------------- SORTIRANJE -----------
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Sortiraj po:",
            color = lightBackgroundColor,
            style = MaterialTheme.typography.body1
        )

        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(modifier = Modifier//.padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.CenterEnd)
                .border(
                    border = BorderStroke(
                        2.dp,
                        MaterialTheme.colors.primary
                    ),
                    shape = RoundedCornerShape(5.dp)
                )
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(5.dp)
                ),
                onClick = {
                    if (sortOrder.value == SortOrder.Descending)
                        sortOrder.value = SortOrder.Ascending
                    else sortOrder.value = SortOrder.Descending
                    searchPosts()
                }) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (sortOrder.value == SortOrder.Descending) "Opadajuće" else "Rastuće",
                        color = primaryColor,
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        painter = painterResource(
                            id = if (sortOrder.value == SortOrder.Ascending) R.drawable.arrow_circle_up else R.drawable.arrow_circle_down),
                        contentDescription = "",
                        modifier = Modifier
                            .icon()
                            .background(backgroundColor),
                        tint = primaryColor
                    )
                }

            }

        }

    }

    Spacer(modifier = Modifier.height(8.dp))
    Divider()
    val categories = CategoriesRepository.getCategoriesData()
    //val data by viewModel.data.observeAsState(Menu(emptyList(), emptyList()))
    CategoryTabs(
        categories = categories,//  data.categories,
        selectedCategory = categories.find { category -> category.id == sortBy.value.ordinal }!!,

        onCategorySelected = { category ->
            sortBy.value = PostSortBy.values()[category.id]
            searchPosts()
            //coroutineScope.launch { }//lazyListState.scrollToItem(category.getIndex(data)) }
        }
    )

//  --------------- STARI SORT ---------------------
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
////                    .padding(horizontal = 15.dp, vertical = 10.dp),
//                horizontalArrangement = Arrangement.SpaceAround,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Column(
//                    modifier = Modifier
//                        //.fillMaxWidth(0.32f)
////                        .padding(horizontal = 10.dp)
//                        .clickable {
//                            sortBy.value = PostSortBy.CreatedDate
//                            searchPosts()
//                        },
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//
//                    Icon(
//                        Icons.Default.DateRange,
//                        contentDescription = "Back",
//                        tint = if (sortBy.value == PostSortBy.CreatedDate) primaryColor else lightBackgroundColor,
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Text(
//                        text = "",//"Datum",
//                        color = if (sortBy.value == PostSortBy.CreatedDate) primaryColor else lightBackgroundColor
//                    )
//                }
//
//                Column(
//                    modifier = Modifier
//                        //.fillMaxWidth(0.32f)
////                        .padding(horizontal = 10.dp)
//                        .clickable {
//                            sortBy.value = PostSortBy.Rating
//                            searchPosts()
//                        },
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//
//                    Icon(
//                        painter = painterResource(R.drawable.ic_outlined_star),
//                        contentDescription = "Back",
//                        tint = if (sortBy.value == PostSortBy.Rating) primaryColor else lightBackgroundColor,
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Text(
//                        text = "",//""Ocena",
//                        color = if (sortBy.value == PostSortBy.Rating) primaryColor else lightBackgroundColor
//                    )
//                }
//                Column(
//                    modifier = Modifier
//                        //.fillMaxWidth(0.32f)
////                        .padding(horizontal = 10.dp)
//                        .clickable {
//                            sortBy.value = PostSortBy.NumberOfComments
//                            searchPosts()
//                        },
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_outlined_comment),
//                        contentDescription = "Back",
//                        tint = if (sortBy.value == PostSortBy.NumberOfComments) primaryColor else lightBackgroundColor,
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Text(
//                        text = "Komentar",
////                        maxLines = 2,
////                        softWrap = true,
//                        textAlign = TextAlign.Center,
//                        color = if (sortBy.value == PostSortBy.NumberOfComments) primaryColor else lightBackgroundColor
//                    )
//                }
//            }
    Divider()
    Spacer(modifier = Modifier.height(8.dp))
    Column () {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.5f),
                text = "Pretražite objave po tagovima:",
                color = lightBackgroundColor,
                style = MaterialTheme.typography.body1,
                maxLines = 2,
                softWrap = true,
                textAlign = TextAlign.Left,
                fontSize = 15.sp
            )
            AddTagButton(modifier = Modifier.fillMaxWidth(0.8f), vector = R.drawable.tag,
                text = "Dodaj tag", onClick = {
                    openTagDialog.value = true
                }, color = lightBackgroundColor)
        }
        Spacer(modifier = Modifier.width(6.dp))
//                FloatingActionButton(
//                    modifier = Modifier
//                        .wrapContentSize(Alignment.TopStart)
//                        .padding(all = 5.dp)
//                        .size(20.dp),
//                    backgroundColor = primaryColor,
//                    onClick = {
//                        println("Klikno sam na dugme")
//                        //otvori mu dialog za dodavanje taga
//                        openTagDialog.value = true
//
//                    }) {
//                    Icon(
//                        imageVector = Icons.Outlined.Tag,
//                        contentDescription = "Add"
//                    )
//
//                }

    }
//    Spacer(modifier = Modifier.height(3.dp))

}

@Composable
fun HiglightSection(
    modifier: Modifier = Modifier,
    highlights: List<StoryHighlight>
) {

    LazyRow(modifier = modifier, contentPadding = PaddingValues(start = 15.dp)) {
        items(highlights.size) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(end = 15.dp)
            ) {
                RoundImage(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(color = secondaryColor).clickable(onClick = {
                            println("Klik na dugme")
                            highlights[it].onClick()
                        }),
                    profileImage = highlights[it].image
                )
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
fun RoundImage(
    modifier: Modifier = Modifier,
    profileImage: Painter
) {
    Image(
        painter = profileImage,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(width = 2.dp, color = primaryColor, shape = CircleShape)
            .padding(3.dp)
            .clip(CircleShape)
    )

}

//@SuppressLint("RememberReturnType")
//@Composable
//fun SortByIcon(
//    modifier: Modifier = Modifier,
//    vector: Int,
//    text: String,
//    onClick: () -> Unit,
//    sortBy: MutableState<PostSortBy>,
//) {
//    val sortBy = remember { sortBy }
//    Column(
//        modifier = Modifier
//            .fillMaxWidth(0.33f)
//            .padding(horizontal = 10.dp)
//            .clickable {
//                onClick
////                sortBy.value = PostSortBy.CreatedDate
////                searchPosts()
//            },
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Icon(
//            imageVector = ImageVector.vectorResource(id = vector),
//            contentDescription = "Back",
//            tint = if (sortBy.value == PostSortBy.CreatedDate) primaryColor else lightBackgroundColor,
//            modifier = Modifier.size(24.dp)
//        )
//        Text(
//            text = text,
//            color = if (sortBy.value == PostSortBy.CreatedDate) primaryColor else lightBackgroundColor
//        )
//    }
//}


@Composable
fun AddTagButton(modifier: Modifier, vector: Int, text: String, onClick: () -> Unit, color: Color) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(vertical = 3.dp, horizontal = 2.dp),
                imageVector = ImageVector.vectorResource(id = vector), contentDescription = "", tint = color
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
                softWrap = true,
                textAlign = TextAlign.Center)
        }
    }
}

//private fun Int.getCategory(menu: Menu): Category {
//    return menu.categories.last { it.getIndex(menu) <= this }
//}
//
//private fun Category.getIndex(menu: Menu): Int {
//    var index = 0
//    for (i in 0 until menu.categories.indexOf(this)) {
//        index += 1
//        index += menu.menuItems.filter { it.categoryId == menu.categories[i].id }.size
//    }
//    return index
//}