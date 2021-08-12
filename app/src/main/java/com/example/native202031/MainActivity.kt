package com.example.native202031

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.native202031.ui.theme.Native202031Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    enum class StateKey {
        USER_NAME
    }

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.simpleName) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.info("onCreate savedInstanceState=$savedInstanceState")
        setContent {
            Native202031Theme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = DestScreen.Route.HOME.rawValue) {

        fun navigate(destScreen: DestScreen) {
            when (destScreen.route) {
                DestScreen.Route.BACK -> navController.popBackStack()
                else -> navController.navigate(destScreen.toRouteWithArgs())
            }
        }

        fun receiveDestScreen(
            viewModel: BaseViewModel,
            onReceive: (destScreen: DestScreen) -> Unit
        ) {
            viewModel.viewModelScope.launch {
                viewModel.destScreen.receiveAsFlow().collect {
                    onReceive(it)
                }
            }
        }

        composable(DestScreen.Route.HOME.rawValue) { navBackStackEntry ->
            val viewModel: HomeViewModel = hiltViewModel()
            viewModel.viewModelScope.launch {
                viewModel.destScreen.receiveAsFlow().collect { destScreen ->
                    when (destScreen.route) {
                        DestScreen.Route.BACK -> navController.popBackStack()
                        DestScreen.Route.COMMIT -> {
                            val repo = viewModel.repo
//                                navBackStackEntry.savedStateHandle.get<String>(DestScreen.COMMIT.argKey)
                            navController.navigate("${destScreen.route}/$repo")
                        }
                        else -> navController.navigate(destScreen.route.rawValue)
                    }
                }
            }
            navBackStackEntry.savedStateHandle.get<String>(MainActivity.StateKey.USER_NAME.name)
                ?.let {
                    viewModel.setUser(it)
                }
            HomeScreen()
        }
        composable(DestScreen.Route.SIGN_IN.rawValue) {
            val viewModel: SignInViewModel = hiltViewModel()
            receiveDestScreen(viewModel) { navigate(it) }
            SignInScreen()
        }
        composable(DestScreen.Route.CHECK_USER.rawValue) {
            val viewModel: CheckUserViewModel = hiltViewModel()
            receiveDestScreen(viewModel) {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    MainActivity.StateKey.USER_NAME.name,
                    viewModel.userName.value
                )
                navigate(it)
            }
            CheckUserScreen()
        }
        composable(DestScreen.Route.COMMIT.rawValue) { navBackStackEntry ->
            val viewModel: CommitViewModel = hiltViewModel()
            receiveDestScreen(viewModel) { navigate(it) }
            navBackStackEntry.arguments?.getString("repo")?.let {
                navBackStackEntry.savedStateHandle.set("repo", it)
            }
            CommitScreen()
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