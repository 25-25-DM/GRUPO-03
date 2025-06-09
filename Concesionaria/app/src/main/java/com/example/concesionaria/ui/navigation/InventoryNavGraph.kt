/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.concesionaria.AppViewModelProvider
import com.example.concesionaria.screens.LoginDestination
import com.example.concesionaria.screens.LoginScreen
import com.example.concesionaria.screens.RegisterScreen
import com.example.concesionaria.ui.item.ItemEntryDestination
import com.example.concesionaria.ui.item.ItemEntryScreen

import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.home.HomeScreen
import com.example.inventory.ui.item.ItemDetailsDestination
import com.example.inventory.ui.item.ItemDetailsScreen
import com.example.inventory.ui.item.ItemEditDestination
import com.example.inventory.ui.item.ItemEditScreen

import com.example.inventory.ui.register.RegisterDestination

import com.example.inventory.ui.summary.InventorySummaryDestination
import com.example.inventory.ui.summary.InventorySummaryScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun InventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
      /*  composable(route = LoginDestination.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(HomeDestination.route) }
            )
        }*/

        composable(route = RegisterDestination.route) {
            RegisterScreen(
                viewModel = viewModel(factory = AppViewModelProvider.Factory),
                onRegisterSuccess = { navController.navigate(HomeDestination.route) },
                onGoToLogin = { navController.navigate(LoginDestination.route) }
            )
        }

        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                navigateToItemUpdate = { navController.navigate("${ItemDetailsDestination.route}/${it}") },
                navigateToSummary = { navController.navigate(InventorySummaryDestination.route) }
            )
        }

        composable(route = InventorySummaryDestination.route) {
            InventorySummaryScreen(
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}