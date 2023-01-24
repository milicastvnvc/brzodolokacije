package codeverse.brzodolokacije.ui.addpost

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.data.models.Image
import codeverse.brzodolokacije.ui.addpost.alert.ImageLimitAlert
import codeverse.brzodolokacije.ui.addpost.tags.AddTagDialog
import codeverse.brzodolokacije.ui.addpost.tags.TagsScreen
import codeverse.brzodolokacije.ui.camera.CameraScreen
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.maps.MapScreen
import codeverse.brzodolokacije.ui.theme.*
import codeverse.brzodolokacije.utils.MyState
import codeverse.brzodolokacije.utils.Routes
import coil.compose.rememberImagePainter


//import coil.compose.rememberImagePainter


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddPostScreen(
    navController: NavController,
    addPostViewModel: AddPostViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val descriptionValue = remember { addPostViewModel._description }
    var tagLocation by remember { addPostViewModel._tagLocation }

    val imageBool = remember { addPostViewModel._imageBool }
    val openCamera = remember { addPostViewModel._cameraOpen }
    val openDialog = remember { addPostViewModel._tagDialogOpen }

    //Toasts
    val toastImage =
        Toast.makeText(context, "Obavezno je postaviti barem jednu sliku", Toast.LENGTH_LONG)
    val toastDescription = Toast.makeText(context, "Morate uneti kratak opis", Toast.LENGTH_LONG)
    val toastLocation = Toast.makeText(context, "Morate izabrati lokaciju", Toast.LENGTH_LONG)
    val toastSuccess = Toast.makeText(context, "Uspešno dodata objava", Toast.LENGTH_LONG)
    val toastTagLocationFail =
        Toast.makeText(context, "Ne mozete uneti prazan tag", Toast.LENGTH_LONG)
    val toastTagLocationSuccess =
        Toast.makeText(context, "Uspešno ste dodali tag", Toast.LENGTH_LONG)

    val addPostState = addPostViewModel.stateAddPost.value
    val uploadImagesState = addPostViewModel.stateUploadImages.value

    var selectImages by remember { addPostViewModel._images }
    var tempImages by remember { mutableStateOf(mutableListOf<Uri>()) }

    val focusManager = LocalFocusManager.current

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            if (uris.size > 0){
                tempImages = uris as MutableList<Uri>
                addPostViewModel.addImages(tempImages)
            }

        }

    if (openDialog.value) {
        AddTagDialog(
            onDismiss = addPostViewModel::onDismissTagDialog,
            onConfirm = addPostViewModel::onConfirmTagDialog,
            toastTagLocationSuccess,
            tagLocation
        )
    }

    if (addPostViewModel._mapOpen.value) { //Prikazuje mapu
        MapScreen(
            navController = navController,
            onConfirmMap = addPostViewModel::onConfirmMap,
            onDismissMap = addPostViewModel::onDismissMap
        )
    } else if (openCamera.value) {
        CameraScreen(navController, addPostViewModel::onCameraImageSaved) {
            openCamera.value = false
        }
    } else { //Prikazuje dodavanje posta

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(backgroundColor)
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { focusManager.clearFocus() }
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (uploadImagesState.success) { //Kada je uspesno dodao post, preusmeri ga
                toastSuccess.show()
                navController.navigate(Routes.SEARCH) {
                    //popUpTo = navController.graph.startDestination
                    launchSingleTop = true
                }
            } else if (addPostState.isLoading || uploadImagesState.isLoading) { //loader
                Loader(isDisplayed = true)
            } else {
                Spacer(modifier = Modifier.padding(10.dp))
                if (selectImages.isNotEmpty()) {

//                HorizontalScrollScreen(selectImages)
                    var items = selectImages
                    // a wrapper to fill the entire screen
                    Box() {

                        BoxWithConstraints() {
                            // LazyRow to display your items horizontally
                            LazyRow(
//                              modifier = Modifier.fillMaxWidth(),
                                state = rememberLazyListState()
                            ) {
                                itemsIndexed(items) { index, item ->
                                    Card(
                                        modifier = Modifier
                                            .height(200.dp)
                                            .width(200.dp) // here is the trick
                                            .padding(10.dp)
                                    ) {
                                        Image(
                                            painter = rememberImagePainter(item.uri),
//                                        contentScale = ContentScale.FillBounds,
                                            contentDescription = null,
//                                        contentScale = ContentScale.Crop,
//                                        modifier = Modifier.size(200.dp).clip(RoundedCornerShape(16.dp))
//                                        modifier = Modifier
//                                            .padding(16.dp, 8.dp)
//                                            .size(100.dp)
//                                            .clickable {
//
//                                            }
                                        ) // card's content
                                        FloatingActionButton(
                                            modifier = Modifier
                                                .wrapContentSize(Alignment.TopEnd)
                                                .padding(all = 5.dp)
                                                .size(20.dp),
                                            backgroundColor = backgroundColor,
                                            onClick = {
                                                addPostViewModel.removeImage(index)

                                            }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Close,
                                                contentDescription = "Close"
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                //if(selectImages.isEmpty()){ //nije dodata ni jedna slika, prikaz samo dugmica
                Text(text = "Dodajte fotografiju", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    //DUGME ZA BIRANJE SLIKE IZ GALERIJE
                    Button(
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp),
                        onClick = {
                            galleryLauncher.launch("image/*")
                            addPostViewModel._imageAlert.value = ""
                        }
                    )
                    {
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                            Icon(painter = painterResource(id = R.drawable.add_photo_alternate_filled),
                                modifier = Modifier.fillMaxWidth(0.60f),
                                tint = lightBackgroundColor,
                                contentDescription = "")
                            Text(text = "Galerija")
                        }


                    }
                    //Spacer(Modifier.width(10.dp))

                    //DUGME ZA SLIKANJE SLIKE
                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp),
                        onClick = {
                            openCamera.value = true
                            addPostViewModel._imageAlert.value = ""
                        },
                    ){
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                            Icon(painter = painterResource(id = R.drawable.add_a_photo_filled),
                                modifier = Modifier.fillMaxWidth(0.60f),
                                tint = lightBackgroundColor,
                                contentDescription = "")
                            Text(text = "Kamera")
                        }

                    }
                }
                Spacer(modifier = Modifier.padding(5.dp))

                if (imageBool.value) {
                    ImageLimitAlert {
                        imageBool.value = false
                    }
                }
                if (addPostViewModel._imageAlert.value.isNotBlank()) {
                    Text(
                        text = addPostViewModel._imageAlert.value,
                        color = dangerColor
                    )
                }
                Divider(modifier = Modifier.padding(8.dp))
                AddingPost(
                    descriptionValue,
                    addPostViewModel,
                    navController,
                    selectImages,
                    toastImage,
                    toastLocation,
                    toastDescription,
                    uploadImagesState,
                    context,
                    tagLocation,
                    openDialog
                )
                if (addPostState.error.isNotBlank()) { //prikaz greske
                    Toast.makeText(context, addPostState.error, Toast.LENGTH_LONG).show()
                }
            }

        }
    }

}

@SuppressLint("SuspiciousIndentation")
@Composable
fun AddingPost(
    descriptionValue: MutableState<String?>,
    addPostViewModel: AddPostViewModel,
    navController: NavController,
    selectImages: MutableList<Image>,
    toastImage: Toast,
    toastLocation: Toast,
    toastDescription: Toast,
    uploadImageState: MyState<Unit>,
    context: Context,
    tagLocation: MutableList<String>,
    openDialog: MutableState<Boolean>
) {
//    val focusManager = LocalFocusManager.current
    Spacer(modifier = Modifier.height(5.dp))
//    Row(
//        horizontalArrangement = Arrangement.SpaceBetween,
//        modifier = Modifier.fillMaxWidth(0.85f)
//    ) {
////        Text("Despription")
////        Text("Max character 500")
//    }
//    Spacer(modifier = Modifier.padding(3.dp))
    OutlinedTextField(
        value = descriptionValue.value!!,
        onValueChange = { descriptionValue.value = it },
        label = { Text(text = "Dodaj opis") },
        placeholder = { Text(text = "Dodaj opis") },
        singleLine = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = primaryColor,
            focusedLabelColor = primaryColor,
            cursorColor = primaryColor
        ),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(100.dp),
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onTap = { focusManager.clearFocus() }
//                )
//            },

        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
    Spacer(modifier = Modifier.padding(10.dp))

    Column() {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp),
                onClick = {
                    addPostViewModel._mapOpen.value = true
                },
            ){
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Icon(painter = painterResource(id = R.drawable.add_location_alt_filled),
                        modifier = Modifier.fillMaxWidth(0.60f),
                        tint = lightBackgroundColor,
                        contentDescription = "")
                    Text(text = "Odaberi lokaciju", textAlign = TextAlign.Center)
                }

            }
            //DUGME ZA DODAVANJE LOKACIJE
//            VerticalButton(
//                modifier = Modifier.fillMaxWidth(0.25f),
//                vector = R.drawable.add_location_alt_filled,
//                text = "Odaberi Lokaciju",
//                onClick = {
//                    addPostViewModel._mapOpen.value = true
//                },
//            )

            //Spacer(Modifier.width(50.dp))


            //DUGME ZA DODAVANJE TAGA
            Button(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp),
                onClick = {
                    openDialog.value = true
                },
            ){
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Icon(painter = painterResource(id = R.drawable.sell_filled),
                        modifier = Modifier.fillMaxWidth(0.60f),
                        tint = lightBackgroundColor,
                        contentDescription = "")
                    Text(text = "Dodaj tag", textAlign = TextAlign.Center)
                }

            }
//            VerticalButton(
//                modifier = Modifier.fillMaxWidth(0.4f),
//                vector = R.drawable.sell_filled,
//                text = "Dodaj Tag",
//                onClick = {
//                    openDialog.value = true
//                }
//            )
            //Spacer(Modifier.width(50.dp))


            //VerticalButton(vector = Icons.Filled.Business, text = "Hotel")
            //VerticalButton(vector = Icons.Filled.LocalShipping, text = "Cruise")

        }
    }


//    Row() {
//        Text(
//            text = "Odaberi lokaciju na mapi",
//            color = primaryColor,
//            modifier = Modifier.clickable(onClick = {
//                //navController.navigate(Routes.MAPS + "/${MapUsage.Add.ordinal}")
//                addPostViewModel._mapOpen.value = true
//            })
//        )
//    }
    Spacer(modifier = Modifier.padding(10.dp))
    Divider()
    Spacer(modifier = Modifier.padding(5.dp))
    if (addPostViewModel._location.value != null) {
        Text(text = "Odabrali ste lokaciju: ", fontWeight = FontWeight(500), fontSize = 17.sp)
        Spacer(modifier = Modifier.height(5.dp))
        Row (horizontalArrangement = Arrangement.Center) {
            Icon(
                painterResource(id = R.drawable.ic_location),
                modifier = Modifier.size(20.dp, 20.dp),
                tint = dangerColor,
                contentDescription = ""
            )
//            Icon(painter = painterResource(id = R.drawable.map_location_icon),tint = secondaryColor,  contentDescription = "")
            Text(
                modifier = Modifier.fillMaxWidth(0.9f),
                text = addPostViewModel._location.value!!.place_name,
                color = primaryColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                softWrap = true,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Divider()
        Spacer(modifier = Modifier.height(5.dp))
    }
//    ExtendedFloatingActionButton(
//        modifier = Modifier
//            .width(170.dp)
//            .height(35.dp),
////                .align(alignment = Alignment.BottomEnd),
////                .align(alignment = Alignment.TopEnd),
//        text = { Text(text = "Dodaj tag") },
//        onClick = {
//            openDialog.value = true
//
//        },
//        backgroundColor = secondaryColor,
//        contentColor = backgroundColor,
//        icon = { Icon(Icons.Outlined.Add, "") })

    if (tagLocation.size > 0){
        Text(text = "Odabrali ste sledece tagove: ", fontWeight = FontWeight(500), fontSize = 17.sp)
//        Spacer(modifier = Modifier.padding(5.dp))
        TagsScreen(
            navController = navController,
            tagLocation = tagLocation,
            addPostViewModel::deleteTag
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Divider()
    }
    Spacer(modifier = Modifier.padding(10.dp))
    AddPostButton(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(bottom = 20.dp),
        vector = R.drawable.add_box_40_filled,
        text = "Dodaj objavu",
        onClick = {
            if (selectImages.isEmpty()) {
                toastImage.show()
            } else if (addPostViewModel._location.value == null) {
                toastLocation.show()
            } else {
                addPostViewModel.addPost()
            }
        },
        color = lightBackgroundColor
    )
//    Box(modifier = Modifier.fillMaxHeight(0.7f)) {
//        ExtendedFloatingActionButton(
//            modifier = Modifier
//                .padding(all = 10.dp)
//                .fillMaxWidth(0.8f)
//                .height(50.dp)
//                .align(Alignment.BottomCenter),
//            text = { Text(text = "Dodaj objavu") },
//
//            onClick = {
//                if (selectImages.isEmpty()) {
//                    toastImage.show()
//                } else if (descriptionValue.value.isBlank()) {
//                    toastDescription.show()
//                } else if (addPostViewModel._location.value == null) {
//                    toastLocation.show()
//                } else {
////                    var i = 0
////                    for (image in selectImages) {
////                        val bitmap = MediaStore.Images.Media.getBitmap(
////                            context.getContentResolver(),
////                            image
////                        )
////                        imagesBitmap.add(i, bitmap)
////
////                        i = i + 1
////                    }
//
//                    addPostViewModel.addPost()
//
//                }
//            },
//            backgroundColor = secondaryColor,
//            contentColor = backgroundColor,
//            icon = { Icon(Icons.Outlined.Add, "") })
//    }


}

@Composable
fun AddPostButton(
    modifier: Modifier,
    vector: Int,
    text: String,
    onClick: () -> Unit,
    color: Color
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(vertical = 0.dp, horizontal = 2.dp),
                imageVector = ImageVector.vectorResource(id = vector),
                contentDescription = "",
                tint = color
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                fontSize = 18.sp,
                softWrap = true,
                textAlign = TextAlign.Center
            )
        }
    }
}
//}
//    LazyRow(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//    ) {
//        items.forEach { it ->
//            item {
//                CustomChip(
//                    item = it,
//                    isSelected = it == currentItem,
//                    onItemChanged = {
//                        onItemChanged(it)
//                    }
//                )
//            }
//        }
//    }

@Composable
fun HorizontalScrollScreen(selectImages: MutableList<Uri>) {
    // replace with your items...
//    val items = selectImages
//    // a wrapper to fill the entire screen
//    Box() {
//        // BowWithConstraints will provide the maxWidth used below
//        BoxWithConstraints() {
//            // LazyRow to display your items horizontally
//            LazyRow(
////                modifier = Modifier.fillMaxWidth(),
//                state = rememberLazyListState()
//            ) {
//                itemsIndexed(items) { index, item ->
//                    Card(
//                        modifier = Modifier
//                            .height(200.dp)
//                            .width(200.dp) // here is the trick
//                            .padding(16.dp)
//                    ) {
//                        Image(
//                            painter = rememberImagePainter(item),
//                            contentScale = ContentScale.FillWidth,
//                            contentDescription = null,
//                            modifier = Modifier
//                                .padding(16.dp, 8.dp)
//                                .size(100.dp)
//                                .clickable {
//
//                                }) // card's content
//                        FloatingActionButton(
//                        modifier = Modifier
//                            .padding(all = 16.dp)
//                            .size(20.dp),
//                        backgroundColor = backgroundColor,
//                        onClick = {
//                            selectImages.removeAt(index)
//                        }) {
//                        Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close")
//                    }
//                    }
//                }
//            }
//        }
//    }
}

//@Composable
//fun createList(){
//    MaterialTheme() {
//        HorizontalScroller(){
//            Row(crossAxisSize = LayoutSize.Expand) {
//                (0..3).forEachIndexed { _, i ->
//                    populateListItem(i)
//                }
//            }
//        }
//    }
//}
//@Composable
//fun populateListItem(index: Int){
//    Column(crossAxisSize = LayoutSize.Wrap, modifier = Spacing(16.dp)) {
//        Card(elevation = 0.dp, shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp)) {
//            val im: Image = +imageList[index.rem(3)]
//            Container(expanded = true,height = 180.dp)
//            {
//                DrawImage(image = im)
//            }
//        }
//        HeightSpacer(height = 16.dp)
//        Text("Maldive $index",
//            style = +themeTextStyle { h6 })
//        Text("Enjoy Our $index Resort!",
//            style = +themeTextStyle { body2 })
//    }
//}