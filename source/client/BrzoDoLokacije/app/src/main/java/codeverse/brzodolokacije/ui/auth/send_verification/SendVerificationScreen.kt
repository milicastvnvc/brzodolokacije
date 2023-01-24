package codeverse.brzodolokacije.ui.auth.send_verification

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.components.Logo
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.secondaryColor
import codeverse.brzodolokacije.utils.Routes
import codeverse.brzodolokacije.utils.Validation

@Composable
fun SendVerificationScreen(navController: NavController, sendVerificationViewModel: SendVerificationViewModel = hiltViewModel()){

    val state = sendVerificationViewModel.state.value

    val emailValue = remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter){
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.TopCenter){


        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(0.80f)
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
            Logo("Unesite E-mail")
            Spacer(modifier = Modifier.padding(10.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "Pomoću koda koji vam stigne na e-mail, moći ćete da postavite novu šifru.",
                    modifier = Modifier.fillMaxWidth(0.8f), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.padding(10.dp))

                OutlinedTextField(
                    value = emailValue.value,
                    onValueChange = {emailValue.value = it},
                    label = { Text(text = "E-mail adresa") },
                    placeholder = { Text(text = "E-mail adresa") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                )

                Spacer(modifier = Modifier.padding(10.dp))

                val context = LocalContext.current
                Button(onClick = {
                    val validation = Validation.validateEmail(emailValue.value)
                    if (validation.first){
                        sendVerificationViewModel.sendVerification(emailValue.value, true)
                    }
                    else{
                        Toast.makeText(context,validation.second, Toast.LENGTH_LONG).show()
                    }

                },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = secondaryColor,
                        contentColor = backgroundColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text(text = "Pošalji", fontSize = 20.sp)
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
            navController.navigate(Routes.RESET_PASSWORD+"/"+emailValue.value){
                //popUpTo = navController.graph.startDestination
                launchSingleTop = true
            }

        }
        if(state.isLoading) {
            Loader(true)
        }
    }

}