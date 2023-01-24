package codeverse.brzodolokacije

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import codeverse.brzodolokacije.ui.theme.BrzoDoLokacijeTheme
import dagger.hilt.android.AndroidEntryPoint
import android.view.WindowManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import codeverse.brzodolokacije.composables.LoginScreen
import codeverse.brzodolokacije.composables.VerificationScreen
//import codeverse.brzodolokacije.ui.home.HomeScreen
import codeverse.brzodolokacije.ui.auth.register.RegistrationScreen
import codeverse.brzodolokacije.ui.auth.reset_password.ResetPasswordScreen
import codeverse.brzodolokacije.ui.auth.send_verification.SendVerificationScreen
import codeverse.brzodolokacije.ui.auth.successful_registration.SuccessfulScreen
import codeverse.brzodolokacije.utils.Routes


@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private lateinit var navController :NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            BrzoDoLokacijeTheme {

                BrzoDoLokacijeAplication()
            }
        }
    }

    @Composable
    fun BrzoDoLokacijeAplication(){

        navController = rememberNavController()

        NavHost(navController = navController, startDestination = Routes.LOGIN, builder = {
            composable(Routes.LOGIN, content = { LoginScreen(navController = navController)})
            composable(Routes.REGISTER, content = { RegistrationScreen(navController = navController) })
            composable(Routes.VERIFICATION + "/{email}")
            { backStackEntry ->
                VerificationScreen(navController, backStackEntry.arguments?.getString("email"))
            }
            //composable(Routes.HOME, content = { HomeScreen(navController = navController) })
            composable(Routes.SUCCESSFUL + "/{text}")
            { backStackEntry ->
                SuccessfulScreen(navController, backStackEntry.arguments?.getString("text"))
            }
            composable(Routes.RESET_PASSWORD + "/{email}")
            { backStackEntry ->
                ResetPasswordScreen(navController, backStackEntry.arguments?.getString("email"))
            }
            composable(Routes.SEND_VERIFICATION, content = { SendVerificationScreen(navController = navController) })
        })
    }


}