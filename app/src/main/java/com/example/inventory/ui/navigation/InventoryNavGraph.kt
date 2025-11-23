package com.example.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.ui.item.NoteDetailsScreen
import com.example.inventory.ui.item.NoteEntryScreen
import com.example.inventory.ui.item.NoteEditScreen
import com.example.inventory.ui.notes.NotesScreen
import com.example.inventory.ui.reminders.ReminderListScreen

@Composable
fun InventoryNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = "notes_list",
        modifier = modifier
    ) {

        // --------------------------------------------------
        // LISTA DE NOTAS
        // --------------------------------------------------
        composable("notes_list") {
            NotesScreen(
                navigateToNoteEntry = { navController.navigate("note_entry") },
                navigateToNoteDetails = { noteId, title ->
                    navController.navigate("note_details/$noteId/$title")
                }
            )
        }

        // --------------------------------------------------
        // CREAR NOTA
        // --------------------------------------------------
        composable("note_entry") {
            NoteEntryScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        // --------------------------------------------------
        // EDITAR NOTA
        // --------------------------------------------------
        composable(
            route = "note_edit/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments!!.getInt("noteId")

            NoteEditScreen(
                noteId = noteId,
                navigateBack = { navController.popBackStack() }
            )
        }

        // --------------------------------------------------
        // DETALLES DE NOTA
        // --------------------------------------------------
        composable(
            route = "note_details/{noteId}/{title}",
            arguments = listOf(
                navArgument("noteId") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val noteId = backStackEntry.arguments!!.getInt("noteId")
            val title = backStackEntry.arguments!!.getString("title")!!

            NoteDetailsScreen(
                noteId = noteId,
                navigateBack = { navController.popBackStack() },
                navigateToEditNote = { navController.navigate("note_edit/$noteId") },
                navigateToReminderList = { id, noteTitle ->
                    navController.navigate("reminder_list/$id/$noteTitle")
                }
            )
        }

        // --------------------------------------------------
        // LISTA DE RECORDATORIOS (NUEVO)
        // --------------------------------------------------
        composable(
            route = "reminder_list/{noteId}/{noteTitle}",
            arguments = listOf(
                navArgument("noteId") { type = NavType.IntType },
                navArgument("noteTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val noteId = backStackEntry.arguments!!.getInt("noteId")
            val noteTitle = backStackEntry.arguments!!.getString("noteTitle")!!

            ReminderListScreen(
                noteId = noteId,
                noteTitle = noteTitle,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
