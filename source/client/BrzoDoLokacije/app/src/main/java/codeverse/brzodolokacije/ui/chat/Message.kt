package codeverse.brzodolokacije.ui.chat

import java.util.Calendar

data class Message (
    var text : String?= null,
    var recipient_id: String,
    var time: Long = Calendar.getInstance().timeInMillis,
    var isOut: Boolean = false
        )

val message_dummy = listOf(
    Message(
        text = "E top, tad cemo onda stvarno da pricamo",
        recipient_id = "user",
        isOut = true
    ),
    Message(
        text = "Ne brini sredice me za koji dan",
        recipient_id = "bot",
        isOut = false
    ),
    Message(
        text = "Ova sto radi front ne zna drugacije",
        recipient_id = "bot",
        isOut = false
    ),
    Message(
        text = "Sto si hradkodiran?",
        recipient_id = "user",
        isOut = true
    ),
    Message(
        text = "Cao",
        recipient_id = "user",
        isOut = true
    ),
    Message(
        text = "Cao!",
        recipient_id = "bot",
        isOut = false
    )


)