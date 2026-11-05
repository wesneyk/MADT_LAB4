package com.example.notesstorage.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notesstorage.R
import com.example.notesstorage.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    noteId: String?,
    viewModel: NotesViewModel,
    onSaveSuccess: () -> Unit,
    onCancelClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    var titleError by remember { mutableStateOf(false) }
    var contentError by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        if (noteId != null) {
            viewModel.loadNoteDetails(noteId)
        }
    }

    LaunchedEffect(uiState.selectedNote) {
        val note = uiState.selectedNote
        if (noteId != null && note != null && note.id == noteId) {
            title = note.title
            content = note.content
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (noteId == null) {
                            stringResource(R.string.screen_title_new_note)
                        } else {
                            stringResource(R.string.screen_title_edit_note)
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancelClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (titleError && it.trim().isNotEmpty()) {
                        titleError = false
                    }
                },
                label = { Text(text = stringResource(R.string.label_title)) },
                isError = titleError,
                supportingText = {
                    if (titleError) {
                        Text(
                            text = stringResource(R.string.error_empty_title),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = {
                    content = it
                    if (contentError && it.trim().isNotEmpty()) {
                        contentError = false
                    }
                },
                label = { Text(text = stringResource(R.string.label_content)) },
                isError = contentError,
                supportingText = {
                    if (contentError) {
                        Text(
                            text = stringResource(R.string.error_empty_content),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                maxLines = 15
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.action_cancel))
                }

                Button(
                    onClick = {
                        viewModel.saveNote(
                            id = noteId,
                            rawTitle = title,
                            rawContent = content,
                            onSuccess = onSaveSuccess,
                            onError = { isTitleEmpty, isContentEmpty ->
                                titleError = isTitleEmpty
                                contentError = isContentEmpty
                            }
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.action_save))
                }
            }
        }
    }
}
