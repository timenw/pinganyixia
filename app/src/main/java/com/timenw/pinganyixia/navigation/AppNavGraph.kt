package com.timenw.pinganyixia.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.timenw.pinganyixia.data.prefs.AppSettings
import com.timenw.pinganyixia.ui.screen.ContactsScreen
import com.timenw.pinganyixia.ui.screen.HistoryScreen
import com.timenw.pinganyixia.ui.screen.HomeScreen
import com.timenw.pinganyixia.ui.screen.MedicineScreen
import com.timenw.pinganyixia.ui.screen.SettingsScreen
import com.timenw.pinganyixia.viewmodel.MainViewModel

@Composable
fun AppNavGraph(
    viewModel: MainViewModel,
    settings: AppSettings
) {
    val navController = rememberNavController()
    val startDestination = Routes.Home.route

    NavHost(navController, startDestination) {
        composable(Routes.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToContacts = { navController.navigate(Routes.Contacts.route) },
                onNavigateToMedicine = { navController.navigate(Routes.Medicine.route) },
                onNavigateToHistory = { navController.navigate(Routes.History.route) },
                onNavigateToSettings = { navController.navigate(Routes.Settings.route) }
            )
        }
        composable(Routes.Medicine.route) {
            MedicineScreen(viewModel = viewModel)
        }
        composable(Routes.History.route) {
            HistoryScreen(viewModel = viewModel)
        }
        composable(Routes.Settings.route) {
            SettingsScreen(viewModel = viewModel, settings = settings)
        }
        composable(Routes.Contacts.route) {
            ContactsScreen(viewModel = viewModel)
        }
    }
}