package com.example.native202031

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.native202031.ui.theme.Native202031Theme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainActivity : ComponentActivity() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Native202031Theme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(mainViewModel) { navHostController ->
                        lifecycleScope.launchWhenStarted {
                            mainViewModel.destScreen.receiveAsFlow().collect { destScreen ->
                                logger.info("destScreen=$destScreen")
                                when (destScreen) {
                                    DestScreen.BACK -> navHostController.popBackStack()
                                    else -> navHostController.navigate(destScreen.route)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    receiveDestScreen: (navController: NavHostController) -> Unit
) {
    val navController = rememberNavController()
    receiveDestScreen(navController)
    NavHost(navController = navController, startDestination = "home") {
        composable(DestScreen.HOME.route) {
            HomeScreen(mainViewModel)
        }
        composable(DestScreen.SIGN_IN.route) {
            SignInScreen(mainViewModel)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Native202031Theme {
        Greeting("Android")
    }
}