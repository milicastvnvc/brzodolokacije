package codeverse.brzodolokacije.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.ui.theme.secondaryColor
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen() {
    val context = LocalContext.current
    Column( modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween) {
        TopBarSection(
            username = "Hardcode: user",
            profile = painterResource(id = R.drawable.barcelona),
            isOnline = true //hardcode opet
        )
        ChatSection(Modifier.weight(1f))
        MessageSection()

    }
}
val message = mutableStateOf("")
@Composable
fun MessageSection() {
    val context = LocalContext.current
    Card (
        modifier = Modifier
            .padding(bottom = 60.dp)
            .fillMaxWidth()
            .padding(0.dp),
        backgroundColor = backgroundColor,
        elevation = 10.dp
            ) {
        OutlinedTextField(placeholder = { Text("Poruka..") },
        value = message.value,
            onValueChange = {
                message.value = it
            },
        shape = RoundedCornerShape(25.dp),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dm),
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.clickable{}
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                )
            }
    }


@Composable
fun ChatSection(modifer: Modifier = Modifier) {
    val simpleDateFormat = SimpleDateFormat("h:mm a", Locale.FRANCE)
    LazyColumn(modifier = modifer
        .fillMaxSize()
        .padding(16.dp),
        reverseLayout = true
    ){
        items(message_dummy) { chat ->
            MessageItem (
                messageText = chat.text,
                time = simpleDateFormat.format(chat.time),
                isOut = chat.isOut,
                    )
            Spacer(modifier = Modifier.height(8.dp))
            
        }        
    } 
}

@Composable
fun MessageItem(messageText: String?, time: String?, isOut: Boolean) {
    val BotChatBubbleShape = RoundedCornerShape(0.dp,8.dp,8.dp,8.dp)
    val AuthorChatBubbleShape = RoundedCornerShape(8.dp,0.dp,8.dp,8.dp)

    Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = if(isOut) Alignment.End else Alignment.Start
        ) {
            if(messageText != null) {
                if(messageText != "") {
                    Box(
                        modifier = Modifier
                            .background(
                                if (isOut) primaryColor else secondaryColor,
                                shape = if (isOut) AuthorChatBubbleShape else BotChatBubbleShape
                            )
                            .padding(
                                top = 8.dp,
                                bottom = 8.dp,
                                start = 16.dp,
                                end = 16.dp
                            )
                    ) {
                        Text (
                            text = messageText,
                            color = Color.White
                                )
                    }
                }
            }
        if (time != null) {
            Text(text = time,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp)

            )
        }

        }
}

@Composable
fun TopBarSection(username: String, profile: Painter, isOnline: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        backgroundColor = backgroundColor,
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = profile,
                contentDescription = "profile-image",
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(text = username, fontWeight = FontWeight.SemiBold)
                Text(
                    text = if (isOnline) "Na mrezi" else "Van mreze",
                    fontSize = 12.sp
                )

            }
        }
    }
}

@Composable
@Preview
fun ChatScreenPreview() {
    ChatScreen()
}