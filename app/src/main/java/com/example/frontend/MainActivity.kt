package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.ui.navigation.AppNavigation
import com.example.frontend.ui.screens.auth.AuthViewModel
import com.example.frontend.ui.screens.search.SearchFlowViewModel
import com.example.frontend.ui.theme.FrontEndTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontEndTheme {
                val authViewModel: AuthViewModel = hiltViewModel()
                val searchViewModel: SearchFlowViewModel = hiltViewModel()

                AppNavigation(
                    authViewModel = authViewModel,
                    searchViewModel = searchViewModel
                )
            }
        }
    }
}