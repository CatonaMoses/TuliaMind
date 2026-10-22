package com.example.tuliamind

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tuliamind.screens.*

/** The map of the whole app: each route name opens one screen. */
@Composable
fun AppNav() {
    val nav = rememberNavController()
    val back: () -> Unit = { nav.popBackStack() }

    NavHost(navController = nav, startDestination = "splash") {
        composable("splash") {
            SplashScreen { nav.navigate("pin") { popUpTo("splash") { inclusive = true } } }
        }
        composable("pin") {
            PinScreen { nav.navigate("home") { popUpTo("pin") { inclusive = true } } }
        }
        composable("home") { HomeScreen { route -> nav.navigate(route) } }
        composable("mood") { MoodScreen(back) }
        composable("journal") { JournalScreen(back) }
        composable("breathing") { BreathingScreen(back) }
        composable("quiz") { QuizScreen(back) { nav.navigate("help") } }
        composable("panic") { PanicScreen(back) }
        composable("streak") { StreakScreen(back) }
        composable("help") { HelpScreen(back) }
        composable("booking") { BookingScreen(back) }
        composable("map") { MapScreen(back) }
        composable("web") { WebScreen(back) }
        composable("lab") { LabScreen(back) }
    }
}
