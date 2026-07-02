package com.timenw.pinganyixia.navigation

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Medicine : Routes("medicine")
    object History : Routes("history")
    object Settings : Routes("settings")
    object Contacts : Routes("contacts")
}