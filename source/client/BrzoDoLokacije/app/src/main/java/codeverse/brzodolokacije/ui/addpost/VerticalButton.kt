package codeverse.brzodolokacije.ui.addpost

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun VerticalButton(modifier: Modifier, vector: Int, text: String, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                imageVector = ImageVector.vectorResource(id = vector), contentDescription = ""
            )

            Text(text = text, fontWeight = FontWeight.Bold, maxLines = 2, softWrap = true, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}