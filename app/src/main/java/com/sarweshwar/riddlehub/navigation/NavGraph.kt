package com.sarweshwar.riddlehub.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sarweshwar.riddlehub.ui.auth.AuthViewModel
import com.sarweshwar.riddlehub.ui.auth.LoginScreen
import com.sarweshwar.riddlehub.ui.auth.RegisterScreen
import com.sarweshwar.riddlehub.ui.home.HomeScreen
import com.sarweshwar.riddlehub.ui.create.CreatePuzzleScreen
import com.sarweshwar.riddlehub.ui.detail.PuzzleDetailScreen
import com.sarweshwar.riddlehub.ui.profile.ProfileScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Create : Screen("create")
    object Profile : Screen("profile")
    object Detail : Screen("detail/{puzzleId}") {
        fun createRoute(puzzleId: String) = "detail/$puzzleId"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val startDestination = if (authViewModel.loggedIn.value) Screen.Home.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(animationSpec = tween(400)) + slideInHorizontally(initialOffsetX = { 1000 }) },
        exitTransition = { fadeOut(animationSpec = tween(400)) + slideOutHorizontally(targetOffsetX = { -1000 }) },
        popEnterTransition = { fadeIn(animationSpec = tween(400)) + slideInHorizontally(initialOffsetX = { -1000 }) },
        popExitTransition = { fadeOut(animationSpec = tween(400)) + slideOutHorizontally(targetOffsetX = { 1000 }) }
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(onPuzzleClick = { puzzleId ->
                navController.navigate(Screen.Detail.createRoute(puzzleId))
            })
        }
        composable(Screen.Create.route) {
            CreatePuzzleScreen(onPuzzleCreated = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Create.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onPuzzleClick = { puzzleId ->
                    navController.navigate(Screen.Detail.createRoute(puzzleId))
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("puzzleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val puzzleId = backStackEntry.arguments?.getString("puzzleId") ?: ""
            PuzzleDetailScreen(puzzleId = puzzleId, onBackClick = {
                navController.popBackStack()
            })
        }
    }
}
