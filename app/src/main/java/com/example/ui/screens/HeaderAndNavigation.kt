package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.IshcScreen
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHeader(
    currentScreen: IshcScreen,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onOpenDrawer: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToExperts: () -> Unit,
    onLogout: () -> Unit,
    userInitials: String = "AR",
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Surface(
        color = DarkNavy,
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Menu Icon + Brand Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("nav_drawer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Navigation Menu",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f, fill = false)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ISHC",
                                fontWeight = FontWeight.Black,
                                fontSize = 19.sp,
                                color = AccentGold
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AccentSky)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "GLOBAL",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }
                        Text(
                            text = "Int'l Students Help Center",
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }
                }

                // Top Quick Actions
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    // Experts Link
                    FilterChip(
                        selected = currentScreen == IshcScreen.EXPERTS,
                        onClick = onNavigateToExperts,
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.SupervisorAccount,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp),
                                    tint = AccentGold
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Experts", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = RoyalSlate,
                            selectedContainerColor = LightSlate
                        ),
                        border = null,
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("top_bar_experts_chip")
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    // Profile Avatar Chip
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(AccentGold)
                            .clickable { onNavigateToProfile() }
                            .testTag("top_bar_profile_chip")
                    ) {
                        Text(
                            text = userInitials.take(2).uppercase(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = DarkNavy
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    // Logout
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("top_bar_logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Centered Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = {
                    Text(
                        "Search profiles, programmes, experts...",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = AccentGold
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Search",
                                tint = Color.White
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = RoyalSlate,
                    unfocusedContainerColor = RoyalSlate,
                    focusedBorderColor = AccentGold,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("global_search_input")
            )
        }
    }
}

@Composable
fun DrawerNavigationContent(
    currentScreen: IshcScreen,
    onSelectScreen: (IshcScreen) -> Unit,
    onCloseDrawer: () -> Unit,
    userName: String = "Alex Rivera",
    userInitials: String = "AR"
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(DarkNavy)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Drawer Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Column {
                Text(
                    text = "ISHC DASHBOARD",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AccentGold
                )
                Text(
                    text = "Portal Version 2.4",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            IconButton(onClick = onCloseDrawer) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Menu",
                    tint = Color.White
                )
            }
        }

        HorizontalDivider(color = RoyalSlate, thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Items List
        val navItems = listOf(
            IshcScreen.PROGRAMMES to Icons.Default.Search,
            IshcScreen.FAVORITES to Icons.Default.Star,
            IshcScreen.JOURNEY to Icons.Default.Timeline,
            IshcScreen.DOCUMENTS to Icons.Default.FolderSpecial,
            IshcScreen.APPLICATIONS to Icons.Default.Send,
            IshcScreen.MEETINGS to Icons.Default.VideoCall,
            IshcScreen.SEMINARS to Icons.Default.Event,
            IshcScreen.EXPERTS to Icons.Default.SupervisorAccount,
            IshcScreen.PROFILE to Icons.Default.Person,
            IshcScreen.ADMIN to Icons.Default.AdminPanelSettings
        )

        Column(modifier = Modifier.weight(1f)) {
            navItems.forEach { (screen, icon) ->
                val isSelected = currentScreen == screen
                Surface(
                    color = if (isSelected) AccentGold.copy(alpha = 0.15f) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            onSelectScreen(screen)
                            onCloseDrawer()
                        }
                        .testTag("drawer_item_${screen.name.lowercase()}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = screen.title,
                            tint = if (isSelected) AccentGold else Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = screen.title,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) AccentGold else Color.White
                        )
                    }
                }
            }
        }

        // Student Mini Card Footer
        Surface(
            color = RoyalSlate,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(AccentGold)
                ) {
                    Text(
                        text = userInitials.take(2).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = DarkNavy
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Verified Student Account",
                        fontSize = 11.sp,
                        color = AccentEmerald
                    )
                }
            }
        }
    }
}

@Composable
fun AppBottomNavigationBar(
    currentScreen: IshcScreen,
    onSelectScreen: (IshcScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomItems = listOf(
        IshcScreen.PROGRAMMES to Icons.Default.Search,
        IshcScreen.FAVORITES to Icons.Default.Star,
        IshcScreen.JOURNEY to Icons.Default.Timeline,
        IshcScreen.DOCUMENTS to Icons.Default.Folder,
        IshcScreen.MEETINGS to Icons.Default.VideoCall
    )

    NavigationBar(
        containerColor = DarkNavy,
        contentColor = Color.White,
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        bottomItems.forEach { (screen, icon) ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectScreen(screen) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = screen.title,
                        tint = if (isSelected) AccentGold else Color.White.copy(alpha = 0.6f)
                    )
                },
                label = {
                    Text(
                        text = when(screen) {
                            IshcScreen.PROGRAMMES -> "Programs"
                            IshcScreen.FAVORITES -> "Favorites"
                            IshcScreen.JOURNEY -> "Journey"
                            IshcScreen.DOCUMENTS -> "Vault"
                            IshcScreen.MEETINGS -> "Meetings"
                            else -> screen.title
                        },
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) AccentGold else Color.White.copy(alpha = 0.6f)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = RoyalSlate
                ),
                modifier = Modifier.testTag("bottom_nav_${screen.name.lowercase()}")
            )
        }
    }
}
