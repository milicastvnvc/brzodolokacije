package codeverse.brzodolokacije.ui.profile

import androidx.compose.ui.graphics.painter.Painter

data class StoryHighlight(
    val image: Painter,
    val text: String,
    val onClick: () -> Unit = {}
)
