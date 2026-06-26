package com.example.frontend.ui.navigation

sealed class Screen(val route: String) {
    object SignIn : Screen("signin")
    object SignUp : Screen("signup")
    object VendorSignIn : Screen("vendor_signin")
    object VendorSignUp : Screen("vendor_signup")
    object Home : Screen("home")
    object Search : Screen("search")
    object Orders : Screen("orders")
    object Wishlist : Screen("wishlist")
    object Profile : Screen("profile")
    object SearchResults : Screen("search_results")
    object VehicleVariants : Screen("vehicle_variants")
    object VehicleDetails : Screen("vehicle_details/{vehicleId}") {
        fun createRoute(vehicleId: String) = "vehicle_details/$vehicleId"
    }
    object VendorDashboard : Screen("vendor_dashboard")
    object AdminDashboard : Screen("admin_dashboard")
}
