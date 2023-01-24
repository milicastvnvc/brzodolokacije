package codeverse.brzodolokacije.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import codeverse.brzodolokacije.ui.theme.textColor

@Composable
fun Logo(
    text : String
){
    val image = painterResource(R.drawable.logo_with_shadow_2)

    Image(painter = image, contentDescription = "")
    Spacer(modifier = Modifier.padding(10.dp))
    Text(text = text,style = TextStyle(color = textColor,fontWeight = FontWeight.Bold, fontSize = 30.sp, letterSpacing = 2.sp))
}