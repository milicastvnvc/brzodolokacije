package codeverse.brzodolokacije.ui.auth.successful_registration

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.ui.theme.secondaryColor
import codeverse.brzodolokacije.utils.Routes

@Composable
fun SuccessfulScreen(navController: NavController, indicatior: String?){

    var text: String = "";

    when(indicatior){
        "forget_password" -> text = "Uspešno ste resetovali šifru"
        "verification" -> text = "Uspešno ste verifikovali email"
    }

//    Box(modifier = Modifier.fillMaxSize().background(backgroundColor).padding(top = 20.dp), contentAlignment = Alignment.BottomEnd ){
////        Box(modifier = Modifier
////                .fillMaxSize()
////                //.padding(horizontal = 10.dp),
////            contentAlignment = Alignment.TopCenter
////        ){
////
////        }
//    }


    Box {
        Image(painter = painterResource(id = R.drawable.successful_background),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            modifier = Modifier.fillMaxSize())
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    //                .fillMaxHeight(0.7f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(primaryColor)
                    .padding(horizontal = 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.padding(10.dp))

                Text(
                    text = text,
                    color = lightBackgroundColor,
                    style = TextStyle(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(20.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            navController.navigate(Routes.LOGIN) {
                                launchSingleTop = true
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
                        Text(text = "Prijavi se", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    //Spacer(modifier = Modifier.padding(10.dp))
                    //            Text(text = "Prijavi se",
                    //                modifier = Modifier.clickable(onClick = {
                    //                    navController.navigate(Routes.LOGIN){
                    //                        launchSingleTop = true
                    //                    }
                    //                })
                    //            )
                    //            Spacer(modifier = Modifier.padding(10.dp))
                }
            }
        }
    }

}
