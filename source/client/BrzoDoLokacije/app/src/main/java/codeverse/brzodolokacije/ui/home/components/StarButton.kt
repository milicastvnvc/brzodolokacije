package codeverse.brzodolokacije.ui.home.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import codeverse.brzodolokacije.R

enum class StarAnimationState {
    Initial,
    Start,
    End
}

private const val springRatio = Spring.DampingRatioHighBouncy

@Composable
fun AnimStarButton(
    idStar: Int,
    performRate: (Int) -> Unit,
    myRate: MutableState<Int>
) {
    var transitionState by remember {
        mutableStateOf(MutableTransitionState(StarAnimationState.Initial))
    }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .clickable(
                onClick = {
                    transitionState = MutableTransitionState(StarAnimationState.Start)
//                    onStarClick.invoke(post,idStar)
//                    coroutineScope.launch {
//                        PostsRepository.toggleStar(post.id.toLong(), idStar)
//                    }
                    performRate(idStar + 1)

                }
            )
            .indication(
                indication = rememberRipple(bounded = false, radius = 24.dp),
                interactionSource = remember { MutableInteractionSource() }
            )
//            .padding(vertical = 10.dp)
            .then(Modifier.size(30.dp)),
        contentAlignment = Alignment.Center
    ) {

        var starIconRes by remember { mutableStateOf(R.drawable.ic_outlined_star) }
        val startColor = contentColorFor(MaterialTheme.colors.background)
        val endColor = Color.Yellow

        if (transitionState.currentState == StarAnimationState.Start) {
            transitionState.targetState = StarAnimationState.End
        }

        val transition = updateTransition(transitionState, label = "")

        val animatedColor by transition.animateColor(
            transitionSpec = {
                when {
                    StarAnimationState.Initial isTransitioningTo StarAnimationState.Start ->
                        spring(dampingRatio = springRatio)
                    StarAnimationState.Start isTransitioningTo StarAnimationState.End ->
                        tween(200)
                    else -> snap()
                }
            }, label = ""
        ) { state ->
            when (state) {
                StarAnimationState.Initial -> if (myRate.value >= idStar + 1) endColor else startColor
                else -> if (myRate.value < idStar + 1) startColor else endColor
            }
        }

        val size by transition.animateDp(
            transitionSpec = {
                when {
                    StarAnimationState.Initial isTransitioningTo StarAnimationState.Start ->
                        spring(dampingRatio = springRatio)
                    StarAnimationState.Start isTransitioningTo StarAnimationState.End ->
                        tween(200)
                    else -> snap()
                }
            }, label = ""
        ) { state ->
            when (state) {
                StarAnimationState.Initial -> 24.dp
                StarAnimationState.Start -> 12.dp
                StarAnimationState.End -> 24.dp
            }
        }

        starIconRes = if (myRate.value >= idStar + 1) {
            R.drawable.ic_filled_star
        } else {
            R.drawable.ic_outlined_star
        }

        Icon(
            ImageBitmap.imageResource(id = starIconRes), tint = animatedColor,
            modifier = Modifier.size(size),
            contentDescription = ""
        )
    }
}


//@Composable
////@Preview
//private fun StarButtonPreview() {
//    AnimStarButton(
//        post = Post(
//            id = 1,
//            image = "https://source.unsplash.com/random/400x300",
//            user = User(
//                name = names.first(),
//                username = names.first(),
//                image = "https://randomuser.me/api/portraits/men/1.jpg"
//            ),
//            likesCount = 100,
//            commentsCount = 20,
//            timeStamp = System.currentTimeMillis() - (60000),
//
//            name = "post",
//            location = "Kragujevac",
//            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s"
//        ),
//        onStarClick = {
//        },1)
//}