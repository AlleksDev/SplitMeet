package com.coditos.splitmeet.core.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.coditos.splitmeet.R
import com.coditos.splitmeet.features.group.presentation.screens.GroupsScreen
import com.coditos.splitmeet.features.home.presentation.screens.HomeScreen
import com.coditos.splitmeet.features.notification.presentation.screens.NotificationScreen
import com.coditos.splitmeet.features.profile.presentation.screens.ProfileScreen

@Composable
fun MainScreen(
    onNavigateToCreateOuting: () -> Unit,
    onNavigateToCreateGroup: () -> Unit,
    onNavigateToOutingDetail: (Long) -> Unit,
    onNavigateToGroupDetail: (Long) -> Unit,
    onScanQrClick: () -> Unit,
    onLoggedOut: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onScanQrClick()
        } else {
            Toast.makeText(context, "Se necesita acceso a la cámara para escanear el QR", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.split),
                        contentDescription = "SplitMeet Logo",
                        modifier = Modifier.height(32.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = {
                        val permissionCheckResult = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        )

                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            onScanQrClick()
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.QrCodeScanner,
                            contentDescription = "Escanear QR",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { if (item.enabled) selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == index) item.selectedIcon else item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label, fontSize = 11.sp) },
                        enabled = item.enabled,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 0 || selectedTab == 1) {
                FloatingActionButton(
                    onClick = {
                        if (selectedTab == 0) onNavigateToCreateOuting()
                        else onNavigateToCreateGroup()
                    },
                    containerColor = Color(0xFFE67E22),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = if (selectedTab == 0) "Crear salida" else "Crear grupo")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    onNavigateToOutingDetail = onNavigateToOutingDetail
                )
                1 -> GroupsScreen(
                    onNavigateToDetail = onNavigateToGroupDetail
                )
                2 -> NotificationScreen()
                3 -> ProfileScreen(onLoggedOut = onLoggedOut)
            }
        }
    }
}

private data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val enabled: Boolean
)

private val bottomNavItems = listOf(
    BottomNavItem("Inicio", Icons.Outlined.Home, Icons.Filled.Home, true),
    BottomNavItem("Grupos", Icons.Outlined.Groups, Icons.Filled.Groups, true),
    BottomNavItem("Notificaciones", Icons.Outlined.Notifications, Icons.Filled.Notifications, true),
    BottomNavItem("Perfil", Icons.Outlined.AccountCircle, Icons.Filled.AccountCircle, true)
)
