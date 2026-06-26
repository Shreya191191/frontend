package com.example.frontend.ui.screens.home

import android.widget.Toast
import com.example.frontend.ui.components.bounceClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.frontend.R
import com.example.frontend.ui.components.RatingBar
import com.example.frontend.ui.components.RentARideButton
import com.example.frontend.ui.components.RentARideDatePicker
import com.example.frontend.ui.components.RentARideTimePicker
import com.example.frontend.ui.navigation.Screen
import com.example.frontend.ui.theme.EmeraldPrimary
import com.example.frontend.ui.theme.EmeraldPrimaryContainer
import com.example.frontend.ui.theme.SlateGrey
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Search state parameters
    var selectedDistrict by remember { mutableStateOf("Colombo") }
    var selectedPickUpLocation by remember { mutableStateOf("Colombo Airport (CMB)") }
    var selectedDropOffLocation by remember { mutableStateOf("Colombo Airport (CMB)") }
    
    var pickUpDate by remember { mutableStateOf<Calendar?>(null) }
    var dropOffDate by remember { mutableStateOf<Calendar?>(null) }
    var pickUpTime by remember { mutableStateOf<Calendar?>(null) }
    var dropOffTime by remember { mutableStateOf<Calendar?>(null) }

    // Mock drop-down choices
    val districts = listOf("Colombo", "Kandy", "Galle", "Negombo")
    val locations = mapOf(
        "Colombo" to listOf("Colombo Airport (CMB)", "Colombo Downtown", "Galle Face Green"),
        "Kandy" to listOf("Kandy Central Station", "Kandy Lake View"),
        "Galle" to listOf("Galle Fort Gate", "Galle Coastal Rd"),
        "Negombo" to listOf("Negombo Beach Resort", "Negombo Central")
    )

    // District and Location Dropdown control
    var showDistrictMenu by remember { mutableStateOf(false) }
    var showPickUpMenu by remember { mutableStateOf(false) }
    var showDropOffMenu by remember { mutableStateOf(false) }

    // Vehicle mock lists
    val featuredVehicles = remember {
        listOf(
            MockVehicle("1", "Tesla Model S", "Luxury", "$120/day", R.drawable.a_b, 5, "Automatic", 4.9f, true),
            MockVehicle("2", "Audi Q7 Super", "SUV", "$95/day", R.drawable.b_c, 7, "Automatic", 4.8f, true),
            MockVehicle("3", "BMW M4 Coupe", "Luxury", "$150/day", R.drawable.promo_car, 4, "Automatic", 4.9f, false)
        )
    }

    val recommendedVehicles = remember {
        listOf(
            MockVehicle("4", "Volkswagen Golf", "Hatchback", "$45/day", R.drawable.empty_vehicle, 5, "Manual", 4.5f, false),
            MockVehicle("2", "Audi Q7 Super", "SUV", "$95/day", R.drawable.b_c, 7, "Automatic", 4.8f, true),
            MockVehicle("1", "Tesla Model S", "Luxury", "$120/day", R.drawable.a_b, 5, "Automatic", 4.9f, true)
        )
    }

    val nearbyVehicles = remember {
        listOf(
            MockVehicle("3", "BMW M4 Coupe", "Luxury", "$150/day", R.drawable.promo_car, 4, "Automatic", 4.9f, false, "1.2 km away"),
            MockVehicle("4", "Volkswagen Golf", "Hatchback", "$45/day", R.drawable.empty_vehicle, 5, "Manual", 4.5f, false, "2.5 km away"),
            MockVehicle("2", "Audi Q7 Super", "SUV", "$95/day", R.drawable.b_c, 7, "Automatic", 4.8f, true, "3.1 km away")
        )
    }

    val categories = listOf(
        CategoryItem("SUV", R.drawable.b_c, "12 Available"),
        CategoryItem("Sedan", R.drawable.a_b, "18 Available"),
        CategoryItem("Luxury", R.drawable.promo_car, "8 Available"),
        CategoryItem("Hatchback", R.drawable.empty_vehicle, "15 Available")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Welcome and Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                EmeraldPrimary.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(
                        text = "Hello, Shreyas 👋",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Find the perfect car for your next adventure.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = SlateGrey
                    )
                }
            }
        }

        // Search Widget
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-40).dp)
                    .shadow(12.dp, shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Book Your Ride",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    // District Selector
                    Box {
                        OutlinedButton(
                            onClick = { showDistrictMenu = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = EmeraldPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(horizontalAlignment = Alignment.Start) {
                                        Text("Select District", style = MaterialTheme.typography.labelSmall, color = SlateGrey)
                                        Text(selectedDistrict, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = SlateGrey)
                            }
                        }
                        DropdownMenu(
                            expanded = showDistrictMenu,
                            onDismissRequest = { showDistrictMenu = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            districts.forEach { dist ->
                                DropdownMenuItem(
                                    text = { Text(dist) },
                                    onClick = {
                                        selectedDistrict = dist
                                        selectedPickUpLocation = locations[dist]?.first() ?: ""
                                        selectedDropOffLocation = locations[dist]?.first() ?: ""
                                        showDistrictMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Pick-up Location Selector
                    Box {
                        OutlinedButton(
                            onClick = { showPickUpMenu = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = EmeraldPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(horizontalAlignment = Alignment.Start) {
                                        Text("Pick-up Location", style = MaterialTheme.typography.labelSmall, color = SlateGrey)
                                        Text(selectedPickUpLocation, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = SlateGrey)
                            }
                        }
                        DropdownMenu(
                            expanded = showPickUpMenu,
                            onDismissRequest = { showPickUpMenu = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            locations[selectedDistrict]?.forEach { loc ->
                                DropdownMenuItem(
                                    text = { Text(loc) },
                                    onClick = {
                                        selectedPickUpLocation = loc
                                        showPickUpMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Drop-off Location Selector
                    Box {
                        OutlinedButton(
                            onClick = { showDropOffMenu = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = EmeraldPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(horizontalAlignment = Alignment.Start) {
                                        Text("Drop-off Location", style = MaterialTheme.typography.labelSmall, color = SlateGrey)
                                        Text(selectedDropOffLocation, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                }
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = SlateGrey)
                            }
                        }
                        DropdownMenu(
                            expanded = showDropOffMenu,
                            onDismissRequest = { showDropOffMenu = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            locations[selectedDistrict]?.forEach { loc ->
                                DropdownMenuItem(
                                    text = { Text(loc) },
                                    onClick = {
                                        selectedDropOffLocation = loc
                                        showDropOffMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Dates and Times
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            RentARideDatePicker(
                                label = "Pick-up Date",
                                selectedDate = pickUpDate,
                                onDateSelected = { pickUpDate = it }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            RentARideTimePicker(
                                label = "Pick-up Time",
                                selectedTime = pickUpTime,
                                onTimeSelected = { h, m ->
                                    pickUpTime = Calendar.getInstance().apply {
                                        set(Calendar.HOUR_OF_DAY, h)
                                        set(Calendar.MINUTE, m)
                                    }
                                }
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            RentARideDatePicker(
                                label = "Drop-off Date",
                                selectedDate = dropOffDate,
                                onDateSelected = { dropOffDate = it },
                                minDate = pickUpDate ?: Calendar.getInstance()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            RentARideTimePicker(
                                label = "Drop-off Time",
                                selectedTime = dropOffTime,
                                onTimeSelected = { h, m ->
                                    dropOffTime = Calendar.getInstance().apply {
                                        set(Calendar.HOUR_OF_DAY, h)
                                        set(Calendar.MINUTE, m)
                                    }
                                }
                            )
                        }
                    }

                    // Search Button
                    RentARideButton(
                        text = "Search Available Cars",
                        onClick = {
                            if (pickUpDate == null || dropOffDate == null) {
                                Toast.makeText(context, "Please select pick-up and drop-off dates", Toast.LENGTH_SHORT).show()
                            } else {
                                navController.navigate(Screen.SearchResults.route)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = EmeraldPrimary,
                        contentColor = Color.Black
                    )
                }
            }
        }

        // Featured Vehicles
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Featured Vehicles",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "See All",
                        style = MaterialTheme.typography.labelLarge,
                        color = EmeraldPrimary,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Search.route)
                        }
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(featuredVehicles) { vehicle ->
                        VehicleFeaturedCard(vehicle = vehicle) {
                            navController.navigate(Screen.SearchResults.route)
                        }
                    }
                }
            }
        }

        // Popular Categories
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(
                    text = "Popular Categories",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories) { category ->
                        CategoryCard(category = category) {
                            navController.navigate(Screen.Search.route)
                        }
                    }
                }
            }
        }

        // Special Offers / Promotions
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(
                    text = "Special Offers",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(140.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            Toast.makeText(context, "Promo code RIDE50 copied!", Toast.LENGTH_SHORT).show()
                        },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.promo_car),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxHeight()
                                .width(200.dp)
                                .offset(x = 10.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(16.dp)
                                .fillMaxWidth(0.55f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Weekly Special",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldPrimary
                            )
                            Text(
                                text = "Get 20% off luxury SUVs this weekend!",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Code: RIDE50",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Recommended Vehicles
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(
                    text = "Recommended for You",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(recommendedVehicles) { vehicle ->
                        VehicleHorizontalCard(vehicle = vehicle) {
                            navController.navigate(Screen.SearchResults.route)
                        }
                    }
                }
            }
        }

        // Nearby Rentals
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(
                    text = "Available Near You",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    nearbyVehicles.forEach { vehicle ->
                        VehicleListItem(vehicle = vehicle) {
                            navController.navigate(Screen.SearchResults.route)
                        }
                    }
                }
            }
        }
    }
}

// Data structures
data class MockVehicle(
    val id: String,
    val name: String,
    val type: String,
    val price: String,
    val imageRes: Int,
    val seats: Int,
    val transmission: String,
    val rating: Float,
    val isElectric: Boolean,
    val distance: String? = null
)

data class CategoryItem(
    val name: String,
    val imageRes: Int,
    val countText: String
)

@Composable
fun VehicleFeaturedCard(
    vehicle: MockVehicle,
    onClick: () -> Unit
) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(260.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
            .bounceClick(interactionSource),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, SlateGrey.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f)
                    .background(if (isSystemInDarkTheme()) Color(0xFF18281F) else Color(0xFFE8F5E9)) // light/dark tint back panel
            ) {
                Image(
                    painter = painterResource(id = vehicle.imageRes),
                    contentDescription = vehicle.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .padding(16.dp)
                )

                if (vehicle.isElectric) {
                    Surface(
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.TopEnd),
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldPrimary,
                        contentColor = Color.Black
                    ) {
                        Text(
                            text = "⚡ EV",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(
                    text = vehicle.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${vehicle.type} • ${vehicle.transmission} • ${vehicle.seats} Seats",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateGrey
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = vehicle.price,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimary
                    )
                    RatingBar(rating = vehicle.rating)
                }
            }
        }
    }
}

@Composable
fun VehicleHorizontalCard(
    vehicle: MockVehicle,
    onClick: () -> Unit
) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(200.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
            .bounceClick(interactionSource),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, SlateGrey.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(if (isSystemInDarkTheme()) Color(0xFF152A3F) else Color(0xFFE3F2FD)) // Light blue tint back panel
            ) {
                Image(
                    painter = painterResource(id = vehicle.imageRes),
                    contentDescription = vehicle.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = vehicle.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = vehicle.price,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                )
            }
        }
    }
}

@Composable
fun VehicleListItem(
    vehicle: MockVehicle,
    onClick: () -> Unit
) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
            .bounceClick(interactionSource),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, SlateGrey.copy(alpha = 0.15f))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(if (isSystemInDarkTheme()) Color(0xFF22351A) else Color(0xFFF1F8E9))
            ) {
                Image(
                    painter = painterResource(id = vehicle.imageRes),
                    contentDescription = vehicle.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = vehicle.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = SlateGrey
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = vehicle.distance ?: "0.5 km away",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateGrey
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = vehicle.price,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Details",
                    tint = SlateGrey
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: CategoryItem,
    onClick: () -> Unit
) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(130.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current,
                onClick = onClick
            )
            .bounceClick(interactionSource),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, SlateGrey.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = category.imageRes),
                    contentDescription = category.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = category.countText,
                style = MaterialTheme.typography.labelSmall,
                color = SlateGrey
            )
        }
    }
}
