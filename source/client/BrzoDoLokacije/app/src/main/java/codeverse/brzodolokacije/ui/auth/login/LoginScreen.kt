package codeverse.brzodolokacije.composables
import android.content.Intent
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
import codeverse.brzodolokacije.MainActivity
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.ui.auth.login.LoginViewModel
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.components.Logo
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.ui.theme.secondaryColor
import codeverse.brzodolokacije.utils.Routes

@Composable
//@Preview
fun LoginScreen(navController: NavController, viewModel : LoginViewModel = hiltViewModel()){

    val state = viewModel.state.value
    val context = LocalContext.current

    val usernameValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }

    val passwordVisibility = remember { mutableStateOf(false) }
    val focusRequester: FocusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor), contentAlignment = Alignment.TopCenter){
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(0.60f)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(color = backgroundColor)
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
    ) {

        if (!state.success && !state.isLoading){

            Logo("Prijavi se")

            Spacer(modifier = Modifier.padding(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

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
                    //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    //onImeActionPerformed = { _, _ -> focusRequestor.requestFocus()}
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
                            Icon(painterResource(
                                id = R.drawable.password_eye),
                                contentDescription = null,
                                tint = if(passwordVisibility.value) primaryColor
                                else Color.Gray)
                        }
                    },
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .focusRequester(focusRequester = focusRequester),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    ),
                    //onIneActionPerformed = {_, controller -> controller?, hideSoftvwareKeyboard()}
                )

                Spacer(modifier = Modifier.padding(5.dp))

                Column(
                    modifier = Modifier.fillMaxSize(0.8f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(text = "Zaboravili ste šifru?",
                        color = primaryColor,
                        modifier = Modifier.clickable(onClick = {
                            navController.navigate(Routes.SEND_VERIFICATION){
                                //popUpTo = navController.graph.startDestination
                                launchSingleTop = true
                            }
                        })
                    )
                }


                Spacer(modifier = Modifier.padding(5.dp))

                val context = LocalContext.current
                Button(
                    onClick = {

                        val validation : Pair<Boolean,String> = viewModel.validateCredentials(usernameValue.value,passwordValue.value)

                        if (validation.first) { //prosla validacija
                            viewModel.login(usernameValue.value,passwordValue.value)
                        }
                        else{ //nije prosla validacija
                            Toast.makeText(context,validation.second,Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = secondaryColor,
                        contentColor = backgroundColor),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text(text = "Prijavi se", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.padding(20.dp))
                Row(){
                    Text(text = "Nemate nalog? ")
                    Text(text = "Registrujete se",
                        color = primaryColor,
                        modifier = Modifier.clickable(onClick = {
                            navController.navigate(Routes.REGISTER){
                                //popUpTo = navController.graph.startDestination
                                launchSingleTop = true
                            }
                        })
                    )

                }
                Spacer(modifier = Modifier.padding(20.dp))
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
            //println("Da li je verifikovan? " + state.data!!.user!!.isVerified)
            if (state.data!!.user!!.isVerified){
//                navController.navigate(Routes.HOME){
//                    //popUpTo = navController.graph.startDestination
//                    launchSingleTop = true
//                }
                context.startActivity(Intent(context, MainActivity::class.java))
            }
            else{
                navController.navigate(Routes.VERIFICATION+"/"+state.data.user.email){
                    //popUpTo = navController.graph.startDestination
                    launchSingleTop = true
                }
            }

        }
        if(state.isLoading) {
            Loader(true)
        }
    }

}

