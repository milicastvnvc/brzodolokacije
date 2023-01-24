package codeverse.brzodolokacije.composables

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
import codeverse.brzodolokacije.ui.auth.verification.VerificationViewModel
import codeverse.brzodolokacije.ui.components.Loader
import codeverse.brzodolokacije.ui.components.Logo
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.secondaryColor
import codeverse.brzodolokacije.utils.Routes

@Composable
fun VerificationScreen(navController: NavController, email : String?, verificationViewModel: VerificationViewModel = hiltViewModel()) {
    val codeValue = remember { mutableStateOf("") }

    var state = verificationViewModel.state.value

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
            Logo("Unesite kod:")
            Spacer(modifier = Modifier.padding(15.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                OutlinedTextField(
                    value = codeValue.value,
                    onValueChange = {codeValue.value = it},
                    label = { Text(text = "Kod") },
                    placeholder = { Text(text = "Kod")},
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                )


                Spacer(modifier = Modifier.padding(10.dp))

                var context = LocalContext.current
                Button(onClick = {
                    if(codeValue.value.isNotBlank()){
                        verificationViewModel.verifyUser(email!!, codeValue.value)
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
                    Text(text = "Pošalji kod", fontSize = 20.sp)
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
            navController.navigate(Routes.SUCCESSFUL+"/verification"){
                //popUpTo = navController.graph.startDestination
                launchSingleTop = true
            }

        }
        if(state.isLoading) {
            Loader(true)
        }

    }
}