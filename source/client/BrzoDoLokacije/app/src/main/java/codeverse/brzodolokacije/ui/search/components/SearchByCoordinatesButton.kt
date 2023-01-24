package codeverse.brzodolokacije.ui.search.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import codeverse.brzodolokacije.domain.use_case.maps.SearchByCoordinatesUseCase
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.ui.theme.secondaryColor

@Composable
fun SearchByCoordinatesButton(vector: Int,
                              text: String,
                              onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
    ) {

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(
                    vertical = 1.dp,
                    horizontal = 4.dp
                ),
                imageVector = ImageVector.vectorResource(id = vector),
                contentDescription = "",
                tint = primaryColor
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                maxLines = 2,
                softWrap = true,
                fontWeight = FontWeight.Bold,
                color = secondaryColor,
                style = TextStyle(textAlign = TextAlign.Center)
            )

            //Spacer(modifier = Modifier.height(8.dp))
        }
    }
}