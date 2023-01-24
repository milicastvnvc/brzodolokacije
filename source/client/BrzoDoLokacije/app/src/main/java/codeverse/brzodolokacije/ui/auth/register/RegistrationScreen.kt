package codeverse.brzodolokacije.ui.auth.register

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.components.Logo
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.ui.theme.secondaryColor
import codeverse.brzodolokacije.utils.Routes


@Composable
fun RegistrationScreen(navController: NavController, viewModel : RegisterViewModel = hiltViewModel()){

    val state = viewModel.state.value

    val nameValue = remember { mutableStateOf("") }
    val surnameValue = remember { mutableStateOf("") }
    val usernameValue = remember { mutableStateOf("") }
    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val confirmPasswordValue = remember { mutableStateOf("") }

    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }
    val focusRequester: FocusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor), contentAlignment = Alignment.TopCenter){
            //Image(image)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(0.70f)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(backgroundColor)
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
    ) {

        if (!state.success && !state.isLoading){
            Spacer(modifier = Modifier.padding(10.dp))
            Logo("Registruj se")


            Spacer(modifier = Modifier.padding(15.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                OutlinedTextField(
                    value = nameValue.value,
                    onValueChange = {nameValue.value = it},
                    label = { Text(text = "Ime") },
                    placeholder = { Text(text = "Ime")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    ),
                )

                OutlinedTextField(
                    value = surnameValue.value,
                    onValueChange = {surnameValue.value = it},
                    label = { Text(text = "Prezime") },
                    placeholder = { Text(text = "Prezime")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    ),
                )

                OutlinedTextField(
                    value = usernameValue.value,
                    onValueChange = {usernameValue.value = it},
                    label = { Text(text = "Korisničko ime") },
                    placeholder = { Text(text = "Korisničko ime")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    ),
                )

                OutlinedTextField(
                    value = emailValue.value,
                    onValueChange = {emailValue.value = it},
                    label = { Text(text = "E-mail adresa") },
                    placeholder = { Text(text = "E-mail adresa")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    ),
                )

                OutlinedTextField(
                    value = passwordValue.value,
                    onValueChange = {passwordValue.value = it},
                    label = { Text(text = "Šifra") },
                    placeholder = { Text(text = "Šifra")},
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility.value = !passwordVisibility.value
                        }) {
                            Icon(
                                painterResource(
                                    id = R.drawable.password_eye),
                                contentDescription = null,
                                tint = if(passwordVisibility.value) primaryColor
                                else Color.Gray)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .focusRequester(focusRequester = focusRequester),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    ),
                )

                OutlinedTextField(
                    value = confirmPasswordValue.value,
                    onValueChange = {confirmPasswordValue.value = it},
                    label = { Text(text = "Potvrdi šifru") },
                    placeholder = { Text(text = "Potvrdi šifru")},
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            confirmPasswordVisibility.value = !confirmPasswordVisibility.value
                        }) {
                            Icon(painterResource(
                                id = R.drawable.password_eye),
                                contentDescription = null,
                                tint = if(confirmPasswordVisibility.value) primaryColor
                                else Color.Gray)
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    visualTransformation = if (confirmPasswordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .focusRequester(focusRequester = focusRequester),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    ),
                )

                Spacer(modifier = Modifier.padding(10.dp))

                val context = LocalContext.current
                Button(onClick = {
                    val validation : Pair<Boolean,String> = viewModel.validateCredentials(usernameValue.value,passwordValue.value,
                        nameValue.value,surnameValue.value,emailValue.value,confirmPasswordValue.value)
                    if (validation.first) { //prosla validacija
                        viewModel.register(nameValue.value,surnameValue.value,
                            usernameValue.value,emailValue.value,passwordValue.value)
                    }
                    else{ //nije prosla validacija
                        Toast.makeText(context,validation.second, Toast.LENGTH_LONG).show()
                    }
                },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = secondaryColor,
                        contentColor = backgroundColor),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text(text = "Registruj se", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Row(){
                    Text(text = "Već imate nalog? ")
                    Text(text = "Prijavite se",
                        color = primaryColor,
                        modifier = Modifier.clickable(onClick = {
                            navController.navigate(Routes.LOGIN){
                                //popUpTo = navController.graph.startDestination
                                launchSingleTop = true
                            }
                        })
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                }

            }
        if(state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        if (state.success){
            navController.navigate(Routes.VERIFICATION+"/"+state.data!!.email){
                //popUpTo = navController.graph.startDestination
                launchSingleTop = true
            }

        }
        if(state.isLoading) {
            Loader(true)
        }

        }
}

