package codeverse.brzodolokacije.ui.auth.reset_password

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun ResetPasswordScreen(navController : NavController, email : String?, resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel()){

    val state = resetPasswordViewModel.state.value

    val codeValue = remember { mutableStateOf("") }
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
            Logo("Resetuj šifru")


            Spacer(modifier = Modifier.padding(15.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {


                OutlinedTextField(
                    value = codeValue.value,
                    onValueChange = {codeValue.value = it},
                    label = { Text(text = "Kod") },
                    placeholder = { Text(text = "Kod")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next),
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
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next),
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .focusRequester(focusRequester = focusRequester)
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
                )

                Spacer(modifier = Modifier.padding(10.dp))

                val context = LocalContext.current
                Button(onClick = {
                    if(codeValue.value.isNotBlank()){
                        val validation = resetPasswordViewModel.validatePasswords(passwordValue.value,confirmPasswordValue.value)
                        if (validation.first){
                            resetPasswordViewModel.sendVerification(email!!,passwordValue.value,confirmPasswordValue.value,codeValue.value)
                        }
                        else{
                            Toast.makeText(context,validation.second, Toast.LENGTH_LONG).show()
                        }
                    }
                    else{
                        Toast.makeText(context,"Unesite kod", Toast.LENGTH_LONG).show()
                    }

                },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = secondaryColor,
                        contentColor = backgroundColor),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text(text = "Resetuj", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Text(text = "Odustani",
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(Routes.LOGIN){
                            //popUpTo = navController.graph.startDestination
                            launchSingleTop = true
                        }
                    })
                )
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
            //salje ga na reset password
            navController.navigate(Routes.SUCCESSFUL+"/forget_password"){
                //popUpTo = navController.graph.startDestination
                launchSingleTop = true
            }

        }
        if(state.isLoading) {
            Loader(true)
        }
    }

}