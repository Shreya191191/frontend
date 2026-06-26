package com.example.frontend.ui.screens.profile

import com.example.frontend.ui.components.bounceClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.frontend.ui.theme.EmeraldPrimary
import com.example.frontend.ui.theme.SlateGrey

@Composable
fun ProfileScreen(
    navController: NavController,
    userName: String = "Shreyas",
    userEmail: String = "shreyas@rentaride.com",
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Header
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userName.take(1).uppercase(),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userName,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = userEmail,
            style = MaterialTheme.typography.bodyMedium,
            color = SlateGrey
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Profile Info Cards
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, SlateGrey.copy(alpha = 0.12f))
        ) {
            Column {
                ProfileItemRow(icon = Icons.Default.Person, title = "Personal Info", subtitle = "Manage name, mobile & details")
                Divider(color = SlateGrey.copy(alpha = 0.08f), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileItemRow(icon = Icons.Default.Email, title = "Email Address", subtitle = userEmail)
                Divider(color = SlateGrey.copy(alpha = 0.08f), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileItemRow(icon = Icons.Default.Phone, title = "Mobile Number", subtitle = "+94 77 123 4567")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // App Options
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, SlateGrey.copy(alpha = 0.12f))
        ) {
            Column {
                ProfileItemRow(icon = Icons.Default.Lock, title = "Change Password", subtitle = "Secure your login credentials")
                Divider(color = SlateGrey.copy(alpha = 0.08f), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileItemRow(icon = Icons.Default.Lock, title = "Privacy & Safety", subtitle = "Account security configuration")
                Divider(color = SlateGrey.copy(alpha = 0.08f), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileItemRow(icon = Icons.Default.Settings, title = "App Settings", subtitle = "Theme, units & notifications")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Sign Out", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun ProfileItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
            .bounceClick(interactionSource)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = SlateGrey)
        }

        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = SlateGrey)
    }
}
