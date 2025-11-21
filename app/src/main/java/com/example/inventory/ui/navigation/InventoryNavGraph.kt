package com.example.inventory.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier 
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.ui.item.NoteEditScreen
import com.example.inventory.ui.item.NoteEntryScreen
import com.example.inventory.ui.item.NoteDetailsScreen
import com.example.inventory.ui.notes.NotesScreen
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

@Composable
fun InventoryNavHost(
    navController: NavHostController,
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier,
    startDestination: String = "notes",
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("notes") {
            val width = windowSize.widthSizeClass   

            if (width == WindowWidthSizeClass.Compact) {
                // Entra primero a e telefono
                NotesScreen(
                    navigateToNoteEntry = { navController.navigate("note_entry") },
                    navigateToNoteDetail = { id -> navController.navigate("note_details/$id") }
                )
            } else {
// aqui ya es para la tableta
                Row(Modifier.fillMaxSize().padding(16.dp)) {
                    //Para la lista
                    Box(Modifier.weight(0.40f)) {
                        NotesScreen(
                            navigateToNoteEntry = { navController.navigate("note_entry") },
                            navigateToNoteDetail = { id ->
                                //   el ViewModel reciba el noteId
                                navController.navigate("note_details/$id")
                            }
                        )
                    }
                    Spacer(Modifier.width(16.dp))

                    Box(Modifier.weight(0.60f)) {
                        androidx.compose.material3.Text("Selecciona una nota para ver el detalle")
                    }
                }
            }
        }


// aqui crea la nota
        composable("note_entry") {
            NoteEntryScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "note_details/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
            NoteDetailsScreen(
                noteId = noteId,
                navigateBack = { navController.popBackStack() },
                navigateToEditNote = { id -> navController.navigate("edit_note/$id") }
            )
        }

        composable(
            route = "edit_note/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
            NoteEditScreen(
                noteId = noteId,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
