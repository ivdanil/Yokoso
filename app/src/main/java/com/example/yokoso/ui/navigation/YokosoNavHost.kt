package com.example.yokoso.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.example.yokoso.ui.events.EventsScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.yokoso.data.network.SupabaseClient
import com.example.yokoso.ui.auth.AuthScreen
import com.example.yokoso.ui.common.Str
import com.example.yokoso.ui.common.tr
import com.example.yokoso.ui.phrasebook.PhrasebookScreen
import com.example.yokoso.ui.favorites.FavoritesScreen
import com.example.yokoso.ui.home.HomeScreen
import com.example.yokoso.ui.placedetail.PlaceDetailScreen
import com.example.yokoso.ui.profile.ProfileScreen
import com.example.yokoso.ui.review.ReviewFormScreen
import com.example.yokoso.ui.route.RouteScreen
import com.example.yokoso.ui.theme.AccentRed
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.withTimeoutOrNull

object Routes {
    const val AUTH = "auth"
    const val EVENTS = "events"
    const val MAIN = "main"
    const val HOME = "home"
    const val ROUTE = "route"
    const val PHRASEBOOK = "phrasebook"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val PLACE = "place/{id}"
    const val REVIEW = "review/{id}"

    fun place(id: String) = "place/$id"
    fun review(id: String) = "review/$id"
}

@Composable
fun YokosoNavHost() {
    // null = ещё проверяем, есть ли сохранённая сессия
    var startRoute by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val auth = SupabaseClient.client.auth
        // ждём загрузки сессии из хранилища, но не дольше 5 секунд
        withTimeoutOrNull(5000) { auth.awaitInitialization() }
        startRoute = if (auth.currentSessionOrNull() != null) Routes.MAIN else Routes.AUTH
    }

    val start = startRoute
    if (start == null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val rootNav = rememberNavController()
    NavHost(navController = rootNav, startDestination = start) {
        composable(Routes.AUTH) {
            AuthScreen(onAuthSuccess = {
                rootNav.navigate(Routes.MAIN) {
                    popUpTo(Routes.AUTH) { inclusive = true }
                }
            })
        }
        composable(Routes.MAIN) {
            MainScreen(onLogout = {
                rootNav.navigate(Routes.AUTH) {
                    popUpTo(Routes.MAIN) { inclusive = true }
                }
            })
        }
    }
}

@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val items = listOf(
        Triple(Routes.HOME, Icons.Default.Home, Str.NavHome),
        Triple(Routes.ROUTE, Icons.Default.Map, Str.NavRoute),
        Triple(Routes.FAVORITES, Icons.Default.Favorite, Str.NavFav),
        Triple(Routes.PROFILE, Icons.Default.Person, Str.NavProfile)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.hierarchy?.firstOrNull()?.route

                items.forEach { (route, icon, labelKey) ->
                    NavigationBarItem(
                        selected = currentRoute == route,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, contentDescription = tr(labelKey)) },
                        label = { Text(tr(labelKey)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentRed,
                            selectedTextColor = AccentRed
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onPlaceClick = { id -> navController.navigate(Routes.place(id)) },
                    onEventsClick = { navController.navigate(Routes.EVENTS) },
                    onPhrasebookClick = { navController.navigate(Routes.PHRASEBOOK) }
                )
            }
            composable(Routes.PHRASEBOOK) {
                PhrasebookScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.EVENTS) {
                EventsScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.PLACE) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                PlaceDetailScreen(
                    placeId = id,
                    onBack = { navController.popBackStack() },
                    onWriteReview = { navController.navigate(Routes.review(id)) }
                )
            }
            composable(Routes.REVIEW) { backStackEntry ->
                ReviewFormScreen(
                    placeId = backStackEntry.arguments?.getString("id") ?: "",
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.ROUTE) {
                RouteScreen(
                    onPlaceClick = { id -> navController.navigate(Routes.place(id)) }
                )
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    onPlaceClick = { id -> navController.navigate(Routes.place(id)) }
                )
            }
            composable(Routes.PROFILE) {
                ProfileScreen(onLogout = onLogout)
            }
        }
    }
}