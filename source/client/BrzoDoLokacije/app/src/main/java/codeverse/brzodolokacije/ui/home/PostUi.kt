package codeverse.brzodolokacije.ui.home

import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.data.models.maps.MapUsage
import codeverse.brzodolokacije.ui.home.components.*
import codeverse.brzodolokacije.ui.post.component.RatingTag
import codeverse.brzodolokacije.ui.post.detail.DetailsViewModel
import codeverse.brzodolokacije.ui.post.slider.ImageSlider
import codeverse.brzodolokacije.ui.theme.dangerColor
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.helpers.DateHelper
import codeverse.brzodolokacije.utils.Routes
import codeverse.brzodolokacije.utils.helpers.NumberHelper
import coil.compose.rememberImagePainter
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun PostView(
    post: PostItem,
    navController: NavController,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    Column {
        PostHeader(navController, post, detailsViewModel)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(color = Color.LightGray)
                .clickable {
                    navController.navigate(Routes.DETAILS + "/${post.id}")
                }
        ) {
            ImageSlider(navController, post = post)
        }

        PostFooter(navController, post)
        Divider()
    }
}

@Composable
private fun PostHeader(
    navController: NavController,
    post: PostItem,
    detailsViewModel: DetailsViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                navController.navigate(Routes.PROFILE+"/"+post.createdBy.id)
            }
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(color = Color.LightGray, shape = CircleShape)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = rememberImagePainter(Constants.BASE_URL + post.createdBy.profilePicturePath),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = post.createdBy.username, style = MaterialTheme.typography.subtitle2)
        }
//        Icon(Filled.MoreVert, "")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun PostFooter(
    navController: NavController,
    post: PostItem) {

    PostFooterIconSection(navController, post)
    PostFooterTextSection(post)
}

@Composable
private fun PostFooterIconSection(
    navController: NavController,
    post: PostItem) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)) {

                val locationIcon: Painter = painterResource(id = R.drawable.ic_location)

                Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()){
                    Row (modifier = Modifier.fillMaxWidth(0.7f)) {
                        Icon(
                            painter = locationIcon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp, 18.dp),
                            tint = dangerColor
                        )

                        Text(
                            text = post.text,
                            modifier = Modifier
                                .padding(3.dp)
                                .clickable {
                                    navController.navigate(Routes.MAPS + "/${MapUsage.ShowOne.ordinal}" + "/${post.latitudeCenter},${post.longitudeCenter}")
                                },
                            color = primaryColor,
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.caption,
                            maxLines = 2,
                            softWrap = true,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically) {
                        RatingTag(NumberHelper.roundDoubleNumber(post.avgRating))
                    }
                }


            }
            Spacer(modifier = Modifier.width(8.dp))

        }

//        PostIconButton {
//            Icon(ImageVector.vectorResource(id = R.drawable.ic_outlined_bookmark), "")
//        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun PostFooterTextSection(post: PostItem) {
    Column(
        modifier = Modifier.padding(
            start = horizontalPadding,
            end = horizontalPadding,
            bottom = verticalPadding
        )
    ) {
        Text(
            post.numberOfComments.toString() + " komentara",
            style = MaterialTheme.typography.caption
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = DateHelper.formatApiDate(post.dateCreated),
            style = MaterialTheme.typography.caption.copy(fontSize = 10.sp)
        )
    }
}

private fun Long.getTimeElapsedText(): String {
    val now = System.currentTimeMillis()
    val time = this

    return DateUtils.getRelativeTimeSpanString(
        time, now, 0L, DateUtils.FORMAT_ABBREV_TIME
    )
        .toString()
}