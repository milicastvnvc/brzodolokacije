package codeverse.brzodolokacije.ui.post.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor

@Composable
fun RatingTag(rating: Double) {
    val color = when {
        rating == 0.0  -> Color.Gray
        rating >= 3.5  -> primaryColor
        rating > 2.5  -> Color.Yellow
        else          -> Color.Red
    }
    ChipView(grade = rating, colorResource = color)
}

@Composable
fun ChipView(grade: Double, colorResource: Color) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(12.dp))
            //.background(colorResource.copy(1f))
//            .padding(end = 2.dp)
            .background(colorResource.copy(.75f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp, 6.dp, 12.dp, 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                ImageBitmap.imageResource(id = R.drawable.ic_filled_star), tint = Color.Yellow,
                modifier = Modifier.size(14.dp),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = grade.toString(),
                fontSize = 16.sp,
                style = MaterialTheme.typography.caption,
                color = lightBackgroundColor,
                fontWeight = FontWeight.Bold
            )
        }

    }
}
