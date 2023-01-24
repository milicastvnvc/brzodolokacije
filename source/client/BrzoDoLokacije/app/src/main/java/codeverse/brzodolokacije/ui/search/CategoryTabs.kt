package codeverse.brzodolokacije.ui.search

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.ui.search.data.CategoriesRepository
import codeverse.brzodolokacije.ui.search.model.Category
import codeverse.brzodolokacije.ui.theme.McComposeTheme

@Composable
fun CategoryTabs(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = categories.indexOf(selectedCategory),
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onSurface,
        edgePadding = 8.dp,
        indicator = {},
        divider = {}
    ) {
        categories.forEach { category ->
            CategoryTab(
                category = category,
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
    }
}


private enum class CategoryTabState { Selected, NotSelected }

@Composable
private fun CategoryTab(
    category: Category,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val transition = updateTransition(if (selected) CategoryTabState.Selected else CategoryTabState.NotSelected)

    val backgroundColor by transition.animateColor(label = "") { state ->
        when (state) {
            CategoryTabState.Selected -> MaterialTheme.colors.primary
            CategoryTabState.NotSelected -> MaterialTheme.colors.background
        }
    }
    val contentColor by transition.animateColor(label = "") { state ->
        when (state) {
            CategoryTabState.Selected -> LocalContentColor.current
            CategoryTabState.NotSelected -> MaterialTheme.colors.primary
        }
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = backgroundColor,
        contentColor = contentColor,
        border = BorderStroke(2.dp, MaterialTheme.colors.primary)
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            NetworkImage(
//                imageUrl = category.image,
//                contentScale = ContentScale.Fit,
//                modifier = Modifier
//                    .wrapContentWidth()
//                    .height(32.dp),
//                previewPlaceholder = R.drawable.category_fries
//            )
            Icon(//modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                imageVector = ImageVector.vectorResource(id = category.image), contentDescription = "")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.button,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 1.25.sp
            )
        }
    }
}
