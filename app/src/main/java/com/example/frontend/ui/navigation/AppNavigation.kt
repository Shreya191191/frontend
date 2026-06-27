package com.example.frontend.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.ui.components.MainLayoutScaffold
import com.example.frontend.ui.screens.auth.*
import com.example.frontend.ui.screens.search.*
import com.example.frontend.ui.screens.home.HomeScreen
import com.example.frontend.ui.screens.orders.*
import com.example.frontend.ui.screens.profile.ProfileScreen
import com.example.frontend.ui.screens.settings.SettingsScreen
import com.example.frontend.ui.screens.settings.AboutScreen
import com.example.frontend.ui.screens.booking.CheckoutScreen
import com.example.frontend.ui.screens.booking.BookingSuccessScreen
import com.example.frontend.ui.screens.vendor.VendorDashboardScreen
import com.example.frontend.ui.screens.admin.AdminDashboardScreen
import com.example.frontend.ui.screens.settings.*


@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    searchViewModel: SearchFlowViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val currentSession by authViewModel.currentSession.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Identify if current route is part of customer dashboard flows
    val isCustomerRoute = currentRoute in listOf(
        Screen.Home.route,
        Screen.Search.route,
        Screen.Orders.route,
        Screen.Profile.route,
        Screen.SearchResults.route,
        Screen.VehicleVariants.route,
        Screen.VehicleDetails.route
    )


    val startDestination = if (currentSession != null) {
        when {
            currentSession!!.isAdmin -> Screen.AdminDashboard.route
            currentSession!!.isVendor -> Screen.VendorDashboard.route
            else -> Screen.Home.route
        }
    } else {
        Screen.SignIn.route
    }

    if (isCustomerRoute) {
        MainLayoutScaffold(
            navController = navController,
            userName = currentSession?.username ?: "Shreyas",
            userEmail = currentSession?.email ?: "shreyas@rentaride.com",
            onSignOut = {
                authViewModel.signOut()
                navController.navigate(Screen.SignIn.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        ) { innerPadding ->
            CustomerNavHost(
                navController = navController,
                startDestination = startDestination,
                authViewModel = authViewModel,
                searchViewModel = searchViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    } else {
        CustomerNavHost(
            navController = navController,
            startDestination = startDestination,
            authViewModel = authViewModel,
            searchViewModel = searchViewModel,
            modifier = modifier
        )
    }
}

@Composable
fun CustomerNavHost(
    navController: NavHostController,
    startDestination: String,
    authViewModel: AuthViewModel,
    searchViewModel: SearchFlowViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(
                viewModel = authViewModel,
                onNavigateToUser = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateToVendor = {
                    navController.navigate(Screen.VendorDashboard.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateToAdmin = {
                    navController.navigate(Screen.AdminDashboard.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onNavigateToVendorSignIn = {
                    navController.navigate(Screen.VendorSignIn.route)
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                viewModel = authViewModel,
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
                onNavigateToUser = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.VendorSignIn.route) {
            VendorSignInScreen(
                viewModel = authViewModel,
                onNavigateToVendorDashboard = {
                    navController.navigate(Screen.VendorDashboard.route) {
                        popUpTo(Screen.VendorSignIn.route) { inclusive = true }
                    }
                },
                onNavigateToVendorSignUp = {
                    navController.navigate(Screen.VendorSignUp.route)
                },
                onNavigateToUserSignIn = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.VendorSignIn.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.VendorSignUp.route) {
            VendorSignUpScreen(
                viewModel = authViewModel,
                onNavigateToVendorSignIn = {
                    navController.popBackStack()
                },
                onNavigateToVendorDashboard = {
                    navController.navigate(Screen.VendorDashboard.route) {
                        popUpTo(Screen.VendorSignUp.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = searchViewModel
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                navController = navController
            )
        }

        composable(Screen.Orders.route) {
            val ordersViewModel: OrdersViewModel = hiltViewModel()
            OrdersScreen(
                navController = navController,
                viewModel = ordersViewModel
            )
        }

        composable(Screen.Profile.route) {

            ProfileScreen(
                navController = navController,
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }

        composable(Screen.About.route) {
            AboutScreen(navController = navController)
        }


        composable(Screen.SearchResults.route) {
            SearchResultsScreen(
                navController = navController,
                viewModel = searchViewModel
            )
        }

        composable(Screen.VehicleVariants.route) {
            VehicleVariantsScreen(
                navController = navController,
                viewModel = searchViewModel
            )
        }

        composable(
            route = Screen.VehicleDetails.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: ""
            VehicleDetailsScreen(
                navController = navController,
                vehicleId = vehicleId,
                searchFlowViewModel = searchViewModel
            )
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(
                navController = navController,
                searchFlowViewModel = searchViewModel
            )
        }

        composable(Screen.BookingSuccess.route) {
            BookingSuccessScreen(
                navController = navController
            )
        }

        composable(Screen.VendorDashboard.route) {
            VendorDashboardScreen(
                navController = navController,
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(Screen.VendorSignIn.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                navController = navController,
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, onSignOut: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onSignOut) {
                Text("Sign Out")
            }
        }
    }
}
