package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.frontend.ui.navigation.Screen
import com.example.frontend.ui.screens.auth.*
import com.example.frontend.ui.theme.FrontEndTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontEndTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()
                val currentSession by authViewModel.currentSession.collectAsState()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Identify if current route is part of customer dashboard flows
                val isCustomerRoute = currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Search.route,
                    Screen.Orders.route,
                    Screen.Wishlist.route,
                    Screen.Profile.route,
                    Screen.SearchResults.route
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
                    com.example.frontend.ui.components.MainLayoutScaffold(
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
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                } else {
                    CustomerNavHost(
                        navController = navController,
                        startDestination = startDestination,
                        authViewModel = authViewModel,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun CustomerNavHost(
    navController: androidx.navigation.NavHostController,
    startDestination: String,
    authViewModel: com.example.frontend.ui.screens.auth.AuthViewModel,
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
            com.example.frontend.ui.screens.home.HomeScreen(
                navController = navController
            )
        }

        composable(Screen.Search.route) {
            com.example.frontend.ui.screens.search.SearchScreen(
                navController = navController
            )
        }

        composable(Screen.Orders.route) {
            com.example.frontend.ui.screens.orders.OrdersScreen(
                navController = navController
            )
        }

        composable(Screen.Wishlist.route) {
            com.example.frontend.ui.screens.wishlist.WishlistScreen(
                navController = navController
            )
        }

        composable(Screen.Profile.route) {
            com.example.frontend.ui.screens.profile.ProfileScreen(
                navController = navController,
                userName = authViewModel.currentSession.collectAsState().value?.username ?: "Shreyas",
                userEmail = authViewModel.currentSession.collectAsState().value?.email ?: "shreyas@rentaride.com",
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SearchResults.route) {
            com.example.frontend.ui.screens.search.SearchResultsScreen(
                navController = navController
            )
        }

        composable(Screen.VendorDashboard.route) {
            PlaceholderScreen(title = "Vendor Dashboard Screen", onSignOut = {
                authViewModel.signOut()
                navController.navigate(Screen.VendorSignIn.route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }

        composable(Screen.AdminDashboard.route) {
            PlaceholderScreen(title = "Admin Dashboard Screen", onSignOut = {
                authViewModel.signOut()
                navController.navigate(Screen.SignIn.route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
    }
}

@androidx.compose.runtime.Composable
fun PlaceholderScreen(title: String, onSignOut: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
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