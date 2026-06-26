package com.example.frontend.ui.screens.orders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.frontend.ui.theme.EmeraldPrimary
import com.example.frontend.ui.theme.SlateGrey

@Composable
fun OrdersScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Active", "Completed", "Cancelled")

    val mockOrders = remember {
        listOf(
            MockOrder("ORD-5829", "Tesla Model S", "Active", "$240", "Colombo Downtown", "Colombo Airport", "June 28 - June 30", R.drawable.a_b),
            MockOrder("ORD-1290", "Audi Q7 Super", "Completed", "$285", "Negombo Central", "Negombo Central", "June 10 - June 13", R.drawable.b_c),
            MockOrder("ORD-0841", "Volkswagen Golf", "Cancelled", "$90", "Kandy Central", "Kandy Central", "May 20 - May 22", R.drawable.empty_vehicle)
        )
    }

    val filteredOrders = when (selectedTab) {
        0 -> mockOrders.filter { it.status == "Active" }
        1 -> mockOrders.filter { it.status == "Completed" }
        else -> mockOrders.filter { it.status == "Cancelled" }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab selector
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = EmeraldPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = EmeraldPrimary
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    },
                    selectedContentColor = EmeraldPrimary,
                    unselectedContentColor = SlateGrey
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = SlateGrey.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No bookings found",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "You don't have any bookings in this section.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateGrey
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredOrders) { order ->
                    OrderCard(order = order)
                }
            }
        }
    }
}

data class MockOrder(
    val id: String,
    val carName: String,
    val status: String,
    val amount: String,
    val pickup: String,
    val dropoff: String,
    val dates: String,
    val imageRes: Int
)

@Composable
fun OrderCard(order: MockOrder) {
    val statusColor = when (order.status) {
        "Active" -> EmeraldPrimary
        "Completed" -> SlateGrey
        else -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, SlateGrey.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.id,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = SlateGrey
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.15f),
                    contentColor = statusColor
                ) {
                    Text(
                        text = order.status,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = order.imageRes),
                    contentDescription = order.carName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(80.dp)
                        .background(if (isSystemInDarkTheme()) Color(0xFF242424) else Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.carName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = order.dates,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateGrey
                    )
                }

                Text(
                    text = order.amount,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp), color = SlateGrey.copy(alpha = 0.1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = SlateGrey
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${order.pickup} ➔ ${order.dropoff}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateGrey,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
