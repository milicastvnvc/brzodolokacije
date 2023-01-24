package codeverse.brzodolokacije.ui.favourites

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import codeverse.brzodolokacije.R

import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.ui.post.component.RatingTag
import codeverse.brzodolokacije.ui.theme.*
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.helpers.NumberHelper
import coil.compose.rememberImagePainter

@Composable
fun ItemUserCard(user: User,
                 onItemClicked: (user: User) -> Unit,
                 onLikeClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = {
                onItemClicked(user)
            }),
        elevation = 0.dp,
        backgroundColor = favoriteItemUserCard
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {

            val image: Painter = rememberImagePainter(Constants.BASE_URL + user.profilePicturePath)
            Image(
                modifier = Modifier
                    .size(80.dp, 80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                painter = image,
                alignment = Alignment.CenterStart,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier
                .align(Alignment.CenterVertically)
                .fillMaxWidth(0.7f)) {
                Text(
                    text = user.username,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = secondaryColor,
                    fontWeight = FontWeight.Bold,
                    style = typography.subtitle1,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = buildString {
                        append(user.firstName)
                        append(" ")
                        append(user.lastName)
                    },
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colors.surface,
                    style = typography.caption,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                RatingTag(rating = NumberHelper.roundDoubleNumber(user.avgRating))
            }
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.End
            ) {

                FloatingActionButton(
                    modifier = Modifier.size(40.dp),
                    onClick = {
                        //dislajkuj
                        onLikeClick()
                    },
                    backgroundColor = likeColor
                ) {
                    val owner: Painter = painterResource(id = R.drawable.ic_filled_favorite)
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = owner,
                        contentDescription = "",
                        tint = lightBackgroundColor
                    )
                }

//                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
//
//                }
            }
        }
    }
}
