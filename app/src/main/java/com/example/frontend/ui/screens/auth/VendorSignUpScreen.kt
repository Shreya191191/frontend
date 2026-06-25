package com.example.frontend.ui.screens.auth

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.ui.components.RentARideButton
import com.example.frontend.ui.components.RentARideTextField
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun VendorSignUpScreen(
    viewModel: AuthViewModel,
    onNavigateToVendorSignIn: () -> Unit,
    onNavigateToVendorDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Google Sign-In Setup
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val name = account?.displayName ?: ""
            val userEmail = account?.email ?: ""
            val photo = account?.photoUrl?.toString() ?: ""

            viewModel.signInWithGoogle(name, userEmail, photo, isVendor = true)
        } catch (e: Exception) {
            // Handle error
        }
    }

    // Google Sign-In Success Navigation
    LaunchedEffect(uiState.user) {
        uiState.user?.let { user ->
            if (user.isVendor) {
                onNavigateToVendorDashboard()
            }
        }
    }

    // Success Toast on Traditional SignUp
    LaunchedEffect(uiState.signupSuccessMessage) {
        uiState.signupSuccessMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearSuccessMessage()
            onNavigateToVendorSignIn()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Register as Vendor",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create vendor account to list vehicles",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Username Input
        RentARideTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = null
                viewModel.clearError()
            },
            label = "Username",
            placeholder = "Enter your username",
            leadingIcon = Icons.Default.Person,
            errorText = usernameError
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Input
        RentARideTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
                viewModel.clearError()
            },
            label = "Email",
            placeholder = "Enter your email",
            leadingIcon = Icons.Default.Email,
            errorText = emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        RentARideTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
                viewModel.clearError()
            },
            label = "Password",
            placeholder = "Enter your password",
            leadingIcon = Icons.Default.Lock,
            errorText = passwordError,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Error message display
        if (uiState.error != null) {
            Text(
                text = uiState.error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        // Register Button
        RentARideButton(
            text = "Register as Vendor",
            onClick = {
                var isValid = true
                if (username.length < 3) {
                    usernameError = "minimum 3 characters required"
                    isValid = false
                }
                if (email.isBlank()) {
                    emailError = "email required"
                    isValid = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "Invalid email address"
                    isValid = false
                }
                if (password.length < 4) {
                    passwordError = "minimum 4 characters required"
                    isValid = false
                }

                if (isValid) {
                    viewModel.vendorSignUp(username, email, password)
                }
            },
            isLoading = uiState.isLoading,
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sign In link
        Text(
            text = "Already have a vendor account? Sign In",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.clickable { onNavigateToVendorSignIn() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "OR",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Google Sign-In Button
        Button(
            onClick = {
                googleSignInClient.signOut().addOnCompleteListener {
                    val signInIntent = googleSignInClient.signInIntent
                    googleLauncher.launch(signInIntent)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Text(
                text = "Continue with Google",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
