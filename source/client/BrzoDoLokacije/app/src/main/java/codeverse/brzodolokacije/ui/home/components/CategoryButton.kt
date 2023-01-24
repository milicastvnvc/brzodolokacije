package codeverse.brzodolokacije.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.ui.home.CategoryTag
import codeverse.brzodolokacije.ui.profile.StoryHighlight
import codeverse.brzodolokacije.ui.search.RoundImage
import codeverse.brzodolokacije.ui.theme.secondaryColor

@Composable
fun CategorySection(
    modifier: Modifier = Modifier,
    categories: List<CategoryTag>,

    ) {

    LazyRow(modifier = modifier) {
        items(categories.size) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier
//                    .padding(end = 15.dp)
//                    .clickable{}
//            ) {
//                RoundImage(
//                    modifier = Modifier.size(70.dp),
//                    profileImage = categories[it].categoryImage
//                )
//                Text(
//                    text = categories[it].categoryText,
//                    overflow = TextOverflow.Ellipsis,
//                    textAlign = TextAlign.Center
//                )
//            }

            Column(
                modifier
                    .width(72.dp)
                    .padding(end = 15.dp)
                    .clickable { },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier
                        .size(72.dp)
                        .background(
                            color = secondaryColor,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(18.dp)
                ) {
                    Image(
                        painter = categories[it].categoryImage,
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    text = categories[it].categoryText,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }

//    Column(Modifier.padding(horizontal = 16.dp)) {
//        Row(
//            Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(text = "Category", style = MaterialTheme.typography.h6)
//            TextButton(onClick = {}) {
//                Text(text = "More", color = MaterialTheme.colors.primary)
//            }
//        }
//        Row(
//            Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            CategoryButton(
//                text = "Fruits",
//                icon = painterResource(id = R.drawable.brzo_do_lokacije_logo),
//                backgroundColor = Color(0xffFEF4E7)
//            )
//            CategoryButton(
//                text = "Vegetables",
//                icon = painterResource(id = R.drawable.brzo_do_lokacije_logo),
//                backgroundColor = Color(0xffF6FBF3)
//            )
//            CategoryButton(
//                text = "Dairy",
//                icon = painterResource(id = R.drawable.brzo_do_lokacije_logo),
//                backgroundColor = Color(0xffFFFBF3)
//            )
//            CategoryButton(
//                text = "Meats",
//                icon = painterResource(id = R.drawable.brzo_do_lokacije_logo),
//                backgroundColor = Color(0xffF6E6E9)
//            )
//            CategoryButton(
//                text = "Meats",
//                icon = painterResource(id = R.drawable.brzo_do_lokacije_logo),
//                backgroundColor = Color(0xffF6E6E9)
//            )
//        }
//    }
}

@Composable
fun CategoryButton(
    text: String = "",
    icon: Painter,
    backgroundColor: Color,
    onCluck: () -> Unit
) {
    Column(
        Modifier
            .width(72.dp)
            .clickable { }
    ) {
        Box(
            Modifier
                .size(72.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(18.dp)
        ) {
            Image(painter = icon, contentDescription = "", modifier = Modifier.fillMaxSize())
        }
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
}