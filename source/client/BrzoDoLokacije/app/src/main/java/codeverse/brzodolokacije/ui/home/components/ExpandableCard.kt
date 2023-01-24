package codeverse.brzodolokacije.ui.home.components

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import codeverse.brzodolokacije.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
    visibleContent: @Composable() () -> Unit,
    hiddenContent: @Composable() () -> Unit
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "")

    val cardElevation by transition.animateDp({
        tween(durationMillis = 500)
    }, label = "") {
        if (expanded) 24.dp else 4.dp
    }
    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    }, label = "") {
        if (expanded) 0.dp else 16.dp
    }
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = 500)
    }, label = "") {
        if (expanded) 0f else 180f

    }

    Card(
        backgroundColor = backgroundColor,
        elevation = cardElevation,
        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()) {
            Box {
                visibleContent()
            }
            ExpandableContent(visible = expanded,
                initialVisibility = expanded,
                hiddenContent = hiddenContent)
            Box(modifier = Modifier.fillMaxWidth()){
                CardArrow(
                    degrees = arrowRotationDegree,
                    onClick = onCardArrowClick,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

    }
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_upward_24),
                contentDescription = "Expandable Arrow",
                modifier = Modifier.rotate(degrees),
            )
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContent(
    visible: Boolean = true,
    initialVisibility: Boolean = false,
    hiddenContent: @Composable() () -> Unit
) {
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(500)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(500)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            // Expand from the top.
            shrinkTowards = Alignment.Top,
            animationSpec = tween(500)
        ) + fadeOut(
            // Fade in with the initial alpha of 0.3f.
            animationSpec = tween(500)
        )
    }
    AnimatedVisibility(
        visible = visible,
        initiallyVisible = initialVisibility,
        enter = enterTransition,
        exit = exitTransition
    ) {
        Column() {
            hiddenContent()
        }

    }
}