package codeverse.brzodolokacije.ui.post.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.data.models.maps.MapUsage
import codeverse.brzodolokacije.ui.home.components.AnimStarButton
import codeverse.brzodolokacije.ui.theme.dangerColor
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.utils.helpers.DateHelper
import codeverse.brzodolokacije.utils.Routes
import codeverse.brzodolokacije.utils.helpers.NumberHelper
import kotlin.reflect.KMutableProperty0

@Composable
fun PostInfoCard(
    navController: NavController,
    post: PostItem,
    performRate: (Int) -> Unit,
    myRate: MutableState<Int>,
    avgRating: KMutableProperty0<MutableState<Double>>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(
                text = post.text,
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = lightBackgroundColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                val locationIcon: Painter = painterResource(id = R.drawable.ic_location)

                Icon(
                    painter = locationIcon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp, 16.dp),
                    tint = dangerColor
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    fontSize = 14.sp,
                    text = post.placeName,
//                    modifier = Modifier.padding(8.dp, 12.dp, 12.dp, 0.dp),
                    modifier = Modifier.fillMaxWidth(0.65f)
                        .clickable {
                        navController.navigate(Routes.MAPS + "/${MapUsage.ShowOne.ordinal}"+ "/${post.latitudeCenter},${post.longitudeCenter}")
                    },
                    color = primaryColor,
                    style = MaterialTheme.typography.caption
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = DateHelper.formatApiDate(post.dateCreated),
                    fontSize = 10.sp,
//                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    modifier = Modifier.fillMaxWidth(1f),
                    color = lightBackgroundColor,
                    style = MaterialTheme.typography.overline
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

//            Text(
//                text = dateCreated,
//                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
//                color = lightBackgroundColor,
//                style = MaterialTheme.typography.overline
//            )
            Box() {
            Row(
                modifier = Modifier.fillMaxWidth(0.75f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimStarButton(0, performRate, myRate)
                AnimStarButton(1, performRate, myRate)
                AnimStarButton(2, performRate, myRate)
                AnimStarButton(3, performRate, myRate)
                AnimStarButton(4, performRate, myRate)
//                Spacer(modifier = Modifier.width(105.dp))

            }
                Row(
                    modifier = Modifier.fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ){
                RatingTag(NumberHelper.roundDoubleNumber(avgRating.get().value))
                }
            }
        }

    }
}

