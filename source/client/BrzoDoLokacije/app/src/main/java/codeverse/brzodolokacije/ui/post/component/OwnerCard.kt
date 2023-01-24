package codeverse.brzodolokacije.ui.post.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.ui.post.detail.DetailsViewModel
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import codeverse.brzodolokacije.ui.theme.likeColor
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.utils.Routes
import coil.compose.rememberImagePainter

@Composable
fun OwnerCard(navController: NavController,
              user: User,
              name: String,
              bio: String,
              image: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate(Routes.PROFILE+"/"+user.id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // getting the image from the drawable
        val personImage: Painter = rememberImagePainter(image)

        Image(
            modifier = Modifier
                .size(60.dp, 60.dp)
                .clip(RoundedCornerShape(16.dp)),
            painter = personImage,
            alignment = Alignment.CenterStart,
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier) {
            Text(
                text = name,
                color = lightBackgroundColor,
                style = typography.subtitle1,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = bio,
                color = lightBackgroundColor,
                style = typography.caption
            )
        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically) {
            FloatingActionButton(
                modifier = Modifier.size(40.dp),
                onClick = {
                    navController.navigate(Routes.PROFILE+"/"+user.id)
                },
                backgroundColor = primaryColor
            ) {
                val owner: Painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_24)
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = owner,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }
    }
}
