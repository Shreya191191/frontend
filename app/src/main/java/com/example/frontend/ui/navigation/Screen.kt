package com.example.frontend.ui.navigation

sealed class Screen(val route: String) {
    object SignIn : Screen("signin")
    object SignUp : Screen("signup")
    object VendorSignIn : Screen("vendor_signin")
    object VendorSignUp : Screen("vendor_signup")
    object Home : Screen("home")
    object VendorDashboard : Screen("vendor_dashboard")
    object AdminDashboard : Screen("admin_dashboard")
}
