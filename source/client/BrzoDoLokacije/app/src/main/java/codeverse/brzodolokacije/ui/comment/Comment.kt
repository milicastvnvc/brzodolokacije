package codeverse.brzodolokacije.ui.comment

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.data.models.Comment
import codeverse.brzodolokacije.data.models.PostItem
import codeverse.brzodolokacije.ui.chat.*
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.theme.*
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.helpers.DateHelper
import coil.compose.rememberImagePainter
import java.util.*
//var replayOnComment = false
//var parentCommentId : Long = -1
//var user = ""
//, replayOnComment : Boolean = false, parentCommentId : Long, user : String = "" ,
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Comment(navController: NavController, postId: Int, commentViewModel: CommentViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val postState = commentViewModel.postState.value
    val addCommentState = commentViewModel.addCommentState.value
    val deleteCommentState = commentViewModel.deleteCommentState.value
    val updateCommentState = commentViewModel.updateCommentState.value

    val comments = remember { commentViewModel.comments }
    val tempComment = remember { commentViewModel.tempCommentToUpdate }

    Column( modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween) {
        if(postState.isLoading || addCommentState.isLoading || deleteCommentState.isLoading || updateCommentState.isLoading){
            Column( modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                Loader(true)
            }

        }
        else{
            postState.data?.let {
                TopSection(navController, post = it)
                Comments(Modifier.weight(1f),comments = it.comments, commentViewModel, navController, tempComment)
                AnswerToSection(commentViewModel)
                CommentSection(2,commentViewModel,navController, postId.toLong())
            }
        }

    }
}
@Composable
fun TopSection(navController: NavController, post: PostItem) {
    Column(
        modifier = Modifier
        .padding(16.dp)
    ) {
        Column {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = lightBackgroundColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.navigateUp()
                    }
            )
            Spacer(modifier = Modifier
                .height(8.dp))
            Column() {
                Row() {
                    Image(
                        painter = rememberImagePainter(Constants.BASE_URL + post!!.createdBy.profilePicturePath),
                        contentDescription = "profile-image",
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        text = "${post.createdBy.username}",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.subtitle2,
                        textAlign = TextAlign.Center
                    )
                }
                if (post.description != null){
                    Text(
                        fontSize = 12.sp,
                        text = post.description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 10.dp, 0.dp, 0.dp),
                        color = lightBackgroundColor,
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
    Divider(modifier = Modifier.padding(16.dp,0.dp))
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Comments(
    modifer: Modifier = Modifier,
    comments: List<Comment>,
    commentViewModel: CommentViewModel,
    navController: NavController,
    tempComment: MutableState<Comment?>
) {
    var updateCommentId : Long = -1
    var commentText : String = ""
    val openDialog = remember { commentViewModel._updateCommentDialog }

    if (openDialog.value) {
        if (tempComment.value != null){
            println("id komentara je ${tempComment.value!!.id}")
            println("tekst komentara je ${tempComment.value!!.text}")
            UpdateComment(
                onDismiss = commentViewModel::onDismissUpdateCommentDialog,
                onConfirm = commentViewModel::onConfirmUpdateCommentDialog,
                text = tempComment.value!!.text,
                commentId = tempComment.value!!.id,
            )
        }
    }

    LazyColumn(modifier = modifer
        .fillMaxSize()
        .padding(16.dp)
    ){
        itemsIndexed(comments) { index, item ->
            val padding = if (item.parentCommentId == null) 0.dp else 10.dp
            Box(
                Modifier
                    .background(color = secondaryColor)
                    .padding(15.dp)) {
            Column(
                modifier = Modifier
                    .padding(0.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row() {
                        Image(
                            painter = rememberImagePainter(Constants.BASE_URL + item!!.createdBy.profilePicturePath),
                            contentDescription = "profile-image",
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape)
                        )
                        Column() {
                            Text(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                text = "${item.createdBy.username}",
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.subtitle2,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                fontSize = 10.sp,
                                text = "${DateHelper.formatApiDate(item.createdDate)}",
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.subtitle2,
                                textAlign = TextAlign.Center
                            )

                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        val commentIcon: Painter = painterResource(id = R.drawable.comment)

                        Icon(
                            painter = commentIcon,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp, 17.dp),
                            tint = lightBackgroundColor
                        )

                        Text(
                            fontSize = 12.sp,
                            text = "${item.childrenComments.size}",
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.subtitle2,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                )
                Text(
                    text = "${item.text}",
                    fontSize = 12.sp,
//                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(
                    modifier = Modifier
                        .height(8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    ClickableText(text = AnnotatedString("Odgovori"), style = TextStyle(
                        color = primaryColor,
                        fontSize = 12.sp
                    ), onClick = {
                        commentViewModel.answerTo.value = item
                    })
                    Row( modifier = Modifier
                        .fillMaxWidth(0.4f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom) {
                        if (commentViewModel.me!!.id == item.createdBy.id) {
                            ClickableText(
                                style = TextStyle(
                                    color = primaryColor,
                                    fontSize = 12.sp
                                ), text = AnnotatedString("Izmeni"), onClick = {
                                    tempComment.value = item
//                                    updateCommentId = item.id
//                                    commentText = item.text
                                    openDialog.value = true

                                })
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                        if (commentViewModel.me!!.id == item.createdBy.id || commentViewModel.isMe()) {
                            ClickableText(text = AnnotatedString("Obriši"),
                                style = TextStyle(
                                    color = dangerColor,
                                    fontSize = 12.sp
                                ),
                                onClick = {
                                    commentViewModel.deleteComment(item.id)
                                })
                        }
                    }
                }
            }
        }
            if (item.childrenComments != null){
                for (child in item.childrenComments) {
                    showChildComment(child, commentViewModel, openDialog, navController, tempComment)
                }
            }

            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
        }
        }
    }
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun showChildComment(
    child: Comment,
    commentViewModel: CommentViewModel,
    openDialog: MutableState<Boolean>,
    navController: NavController,
    tempComment: MutableState<Comment?>
) {

    if (openDialog.value) {
        if (tempComment.value != null){
            println("id komentara je ${tempComment.value!!.id}")
            println("tekst komentara je ${tempComment.value!!.text}")
            UpdateComment(
                onDismiss = commentViewModel::onDismissUpdateCommentDialog,
                onConfirm = commentViewModel::onConfirmUpdateCommentDialog,
                text = tempComment.value!!.text,
                commentId = tempComment.value!!.id,
            )
        }
    }
    Box(
        Modifier
            .background(color = secondaryColor)
            .padding(45.dp, 15.dp, 15.dp, 15.dp)) {
        Column(
            modifier = Modifier
                .padding(0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row() {
                    Image(
                        painter = rememberImagePainter(Constants.BASE_URL + child!!.createdBy.profilePicturePath),
                        contentDescription = "profile-image",
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                    )
                    Column() {
                        Text(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            text = "${child.createdBy.username}",
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.subtitle2,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            fontSize = 10.sp,
                            text = "${DateHelper.formatApiDate(child.createdDate)}",
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.subtitle2,
                            textAlign = TextAlign.Center
                        )

                    }
                }
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    val commentIcon: Painter = painterResource(id = R.drawable.comment)
//
//                    Icon(
//                        painter = commentIcon,
//                        contentDescription = null,
//                        modifier = Modifier.size(17.dp, 17.dp),
//                        tint = lightBackgroundColor
//                    )
//
//                    Text(
//                        fontSize = 12.sp,
//                        text = "${child.childrenComments.size}",
//                        modifier = Modifier.padding(start = 8.dp),
//                        style = MaterialTheme.typography.subtitle2,
//                        textAlign = TextAlign.Center
//                    )
//                }
            }
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            Text(
                text = "${child.text}",
                fontSize = 12.sp,
//                    modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                ClickableText(text = AnnotatedString("Odgovori"), style = TextStyle(
                    color = secondaryColor,
                    fontSize = 12.sp
                ), onClick = {
                    //commentViewModel.answerTo.value = child
                })
                Row( modifier = Modifier
                    .fillMaxWidth(0.4f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom) {
                    if (commentViewModel.me!!.id == child.createdBy.id) {
                        ClickableText(
                            style = TextStyle(
                                color = primaryColor,
                                fontSize = 12.sp
                            ), text = AnnotatedString("Izmeni"), onClick = {
                                tempComment.value = child
                                openDialog.value = true
                            })
                        Spacer(modifier = Modifier.width(5.dp))
                    }

                    if (commentViewModel.me!!.id == child.createdBy.id || commentViewModel.isMe()) {
                        ClickableText(text = AnnotatedString("Obriši"),
                            style = TextStyle(
                                color = dangerColor,
                                fontSize = 12.sp
                            ),
                            onClick = {
                                commentViewModel.deleteComment(child.id)
                            })
                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun CommentSection(
    flag: Int,
    commentViewModel: CommentViewModel,
    navController: NavController,
    postId: Long
) {
    val comment = mutableStateOf("")
    val context = LocalContext.current
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
//    if(replayOnComment) {
//        keyboardController?.show()
//        comment.value = user
//    }
    Card(
        modifier = Modifier
            .padding(bottom = 0.dp)
            .fillMaxWidth()
            .padding(0.dp),
        backgroundColor = backgroundColor
    ) {
            OutlinedTextField(
                placeholder = { Text("Napiši komentar") },
                value = comment.value,
                onValueChange = {
                    comment.value = it
                },
                shape = RoundedCornerShape(25.dp),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_dm),
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier.clickable(
                            onClick = {
                                if (comment.value.isNotBlank()) {
                                    if (commentViewModel.answerTo.value != null) {
                                        commentViewModel.addComment(
                                            postId,
                                            comment.value,
                                            commentViewModel.answerTo.value!!.id
                                        )
                                    } else {
                                        commentViewModel.addComment(postId, comment.value)
                                    }
                                    println("dodaje se na id=" + postId + " vrednost " + comment.value)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Ne mozete objaviti prazan komentar",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        )
                    )

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }

}

@Composable
fun AnswerToSection(commentViewModel: CommentViewModel){

    if (commentViewModel.answerTo.value != null) {
        val parent = commentViewModel.answerTo.value
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        )
        {
            Text(text = "Odgovori @" + parent!!.createdBy.username)
            Spacer(modifier = Modifier.width(10.dp))
            FloatingActionButton(
                modifier = Modifier
                    .wrapContentSize(Alignment.CenterEnd)
                    .size(20.dp),
                backgroundColor = primaryColor,
                onClick = {
                    commentViewModel.answerTo.value = null
                }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close",
                    tint = lightBackgroundColor
                )

            }
        }
    }
}
@Composable
fun UpdateComment(
    onDismiss: () -> Unit,
    onConfirm: (commentId: Long, text: String) -> Unit,
    text: String,
    commentId: Long
) {
    var updateText by remember { mutableStateOf("") }
    updateText = text
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = "Izmeni komentar")
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                })
            {
                Text(text = "Odustani", color = primaryColor)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(commentId,updateText)
                }) {
                Text(text = "Izmeni", color = primaryColor)
            }
        },
        text = {
            Column() {
                OutlinedTextField(
                    value = updateText,
                    onValueChange = { updateText = it },
//                    label = { Text(text = "Tag") },
                    singleLine = true
                )
            }
        }
    )
}