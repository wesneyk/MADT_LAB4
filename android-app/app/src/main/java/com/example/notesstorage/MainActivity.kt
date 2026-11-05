package com.example.notesstorage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notesstorage.ui.screens.NoteDetailScreen
import com.example.notesstorage.ui.screens.NoteEditScreen
import com.example.notesstorage.ui.screens.NotesListScreen
import com.example.notesstorage.ui.theme.NotesStorageTheme
import com.example.notesstorage.viewmodel.NotesViewModel

sealed class Screen(val route: String) {
    object NotesList : Screen("notes_list")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: String) = "note_detail/$noteId"
    }
    object NoteAdd : Screen("note_add")
    object NoteEdit : Screen("note_edit/{noteId}") {
        fun createRoute(noteId: String) = "note_edit/$noteId"
    }
}

class MainActivity : ComponentActivity() {

    private val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesStorageTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotesAppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun NotesAppNavigation(viewModel: NotesViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.NotesList.route
    ) {
        composable(Screen.NotesList.route) {
            NotesListScreen(
                viewModel = viewModel,
                onNoteClick = { noteId ->
                    navController.navigate(Screen.NoteDetail.createRoute(noteId))
                },
                onAddNoteClick = {
                    navController.navigate(Screen.NoteAdd.route)
                },
                onEditNoteClick = { noteId ->
                    navController.navigate(Screen.NoteEdit.createRoute(noteId))
                }
            )
        }

        composable(
            route = Screen.NoteDetail.route,
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId").orEmpty()
            NoteDetailScreen(
                noteId = noteId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate(Screen.NoteEdit.createRoute(id))
                }
            )
        }

        composable(Screen.NoteAdd.route) {
            NoteEditScreen(
                noteId = null,
                viewModel = viewModel,
                onSaveSuccess = { navController.popBackStack() },
                onCancelClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.NoteEdit.route,
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            NoteEditScreen(
                noteId = noteId,
                viewModel = viewModel,
                onSaveSuccess = { navController.popBackStack() },
                onCancelClick = { navController.popBackStack() }
            )
        }
    }
}
