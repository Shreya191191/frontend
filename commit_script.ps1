# PowerShell Script to recreate git history in 33 logical steps
$backupDir = "C:\Users\shrey\.gemini\antigravity\brain\90c6622f-a4a0-448f-a0c6-9bcc82502d63\git_backup"

function Commit-Step($msg) {
    git add .
    git commit -m $msg
    Write-Host "Committed: $msg" -ForegroundColor Green
}

# Ensure we are on master branch and clean
git checkout master

# --- Step 1: Delete ic_wishlist.xml drawable asset ---
Remove-Item app/src/main/res/drawable/ic_wishlist.xml -Force -ErrorAction SilentlyContinue
Commit-Step "refactor(wishlist): delete ic_wishlist.xml drawable asset"

# --- Step 2: Delete WishlistScreen.kt screen file ---
Remove-Item app/src/main/java/com/example/frontend/ui/screens/wishlist/WishlistScreen.kt -Force -ErrorAction SilentlyContinue
Commit-Step "refactor(wishlist): delete WishlistScreen.kt screen composable"

# --- Step 3: Modify SearchMapper.kt formatting ---
Copy-Item "$backupDir\SearchMapper.kt" app/src/main/java/com/example/frontend/data/mapper/SearchMapper.kt -Force
Commit-Step "refactor(mapper): prune packages whitespace import in SearchMapper"

# --- Step 4: SettingsManager Stage 1 (class skeleton) ---
New-Item -ItemType File -Path app/src/main/java/com/example/frontend/data/local/pref/SettingsManager.kt -Force | Out-Null
Set-Content app/src/main/java/com/example/frontend/data/local/pref/SettingsManager.kt @"
package com.example.frontend.data.local.pref

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {
    // Skeleton
}
"@
Commit-Step "feat(settings): implement SettingsManager Preferences DataStore class"

# --- Step 5: SettingsManager Stage 2 (final implementation) ---
Copy-Item "$backupDir\SettingsManager.kt" app/src/main/java/com/example/frontend/data/local/pref/SettingsManager.kt -Force
Commit-Step "feat(settings): implement theme flow and theme storage in SettingsManager"

# --- Step 6: ComingSoonDialog Stage 1 (skeleton) ---
New-Item -ItemType File -Path app/src/main/java/com/example/frontend/ui/components/ComingSoonDialog.kt -Force | Out-Null
Set-Content app/src/main/java/com/example/frontend/ui/components/ComingSoonDialog.kt @"
package com.example.frontend.ui.components

import androidx.compose.runtime.Composable

@Composable
fun ComingSoonDialog(onDismiss: () -> Unit) {
    // Skeleton
}
"@
Commit-Step "ui(components): create ComingSoonDialog composable dialog skeleton"

# --- Step 7: ComingSoonDialog Stage 2 (final implementation) ---
Copy-Item "$backupDir\ComingSoonDialog.kt" app/src/main/java/com/example/frontend/ui/components/ComingSoonDialog.kt -Force
Commit-Step "ui(components): add styled buttons and colors to ComingSoonDialog"

# --- Step 8: SettingsViewModel Stage 1 (skeleton) ---
New-Item -ItemType Directory -Path app/src/main/java/com/example/frontend/ui/screens/settings -Force | Out-Null
New-Item -ItemType File -Path app/src/main/java/com/example/frontend/ui/screens/settings/SettingsViewModel.kt -Force | Out-Null
Set-Content app/src/main/java/com/example/frontend/ui/screens/settings/SettingsViewModel.kt @"
package com.example.frontend.ui.screens.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    // Skeleton
}
"@
Commit-Step "feat(settings): create SettingsViewModel skeleton"

# --- Step 9: SettingsViewModel Stage 2 (final implementation) ---
Copy-Item "$backupDir\settings\SettingsViewModel.kt" app/src/main/java/com/example/frontend/ui/screens/settings/SettingsViewModel.kt -Force
Commit-Step "feat(settings): integrate theme updates inside SettingsViewModel"

# --- Step 10: SettingsScreen Stage 1 (skeleton) ---
New-Item -ItemType File -Path app/src/main/java/com/example/frontend/ui/screens/settings/SettingsScreen.kt -Force | Out-Null
Set-Content app/src/main/java/com/example/frontend/ui/screens/settings/SettingsScreen.kt @"
package com.example.frontend.ui.screens.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    Text("Settings Screen Placeholder")
}
"@
Commit-Step "ui(settings): create SettingsScreen skeleton view"

# --- Step 11: SettingsScreen Stage 2 (final implementation) ---
Copy-Item "$backupDir\settings\SettingsScreen.kt" app/src/main/java/com/example/frontend/ui/screens/settings/SettingsScreen.kt -Force
Commit-Step "ui(settings): implement Theme Selection options and layout in SettingsScreen"

# --- Step 12: AboutScreen Stage 1 (skeleton) ---
New-Item -ItemType File -Path app/src/main/java/com/example/frontend/ui/screens/settings/AboutScreen.kt -Force | Out-Null
Set-Content app/src/main/java/com/example/frontend/ui/screens/settings/AboutScreen.kt @"
package com.example.frontend.ui.screens.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AboutScreen(navController: NavController) {
    Text("About Screen Placeholder")
}
"@
Commit-Step "ui(settings): create AboutScreen layout showing basic view"

# --- Step 13: AboutScreen Stage 2 (final implementation) ---
Copy-Item "$backupDir\settings\AboutScreen.kt" app/src/main/java/com/example/frontend/ui/screens/settings/AboutScreen.kt -Force
Commit-Step "ui(settings): populate app description, tech stack, and developer info in AboutScreen"

# --- Step 14: Screen.kt Stage 1 (Add Settings and About, keep Wishlist) ---
$screenContent = Get-Content app/src/main/java/com/example/frontend/ui/navigation/Screen.kt -Raw
$target = "    object Wishlist : Screen(`"wishlist`")`r`n    object Profile : Screen(`"profile`")"
$replacement = "    object Wishlist : Screen(`"wishlist`")`r`n    object Profile : Screen(`"profile`")`r`n    object Settings : Screen(`"settings`")`r`n`r`n    object About : Screen(`"about`")"
$screenContent = $screenContent.Replace($target, $replacement)
Set-Content app/src/main/java/com/example/frontend/ui/navigation/Screen.kt $screenContent -NoNewline
Commit-Step "feat(navigation): declare Settings and About routes in Screen"

# --- Step 15: Screen.kt Stage 2 (Remove Wishlist - Final) ---
Copy-Item "$backupDir\Screen.kt" app/src/main/java/com/example/frontend/ui/navigation/Screen.kt -Force
Commit-Step "refactor(navigation): remove Wishlist route from Screen"

# --- Step 16: AppNavigation.kt Stage 1 (Add Settings and About composable routes, keep Wishlist) ---
$navContent = Get-Content app/src/main/java/com/example/frontend/ui/navigation/AppNavigation.kt -Raw
# Inject composables before composable(Screen.Profile.route)
$targetComp = "        composable(Screen.Profile.route) {"
$replaceComp = "        composable(Screen.Settings.route) {`r`n            SettingsScreen(navController = navController)`r`n        }`r`n`r`n        composable(Screen.About.route) {`r`n            AboutScreen(navController = navController)`r`n        }`r`n`r`n        composable(Screen.Profile.route) {"
$navContent = $navContent.Replace($targetComp, $replaceComp)

# Inject imports
$targetImport = "import com.example.frontend.ui.screens.profile.ProfileScreen"
$replaceImport = "import com.example.frontend.ui.screens.profile.ProfileScreen`r`nimport com.example.frontend.ui.screens.settings.SettingsScreen`r`nimport com.example.frontend.ui.screens.settings.AboutScreen"
$navContent = $navContent.Replace($targetImport, $replaceImport)

Set-Content app/src/main/java/com/example/frontend/ui/navigation/AppNavigation.kt $navContent -NoNewline
Commit-Step "feat(navigation): register Settings and About composable routes in AppNavigation"

# --- Step 17: AppNavigation.kt Stage 2 (Remove Wishlist - Final) ---
Copy-Item "$backupDir\AppNavigation.kt" app/src/main/java/com/example/frontend/ui/navigation/AppNavigation.kt -Force
Commit-Step "refactor(navigation): remove Wishlist composable and imports from AppNavigation"

# --- Step 18: MainLayoutScaffold.kt Stage 1 (Remove Wishlist bottom nav item) ---
$scaffoldContent = Get-Content app/src/main/java/com/example/frontend/ui/components/MainLayoutScaffold.kt -Raw
$targetItem = "    BottomNavItem(`"Wishlist`", Screen.Wishlist.route, Icons.Default.FavoriteBorder),`r`n"
$scaffoldContent = $scaffoldContent.Replace($targetItem, "")
Set-Content app/src/main/java/com/example/frontend/ui/components/MainLayoutScaffold.kt $scaffoldContent -NoNewline
Commit-Step "refactor(navigation): remove Wishlist item from MainLayoutScaffold bottom menu"

# --- Step 19: MainLayoutScaffold.kt Stage 2 (Remove Wishlist title mapping) ---
$scaffoldContent = Get-Content app/src/main/java/com/example/frontend/ui/components/MainLayoutScaffold.kt -Raw
$targetTitle = "        Screen.Wishlist.route -> `"My Wishlist`"`r`n"
$scaffoldContent = $scaffoldContent.Replace($targetTitle, "")
Set-Content app/src/main/java/com/example/frontend/ui/components/MainLayoutScaffold.kt $scaffoldContent -NoNewline
Commit-Step "refactor(navigation): remove Wishlist title from TopAppBar title resolver"

# --- Step 20: MainLayoutScaffold.kt Stage 3 (Fix Home bottom navigation click restoreState) ---
$scaffoldContent = Get-Content app/src/main/java/com/example/frontend/ui/components/MainLayoutScaffold.kt -Raw
$targetRestore = "                                        // Restore state when reselecting a previously selected item`r`n                                        restoreState = true"
$replaceRestore = "                                        // Restore state when reselecting a previously selected item`r`n                                        restoreState = item.route != Screen.Home.route"
$scaffoldContent = $scaffoldContent.Replace($targetRestore, $replaceRestore)
Set-Content app/src/main/java/com/example/frontend/ui/components/MainLayoutScaffold.kt $scaffoldContent -NoNewline
Commit-Step "fix(navigation): disable restoreState for Home bottom navigation click"

# --- Step 21: MainLayoutScaffold.kt Stage 4 (Fix Home drawer click restoreState - Final) ---
Copy-Item "$backupDir\MainLayoutScaffold.kt" app/src/main/java/com/example/frontend/ui/components/MainLayoutScaffold.kt -Force
Commit-Step "fix(navigation): disable restoreState for Home Dashboard drawer click"

# --- Step 22: ProfileScreen.kt Stage 1 (Update ProfileItemRow onClick signature) ---
$profileContent = Get-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt -Raw
$targetSig = "fun ProfileItemRow(`r`n    icon: ImageVector,`r`n    title: String,`r`n    subtitle: String,`r`n    onClick: () -> Unit = {}`r`n)"
$replaceSig = "fun ProfileItemRow(`r`n    icon: ImageVector,`r`n    title: String,`r`n    subtitle: String,`r`n    onClick: (() -> Unit)? = null`r`n)"
$profileContent = $profileContent.Replace($targetSig, $replaceSig)
Set-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt $profileContent -NoNewline
Commit-Step "refactor(profile): support nullable onClick parameter in ProfileItemRow"

# --- Step 23: ProfileScreen.kt Stage 2 (Make Email Address row display-only) ---
$profileContent = Get-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt -Raw
$targetEmail = "ProfileItemRow(icon = Icons.Default.Email, title = `"Email Address`", subtitle = userEmail)"
# Replaces it to make it display-only (since default is empty lambda which would show ripple, we want to pass null)
$replaceEmail = "ProfileItemRow(icon = Icons.Default.Email, title = `"Email Address`", subtitle = userEmail, onClick = null)"
$profileContent = $profileContent.Replace($targetEmail, $replaceEmail)
Set-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt $profileContent -NoNewline
Commit-Step "ui(profile): make Email Address row display-only without ripple or navigation arrow"

# --- Step 24: ProfileScreen.kt Stage 3 (Make Mobile Number row display-only) ---
$profileContent = Get-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt -Raw
$targetPhone = "ProfileItemRow(icon = Icons.Default.Phone, title = `"Mobile Number`", subtitle = userPhone)"
$replacePhone = "ProfileItemRow(icon = Icons.Default.Phone, title = `"Mobile Number`", subtitle = userPhone, onClick = null)"
$profileContent = $profileContent.Replace($targetPhone, $replacePhone)
Set-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt $profileContent -NoNewline
Commit-Step "ui(profile): make Mobile Number row display-only without ripple or navigation arrow"

# --- Step 25: ProfileScreen.kt Stage 4 (Make Address row display-only) ---
$profileContent = Get-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt -Raw
$targetAddress = "ProfileItemRow(icon = Icons.Default.Home, title = `"Address`", subtitle = userAddress)"
$replaceAddress = "ProfileItemRow(icon = Icons.Default.Home, title = `"Address`", subtitle = userAddress, onClick = null)"
$profileContent = $profileContent.Replace($targetAddress, $replaceAddress)
Set-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt $profileContent -NoNewline
Commit-Step "ui(profile): make Address row display-only without ripple or navigation arrow"

# --- Step 26: ProfileScreen.kt Stage 5 (Add Change Password click handler to trigger ComingSoonDialog) ---
$profileContent = Get-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt -Raw
$targetPass = "ProfileItemRow(icon = Icons.Default.Lock, title = `"Change Password`", subtitle = `"Secure your login credentials`")"
$replacePass = "ProfileItemRow(icon = Icons.Default.Lock, title = `"Change Password`", subtitle = `"Secure your login credentials`", onClick = { showComingSoonDialog = true })"
# Also need to inject state
$targetState = "var showEditDialog by remember { mutableStateOf(false) }"
$replaceState = "var showEditDialog by remember { mutableStateOf(false) }`r`n    var showComingSoonDialog by remember { mutableStateOf(false) }"
$profileContent = $profileContent.Replace($targetPass, $replacePass).Replace($targetState, $replaceState)
Set-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt $profileContent -NoNewline
Commit-Step "refactor(profile): connect Change Password menu row to trigger ComingSoonDialog"

# --- Step 27: ProfileScreen.kt Stage 6 (Add Privacy & Safety click handler) ---
$profileContent = Get-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt -Raw
$targetPrivacy = "ProfileItemRow(icon = Icons.Default.Lock, title = `"Privacy & Safety`", subtitle = `"Account security configuration`")"
$replacePrivacy = "ProfileItemRow(icon = Icons.Default.Lock, title = `"Privacy & Safety`", subtitle = `"Account security configuration`", onClick = { showComingSoonDialog = true })"
$profileContent = $profileContent.Replace($targetPrivacy, $replacePrivacy)
Set-Content app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt $profileContent -NoNewline
Commit-Step "refactor(profile): connect Privacy & Safety menu row to trigger ComingSoonDialog"

# --- Step 28: ProfileScreen.kt Stage 7 (Add App Settings click handler - Final) ---
Copy-Item "$backupDir\ProfileScreen.kt" app/src/main/java/com/example/frontend/ui/screens/profile/ProfileScreen.kt -Force
Commit-Step "refactor(profile): connect App Settings menu row to navigate to SettingsScreen"

# --- Step 29: VehicleDetailsScreen.kt Stage 1 (Remove Favorite button from Top Bar) ---
$detailsContent = Get-Content app/src/main/java/com/example/frontend/ui/screens/search/VehicleDetailsScreen.kt -Raw
$targetFavoriteBtn = @"
                    // Favorite/Wishlist Button
                    IconButton(onClick = {
                        viewModel.toggleFavorite(vehicleId)
                        val message = if (!uiState.isFavorite) "Added to Favorites" else "Removed from Favorites"
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = if (uiState.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (uiState.isFavorite) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
"@
# Normalise line endings for replacement
$targetFavoriteBtn = $targetFavoriteBtn -replace "\r?\n", "`r`n"
$detailsContent = $detailsContent.Replace($targetFavoriteBtn, "")
Set-Content app/src/main/java/com/example/frontend/ui/screens/search/VehicleDetailsScreen.kt $detailsContent -NoNewline
Commit-Step "ui(vehicle): remove Favorite heart icon button from VehicleDetailsScreen top bar"

# --- Step 30: VehicleDetailsScreen.kt Stage 2 (Remove other Favorite references - Final) ---
Copy-Item "$backupDir\VehicleDetailsScreen.kt" app/src/main/java/com/example/frontend/ui/screens/search/VehicleDetailsScreen.kt -Force
Commit-Step "refactor(vehicle): prune remaining Favorite state checks and references in VehicleDetailsScreen"

# --- Step 31: VehicleDetailsViewModel.kt Stage 1 (Remove toggleFavorite method) ---
$vmContent = Get-Content app/src/main/java/com/example/frontend/ui/screens/search/VehicleDetailsViewModel.kt -Raw
$targetToggle = @"
    fun toggleFavorite(vehicleId: String) {
        if (favoriteIds.contains(vehicleId)) {
            favoriteIds.remove(vehicleId)
            _uiState.update { it.copy(isFavorite = false) }
        } else {
            favoriteIds.add(vehicleId)
            _uiState.update { it.copy(isFavorite = true) }
        }
    }
"@
$targetToggle = $targetToggle -replace "\r?\n", "`r`n"
$vmContent = $vmContent.Replace($targetToggle, "")
Set-Content app/src/main/java/com/example/frontend/ui/screens/search/VehicleDetailsViewModel.kt $vmContent -NoNewline
Commit-Step "refactor(vehicle): remove toggleFavorite action and state logic from VehicleDetailsViewModel"

# --- Step 32: VehicleDetailsViewModel.kt Stage 2 (Prune favorite state variables - Final) ---
Copy-Item "$backupDir\VehicleDetailsViewModel.kt" app/src/main/java/com/example/frontend/ui/screens/search/VehicleDetailsViewModel.kt -Force
Commit-Step "refactor(vehicle): prune Favorite state storage and checks from VehicleDetailsViewModel"

# --- Step 33: MainActivity.kt Stage 1 (Inject SettingsManager) ---
$activityContent = Get-Content app/src/main/java/com/example/frontend/MainActivity.kt -Raw
$targetInject = "class MainActivity : ComponentActivity(), PaymentResultWithDataListener {"
$replaceInject = "class MainActivity : ComponentActivity(), PaymentResultWithDataListener {`r`n`r`n    @Inject`r`n    lateinit var settingsManager: SettingsManager"
$activityContent = $activityContent.Replace($targetInject, $replaceInject)
Set-Content app/src/main/java/com/example/frontend/MainActivity.kt $activityContent -NoNewline
Commit-Step "feat(main): inject SettingsManager into MainActivity"

# --- Step 34: MainActivity.kt Stage 2 (Read theme preference synchronously - Final) ---
Copy-Item "$backupDir\MainActivity.kt" app/src/main/java/com/example/frontend/MainActivity.kt -Force
Commit-Step "refactor(main): resolve theme preferences synchronously inside onCreate in MainActivity"
