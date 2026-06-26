package com.example.frontend.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.R
import com.example.frontend.ui.screens.home.MockVehicle
import com.example.frontend.ui.screens.home.VehicleListItem
import com.example.frontend.ui.theme.EmeraldPrimary
import com.example.frontend.ui.theme.SlateGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val matchedVehicles = remember {
        listOf(
            MockVehicle("1", "Tesla Model S", "Luxury", "$120/day", R.drawable.a_b, 5, "Automatic", 4.9f, true, "1.2 km away"),
            MockVehicle("2", "Audi Q7 Super", "SUV", "$95/day", R.drawable.b_c, 7, "Automatic", 4.8f, true, "1.8 km away"),
            MockVehicle("3", "BMW M4 Coupe", "Luxury", "$150/day", R.drawable.promo_car, 4, "Automatic", 4.9f, false, "2.2 km away"),
            MockVehicle("4", "Volkswagen Golf", "Hatchback", "$45/day", R.drawable.empty_vehicle, 5, "Manual", 4.5f, false, "3.0 km away")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Simple Top Search Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Search Results",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "4 Vehicles Available • Colombo Airport",
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateGrey
                )
            }
            IconButton(onClick = { /* Filter sheet trigger */ }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Filter results",
                    tint = EmeraldPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(matchedVehicles) { vehicle ->
                VehicleListItem(vehicle = vehicle) {
                    // Navigate to details in future module
                }
            }
        }
    }
}
