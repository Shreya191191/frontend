package com.example.frontend.ui.screens.wishlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.frontend.R
import com.example.frontend.ui.components.RatingBar
import com.example.frontend.ui.components.RentARideButton
import com.example.frontend.ui.screens.home.MockVehicle
import com.example.frontend.ui.theme.EmeraldPrimary
import com.example.frontend.ui.theme.SlateGrey

@Composable
fun WishlistScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var wishlistItems by remember {
        mutableStateOf(
            listOf(
                MockVehicle("1", "Tesla Model S", "Luxury", "$120/day", R.drawable.a_b, 5, "Automatic", 4.9f, true),
                MockVehicle("2", "Audi Q7 Super", "SUV", "$95/day", R.drawable.b_c, 7, "Automatic", 4.8f, true)
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "My Saved Vehicles",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (wishlistItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = SlateGrey.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your wishlist is empty",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap the heart icon on vehicles to save them here.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateGrey
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(wishlistItems) { vehicle ->
                    WishlistCard(
                        vehicle = vehicle,
                        onRemove = {
                            wishlistItems = wishlistItems.filter { it.id != vehicle.id }
                        },
                        onBook = {
                            navController.navigate("search_results")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WishlistCard(
    vehicle: MockVehicle,
    onRemove: () -> Unit,
    onBook: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, SlateGrey.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = vehicle.imageRes),
                    contentDescription = vehicle.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(80.dp)
                        .background(if (isSystemInDarkTheme()) Color(0xFF242424) else Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = vehicle.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${vehicle.type} • ${vehicle.transmission} • ${vehicle.seats} Seats",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateGrey
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    RatingBar(rating = vehicle.rating)
                }

                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Remove from Favorites",
                        tint = EmeraldPrimary
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp), color = SlateGrey.copy(alpha = 0.1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Price",
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateGrey
                    )
                    Text(
                        text = vehicle.price,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                    )
                }

                Button(
                    onClick = onBook,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Rent Now", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
