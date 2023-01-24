package codeverse.brzodolokacije.ui.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.profile.edit.EditProfileViewModel
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.dangerColor
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.utils.Constants
import codeverse.brzodolokacije.utils.Routes
import coil.compose.rememberImagePainter


@Composable
//@Preview(showBackground = true)
fun EditProfileScreen(navController: NavController, editProfileViewModel: EditProfileViewModel = hiltViewModel()) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }

    var user = editProfileViewModel.current_user

    val name = remember { mutableStateOf(editProfileViewModel.current_user.firstName) }
    val surName = remember { mutableStateOf(editProfileViewModel.current_user.lastName) }
    val username = remember { mutableStateOf(editProfileViewModel.current_user.username) }
    val currentImage: Painter = rememberImagePainter(Constants.BASE_URL + user!!.profilePicturePath)

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val editedImage = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    val context = LocalContext.current

    var editState = editProfileViewModel.editState.value

    if(editState.success){
        Toast.makeText(context,"Uspešna izmena profila",Toast.LENGTH_LONG).show()
        navController.navigate(Routes.PROFILE+"/0"){
            //popUpTo = navController.graph.startDestination
            launchSingleTop = true
        }
    }
    else if(editState.isLoading){
        //promeniti da bude preko cele stranice
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Loader(true)
        }
    }
    else{
        Column(modifier = Modifier.fillMaxSize()) {
            //Spacer(modifier = Modifier.height(8.dp))
            TopBarEdit(editProfileViewModel = editProfileViewModel, name = name,surName = surName, username = username, editedImage = editedImage, modifier = Modifier.padding(10.dp), navController = navController)
            Spacer(modifier = Modifier.height(15.dp))
            EditPicture(currentImage = currentImage,
                imageUri = imageUri,
                editedImage = editedImage,
                launcher = launcher,
                editProfileViewModel = editProfileViewModel)
            Spacer(modifier = Modifier.height(15.dp))
            EditFields(name,surName,username)
        }
    }

}

@Composable
fun TopBarEdit(
    editProfileViewModel: EditProfileViewModel,
    navController: NavController,
    name: MutableState<String>,
    surName: MutableState<String>,
    username: MutableState<String>,
    editedImage: MutableState<Bitmap?>,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Icon( //IconButton?
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = lightBackgroundColor,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navController.navigate(Routes.PROFILE+"/0")
                }
        )
        Text(
            text = "Izmeni podatke",
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = "Sačuvaj",
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.clickable {
                if (name.value.isBlank() || surName.value.isBlank() || username.value.isBlank()){

                    Toast.makeText(context,"Popunite prazna polja",Toast.LENGTH_LONG).show()
                }
                else{
                    //navController.navigate(Routes.PROFILE)
                    editProfileViewModel.editUser(name.value,surName.value,username.value, editedImage.value)
                }

            }
        )
    }
}

@Composable
fun EditPicture(
    modifier: Modifier = Modifier,
    currentImage: Painter,
    imageUri: Uri?,
    editedImage: MutableState<Bitmap?>,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    editProfileViewModel: EditProfileViewModel
){

    Column(modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                //.padding(horizontal = 20.dp)
        ) {
            RoundImageEdit(
                currentImage = currentImage,
                imageUri = imageUri,
                editedImage = editedImage,
                modifier = Modifier.fillMaxSize(0.17f)
                    //.size(100.dp)
                    //.weight(0.3f)

            )
        }
        Text(text="Izmenite sliku", color = primaryColor,
            modifier = Modifier.clickable(onClick = {
                //LOGIKA ZA IZMENU SLIKE
                launcher.launch("image/*")
                editProfileViewModel._imageAlert.value = ""

            }))

        if (editProfileViewModel._imageAlert.value.isNotBlank()) {
            Text(
                text = editProfileViewModel._imageAlert.value,
                color = dangerColor
            )
        }

    }
}

@Composable
fun RoundImageEdit(
    currentImage: Painter,
    imageUri : Uri?,
    editedImage: MutableState<Bitmap?>,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    var imageToShow: Bitmap? = null;

    imageUri?.let {
        if (Build.VERSION.SDK_INT < 28) {
            editedImage.value = MediaStore.Images
                .Media.getBitmap(context.contentResolver, it)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, it)
            editedImage.value = ImageDecoder.decodeBitmap(source)
        }

        editedImage.value?.let { btm ->
            imageToShow = btm
        }
    }
    if(imageToShow != null){
        Image(
            bitmap = imageToShow!!.asImageBitmap(),
            contentDescription = null,
            modifier = modifier
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .border(width = 1.dp, color = backgroundColor, shape = CircleShape)
                .padding(3.dp)
                .clip(CircleShape)
        )
    }
    else{
        Image(
            painter = currentImage,
            contentDescription = null,
            modifier = modifier
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .border(width = 1.dp, color = backgroundColor, shape = CircleShape)
                .padding(3.dp)
                .clip(CircleShape)
        )
    }

}

@Composable
fun EditFields(name: MutableState<String>, surName:MutableState<String>,username: MutableState<String>){
    val nameValue = name
    val surnameValue = surName
    val usernameValue = username

//    val passwordValue = remember { mutableStateOf("") }
//    val confirmPasswordValue = remember { mutableStateOf("") }

    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }
    val focusRequester: FocusRequester = remember { FocusRequester() }
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
//        Box(modifier = Modifier
//            .fillMaxSize(),
//            //.background(backgroundColor),
//            contentAlignment = Alignment.TopCenter){
//            //Image(image)
//        }
//    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(0.70f)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            //.background(backgroundColor)
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            OutlinedTextField(
                value = nameValue.value,
                onValueChange = {nameValue.value = it},
                label = { Text(text = "Ime") },
                placeholder = { Text(text = "Ime")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor
                )
            )
            Spacer(modifier = Modifier.padding(5.dp))
            OutlinedTextField(
                value = surnameValue.value,
                onValueChange = {surnameValue.value = it},
                label = { Text(text = "Prezime") },
                placeholder = { Text(text = "Prezime")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor
                )
            )

            OutlinedTextField(
                value = usernameValue.value,
                onValueChange = {usernameValue.value = it},
                label = { Text(text = "Korisničko ime") },
                placeholder = { Text(text = "Korisničko ime")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f),
            )
            Spacer(modifier = Modifier.padding(5.dp))
//            OutlinedTextField(
//                value = emailValue.value,
//                onValueChange = {emailValue.value = it},
//                label = { Text(text = "E-mail adresa") },
//                placeholder = { Text(text = "E-mail adresa")},
//                singleLine = true,
//                modifier = Modifier.fillMaxWidth(0.8f),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedBorderColor = primaryColor,
//                    focusedLabelColor = primaryColor,
//                    cursorColor = primaryColor
//                )
//            )
//            Spacer(modifier = Modifier.padding(5.dp))
//            OutlinedTextField(
//                value = passwordValue.value,
//                onValueChange = {passwordValue.value = it},
//                label = { Text(text = "Šifra") },
//                placeholder = { Text(text = "Šifra")},
//                singleLine = true,
//                trailingIcon = {
//                    IconButton(onClick = {
//                        passwordVisibility.value = !passwordVisibility.value
//                    }) {
//                        Icon(
//                            painterResource(
//                                id = R.drawable.password_eye),
//                            contentDescription = null,
//                            tint = if(passwordVisibility.value) primaryColor
//                            else Color.Gray)
//                    }
//                },
//                visualTransformation = if (passwordVisibility.value) VisualTransformation.None
//                else PasswordVisualTransformation(),
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .focusRequester(focusRequester = focusRequester),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedBorderColor = primaryColor,
//                    focusedLabelColor = primaryColor,
//                    cursorColor = primaryColor
//                )
//
//            )
//            Spacer(modifier = Modifier.padding(5.dp))
//            OutlinedTextField(
//                value = confirmPasswordValue.value,
//                onValueChange = {confirmPasswordValue.value = it},
//                label = { Text(text = "Potvrdi šifru") },
//                placeholder = { Text(text = "Potvrdi šifru")},
//                singleLine = true,
//                trailingIcon = {
//                    IconButton(onClick = {
//                        confirmPasswordVisibility.value = !confirmPasswordVisibility.value
//                    }) {
//                        Icon(painterResource(
//                            id = R.drawable.password_eye),
//                            contentDescription = null,
//                            tint = if(confirmPasswordVisibility.value) primaryColor
//                            else Color.Gray)
//                    }
//                },
//                visualTransformation = if (confirmPasswordVisibility.value) VisualTransformation.None
//                else PasswordVisualTransformation(),
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .focusRequester(focusRequester = focusRequester),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedBorderColor = primaryColor,
//                    focusedLabelColor = primaryColor,
//                    cursorColor = primaryColor
//                )
//            )

            Spacer(modifier = Modifier.padding(10.dp))

//            val context = LocalContext.current
//            Button(onClick = {
//                //val validation : Pair<Boolean,String> = viewModel.validateCredentials(usernameValue.value,passwordValue.value,
//                //    nameValue.value,surnameValue.value,emailValue.value,confirmPasswordValue.value)
//                //if (validation.first) { //prosla validacija
//
//                //    viewModel.register(nameValue.value,surnameValue.value,
//                //        usernameValue.value,emailValue.value,passwordValue.value)
//                //}
//                //else{ //nije prosla validacija
//                    //Toast.makeText(context,validation.second, Toast.LENGTH_LONG).show()
//                //}
//            },
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = secondaryColor,
//                    contentColor = backgroundColor
//                ),
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .height(50.dp)
//            ) {
//                Text(text = "Registruj se", fontSize = 20.sp)
//            }
//
//            Spacer(modifier = Modifier.padding(10.dp))
        }
    }
}