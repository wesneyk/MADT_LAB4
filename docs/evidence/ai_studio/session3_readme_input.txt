Notes Storage Android project: input for writing README.md.
Bundled unchanged from C:\lab_4 (excluded: build outputs, gradlew scripts, gradle-wrapper.jar, local.properties, images).
Each file starts with a line "===== FILE: <path relative to the repository root C:\lab_4> =====".

===== REPOSITORY TREE (files, without build outputs) =====
android-app/app/build.gradle.kts
android-app/app/proguard-rules.pro
android-app/app/src/main/AndroidManifest.xml
android-app/app/src/main/java/com/example/notesstorage/database/AppDatabase.kt
android-app/app/src/main/java/com/example/notesstorage/database/NoteDao.kt
android-app/app/src/main/java/com/example/notesstorage/database/NoteEntity.kt
android-app/app/src/main/java/com/example/notesstorage/MainActivity.kt
android-app/app/src/main/java/com/example/notesstorage/model/Note.kt
android-app/app/src/main/java/com/example/notesstorage/repository/NotesRepository.kt
android-app/app/src/main/java/com/example/notesstorage/storage/JsonNotesStorage.kt
android-app/app/src/main/java/com/example/notesstorage/ui/screens/NoteDetailScreen.kt
android-app/app/src/main/java/com/example/notesstorage/ui/screens/NoteEditScreen.kt
android-app/app/src/main/java/com/example/notesstorage/ui/screens/NotesListScreen.kt
android-app/app/src/main/java/com/example/notesstorage/ui/theme/Theme.kt
android-app/app/src/main/java/com/example/notesstorage/viewmodel/NotesViewModel.kt
android-app/app/src/main/res/values/strings.xml
android-app/app/src/main/res/values-ru/strings.xml
android-app/app/src/test/java/com/example/notesstorage/InMemoryMockNoteDao.kt
android-app/app/src/test/java/com/example/notesstorage/NotesStorageUnitTest.kt
android-app/build.gradle.kts
android-app/gradle.properties
android-app/gradle/libs.versions.toml
android-app/gradle/wrapper/gradle-wrapper.jar
android-app/gradle/wrapper/gradle-wrapper.properties
android-app/gradlew
android-app/gradlew.bat
android-app/local.properties
android-app/settings.gradle.kts
docs/CODE_REVIEW.md
docs/evidence/ai_studio/session2_review_changes.diff
docs/evidence/ai_studio/session2_review_input_sources.txt
docs/evidence/ai_studio/session2_review_response.md
docs/evidence/logs/first_launch_crash_logcat.txt
docs/evidence/screenshots/01_codegen_grill_me.png
docs/evidence/screenshots/02_codegen_plan_architecture.png
docs/evidence/screenshots/03_codegen_plan_decisions.png
docs/evidence/screenshots/04_codegen_generation_started.png
docs/evidence/screenshots/05_codegen_final_result.png
docs/evidence/screenshots/06_codegen_fix_request.png
docs/evidence/screenshots/07_codegen_fix_result.png
docs/evidence/screenshots/10_code_review_request_ai_studio.png
docs/evidence/screenshots/10a_code_review_antigravity_attempt.png
docs/evidence/screenshots/14_code_review_final_result.png
docs/evidence/screenshots/30_app_validation_empty_fields.png
docs/evidence/screenshots/31_app_validation_blank_title.png
docs/evidence/screenshots/32_app_validation_blank_content.png
docs/evidence/screenshots/33_app_create_note_form.png
docs/evidence/screenshots/34_app_create_note_saved_sqlite.png
docs/evidence/screenshots/35_app_view_note_detail.png
docs/evidence/screenshots/36_app_edit_note_form.png
docs/evidence/screenshots/37_app_edit_note_saved_detail.png
docs/evidence/screenshots/38_app_list_after_edit.png
docs/evidence/screenshots/39_app_sync_success.png
docs/evidence/screenshots/40_app_json_tab_after_sync.png
docs/evidence/screenshots/41_app_delete_confirmation.png
docs/evidence/screenshots/42_app_delete_done_sqlite.png
docs/evidence/screenshots/43_app_sync_removes_deleted.png
docs/evidence/screenshots/44_app_json_create.png
docs/evidence/screenshots/45_app_json_edit.png
docs/evidence/screenshots/46_app_storage_isolation_sqlite.png
docs/evidence/screenshots/47_app_json_delete.png
docs/evidence/screenshots/48_app_after_restart_sqlite.png
docs/evidence/screenshots/48b_app_after_restart_json.png
docs/evidence/screenshots/49_app_russian_list.png
docs/evidence/screenshots/50_app_russian_validation.png
docs/evidence/screenshots/51_app_russian_delete_dialog.png
docs/evidence/screenshots/emulator_first_launch_after_codegen.png
docs/evidence/screenshots/emulator_launch_after_session1_fix.png
docs/evidence/TEST_RESULTS.md
docs/evidence/transcripts/session1_code_generation.md
docs/prompts/01_code_generation_prompt.md
docs/prompts/01b_code_generation_fix_prompt.md
docs/prompts/02_code_review_prompt.md
docs/prompts/02b_code_review_prompt_ai_studio.md
docs/prompts/02c_code_review_verification_message.md
docs/WORK_LOG.md

===== FILE: android-app/app/build.gradle.kts =====
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.notesstorage"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.notesstorage"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Jetpack Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // Room
    implementation("androidx.room:room-runtime:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // Testing
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation("org.json:json:20240303")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation("androidx.compose.ui:ui-tooling")
}

===== FILE: android-app/app/proguard-rules.pro =====
# Proguard rules for NotesStorage

===== FILE: android-app/app/src/main/AndroidManifest.xml =====
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:allowBackup="true"
        android:icon="@android:drawable/sym_def_app_icon"
        android:label="@string/app_name"
        android:roundIcon="@android:drawable/sym_def_app_icon"
        android:supportsRtl="true"
        android:theme="@android:style/Theme.Material.Light.NoActionBar">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>

===== FILE: android-app/app/src/main/java/com/example/notesstorage/database/AppDatabase.kt =====
package com.example.notesstorage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notes.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

===== FILE: android-app/app/src/main/java/com/example/notesstorage/database/NoteDao.kt =====
package com.example.notesstorage.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotesFlow(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    suspend fun getAllNotes(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: String): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity): Int

    @Delete
    suspend fun deleteNote(note: NoteEntity): Int

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<NoteEntity>)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(notes: List<NoteEntity>) {
        deleteAll()
        insertAll(notes)
    }
}

===== FILE: android-app/app/src/main/java/com/example/notesstorage/database/NoteEntity.kt =====
package com.example.notesstorage.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesstorage.model.Note

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toDomain(): Note = Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(note: Note): NoteEntity = NoteEntity(
            id = note.id,
            title = note.title,
            content = note.content,
            createdAt = note.createdAt,
            updatedAt = note.updatedAt
        )
    }
}

===== FILE: android-app/app/src/main/java/com/example/notesstorage/MainActivity.kt =====
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

===== FILE: android-app/app/src/main/java/com/example/notesstorage/model/Note.kt =====
package com.example.notesstorage.model

import java.util.UUID

enum class StorageType {
    SQLITE,
    JSON_FILE
}

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

===== FILE: android-app/app/src/main/java/com/example/notesstorage/repository/NotesRepository.kt =====
package com.example.notesstorage.repository

import com.example.notesstorage.database.NoteDao
import com.example.notesstorage.database.NoteEntity
import com.example.notesstorage.model.Note
import com.example.notesstorage.model.StorageType
import com.example.notesstorage.storage.JsonNotesStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface NotesRepository {
    fun getNotesFlow(storageType: StorageType): Flow<List<Note>>
    suspend fun getNotes(storageType: StorageType): List<Note>
    suspend fun getNoteById(id: String, storageType: StorageType): Note?
    suspend fun insertNote(note: Note, storageType: StorageType): Boolean
    suspend fun updateNote(note: Note, storageType: StorageType): Boolean
    suspend fun deleteNote(id: String, storageType: StorageType): Boolean
    suspend fun syncSqliteToJson(): Result<Int>
}

class NotesRepositoryImpl(
    private val noteDao: NoteDao,
    private val jsonStorage: JsonNotesStorage
) : NotesRepository {

    private val jsonNotesStateFlow = MutableStateFlow<List<Note>>(emptyList())

    suspend fun initialize() {
        refreshJsonCache()
    }

    private suspend fun refreshJsonCache() {
        val notes = jsonStorage.readNotes()
        jsonNotesStateFlow.value = notes
    }

    override fun getNotesFlow(storageType: StorageType): Flow<List<Note>> {
        return when (storageType) {
            StorageType.SQLITE -> {
                noteDao.getAllNotesFlow().map { entities ->
                    entities.map { it.toDomain() }
                }
            }
            StorageType.JSON_FILE -> {
                jsonNotesStateFlow.asStateFlow()
            }
        }
    }

    override suspend fun getNotes(storageType: StorageType): List<Note> = withContext(Dispatchers.IO) {
        when (storageType) {
            StorageType.SQLITE -> noteDao.getAllNotes().map { it.toDomain() }
            StorageType.JSON_FILE -> {
                refreshJsonCache()
                jsonNotesStateFlow.value
            }
        }
    }

    override suspend fun getNoteById(id: String, storageType: StorageType): Note? = withContext(Dispatchers.IO) {
        when (storageType) {
            StorageType.SQLITE -> noteDao.getNoteById(id)?.toDomain()
            StorageType.JSON_FILE -> jsonStorage.getNoteById(id)
        }
    }

    override suspend fun insertNote(note: Note, storageType: StorageType): Boolean = withContext(Dispatchers.IO) {
        when (storageType) {
            StorageType.SQLITE -> {
                noteDao.insertNote(NoteEntity.fromDomain(note)) > 0
            }
            StorageType.JSON_FILE -> {
                val ok = jsonStorage.insertNote(note)
                if (ok) refreshJsonCache()
                ok
            }
        }
    }

    override suspend fun updateNote(note: Note, storageType: StorageType): Boolean = withContext(Dispatchers.IO) {
        when (storageType) {
            StorageType.SQLITE -> {
                noteDao.updateNote(NoteEntity.fromDomain(note)) > 0
            }
            StorageType.JSON_FILE -> {
                val ok = jsonStorage.updateNote(note)
                if (ok) refreshJsonCache()
                ok
            }
        }
    }

    override suspend fun deleteNote(id: String, storageType: StorageType): Boolean = withContext(Dispatchers.IO) {
        when (storageType) {
            StorageType.SQLITE -> {
                noteDao.deleteById(id) > 0
            }
            StorageType.JSON_FILE -> {
                val ok = jsonStorage.deleteNote(id)
                if (ok) refreshJsonCache()
                ok
            }
        }
    }

    override suspend fun syncSqliteToJson(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val sqliteNotes = noteDao.getAllNotes().map { it.toDomain() }
            val ok = jsonStorage.writeNotes(sqliteNotes)
            if (ok) {
                refreshJsonCache()
                Result.success(sqliteNotes.size)
            } else {
                Result.failure(Exception("Failed to safely write notes into JSON file"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

===== FILE: android-app/app/src/main/java/com/example/notesstorage/storage/JsonNotesStorage.kt =====
package com.example.notesstorage.storage

import android.content.Context
import com.example.notesstorage.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class JsonNotesStorage(
    private val filesDir: File
) {
    constructor(context: Context) : this(context.filesDir)

    private val mutex = Mutex()
    private val fileName = "notes.json"
    private val targetFile: File
        get() = File(filesDir, fileName)

    suspend fun readNotes(): List<Note> = withContext(Dispatchers.IO) {
        mutex.withLock {
            readNotesInternal()
        }
    }

    suspend fun getNoteById(id: String): Note? = withContext(Dispatchers.IO) {
        readNotes().find { it.id == id }
    }

    suspend fun writeNotes(notes: List<Note>): Boolean = withContext(Dispatchers.IO) {
        mutex.withLock {
            writeNotesInternal(notes)
        }
    }

    suspend fun insertNote(note: Note): Boolean = withContext(Dispatchers.IO) {
        mutex.withLock {
            val currentNotes = readNotesInternal().toMutableList()
            val existingIndex = currentNotes.indexOfFirst { it.id == note.id }
            if (existingIndex >= 0) {
                currentNotes[existingIndex] = note
            } else {
                currentNotes.add(0, note)
            }
            writeNotesInternal(currentNotes)
        }
    }

    suspend fun updateNote(note: Note): Boolean = withContext(Dispatchers.IO) {
        mutex.withLock {
            val currentNotes = readNotesInternal().toMutableList()
            val index = currentNotes.indexOfFirst { it.id == note.id }
            if (index >= 0) {
                currentNotes[index] = note
                writeNotesInternal(currentNotes)
            } else {
                false
            }
        }
    }

    suspend fun deleteNote(id: String): Boolean = withContext(Dispatchers.IO) {
        mutex.withLock {
            val currentNotes = readNotesInternal().toMutableList()
            val removed = currentNotes.removeAll { it.id == id }
            if (removed) {
                writeNotesInternal(currentNotes)
            } else {
                false
            }
        }
    }

    suspend fun clearAll(): Boolean = withContext(Dispatchers.IO) {
        mutex.withLock {
            writeNotesInternal(emptyList())
        }
    }

    private fun readNotesInternal(): List<Note> {
        val file = targetFile
        val backupFile = File(filesDir, "$fileName.bak")

        // Auto-recover from backup file if a crash occurred during atomic rename
        if (!file.exists() && backupFile.exists()) {
            backupFile.renameTo(file)
        }

        if (!file.exists()) return emptyList()

        return try {
            val text = file.readText(Charsets.UTF_8)
            parseJson(text)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun writeNotesInternal(notes: List<Note>): Boolean {
        val jsonArray = JSONArray()
        for (note in notes) {
            val obj = JSONObject().apply {
                put("id", note.id)
                put("title", note.title)
                put("content", note.content)
                put("createdAt", note.createdAt)
                put("updatedAt", note.updatedAt)
            }
            jsonArray.put(obj)
        }
        return safeWrite(jsonArray.toString(2))
    }

    private fun parseJson(jsonString: String): List<Note> {
        val notes = mutableListOf<Note>()
        if (jsonString.isBlank()) return notes
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            notes.add(
                Note(
                    id = obj.getString("id"),
                    title = obj.getString("title"),
                    content = obj.getString("content"),
                    createdAt = obj.getLong("createdAt"),
                    updatedAt = obj.getLong("updatedAt")
                )
            )
        }
        return notes.sortedByDescending { it.updatedAt }
    }

    private fun safeWrite(content: String): Boolean {
        if (!filesDir.exists()) {
            filesDir.mkdirs()
        }

        val tempFile = File(filesDir, "$fileName.tmp")
        val destination = targetFile
        val backupFile = File(filesDir, "$fileName.bak")

        return try {
            FileOutputStream(tempFile).use { fos ->
                fos.write(content.toByteArray(Charsets.UTF_8))
                fos.flush()
                fos.fd.sync()
            }

            if (destination.exists()) {
                if (backupFile.exists()) {
                    backupFile.delete()
                }
                if (!destination.renameTo(backupFile)) {
                    destination.delete()
                }
                if (tempFile.renameTo(destination)) {
                    backupFile.delete()
                    true
                } else {
                    if (backupFile.exists()) {
                        backupFile.renameTo(destination)
                    }
                    false
                }
            } else {
                tempFile.renameTo(destination)
            }
        } catch (t: Throwable) {
            if (tempFile.exists()) {
                tempFile.delete()
            }
            false
        }
    }
}

===== FILE: android-app/app/src/main/java/com/example/notesstorage/ui/screens/NoteDetailScreen.kt =====
package com.example.notesstorage.ui.screens

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.notesstorage.R
import com.example.notesstorage.viewmodel.NotesViewModel
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: String,
    viewModel: NotesViewModel,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        viewModel.loadNoteDetails(noteId)
    }

    val note = uiState.selectedNote

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.screen_title_note_details)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                },
                actions = {
                    if (note != null) {
                        IconButton(onClick = { onEditClick(note.id) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.cd_edit_note)
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.cd_delete_note),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { paddingValues ->
        if (note == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.msg_note_not_found),
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            val dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = stringResource(
                                R.string.label_created_at,
                                dateFormat.format(Date(note.createdAt))
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(
                                R.string.label_updated_at,
                                dateFormat.format(Date(note.updatedAt))
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    if (showDeleteDialog && note != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = stringResource(R.string.dialog_delete_title)) },
            text = { Text(text = stringResource(R.string.dialog_delete_message, note.title)) },
            confirmButton = {
                Button(
                    onClick = {
                        val id = note.id
                        showDeleteDialog = false
                        viewModel.deleteNote(id)
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(text = stringResource(R.string.action_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = stringResource(R.string.action_cancel))
                }
            }
        )
    }
}

===== FILE: android-app/app/src/main/java/com/example/notesstorage/ui/screens/NoteEditScreen.kt =====
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

===== FILE: android-app/app/src/main/java/com/example/notesstorage/ui/screens/NotesListScreen.kt =====
package com.example.notesstorage.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.notesstorage.R
import com.example.notesstorage.model.Note
import com.example.notesstorage.model.StorageType
import com.example.notesstorage.viewmodel.NotesViewModel
import com.example.notesstorage.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    viewModel: NotesViewModel,
    onNoteClick: (String) -> Unit,
    onAddNoteClick: () -> Unit,
    onEditNoteClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val message = if (event.formatArgs.isEmpty()) {
                        context.getString(event.messageResId)
                    } else {
                        context.getString(event.messageResId, *event.formatArgs.toTypedArray())
                    }
                    snackbarHostState.showSnackbar(message)
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_title_notes),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.cd_add_note)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Storage Switcher
            StorageSelector(
                selectedStorage = uiState.currentStorage,
                onStorageSelected = { viewModel.switchStorage(it) }
            )

            // Sync Button
            SyncButtonRow(
                isSyncing = uiState.isSyncing,
                onSyncClick = { viewModel.syncSqliteToJson() }
            )

            // Note List or Empty State
            if (uiState.isLoading && uiState.notes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.notes.isEmpty()) {
                EmptyNotesPlaceholder()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = uiState.notes,
                        key = { it.id }
                    ) { note ->
                        NoteCard(
                            note = note,
                            onClick = { onNoteClick(note.id) },
                            onEditClick = { onEditNoteClick(note.id) },
                            onDeleteClick = { noteToDelete = note }
                        )
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    noteToDelete?.let { note ->
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = { Text(text = stringResource(R.string.dialog_delete_title)) },
            text = {
                Text(
                    text = stringResource(R.string.dialog_delete_message, note.title)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val id = note.id
                        noteToDelete = null
                        viewModel.deleteNote(id)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(text = stringResource(R.string.action_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) {
                    Text(text = stringResource(R.string.action_cancel))
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StorageSelector(
    selectedStorage: StorageType,
    onStorageSelected: (StorageType) -> Unit
) {
    val options = listOf(
        StorageType.SQLITE to stringResource(R.string.storage_sqlite),
        StorageType.JSON_FILE to stringResource(R.string.storage_json)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, (type, label) ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = { onStorageSelected(type) },
                    selected = type == selectedStorage
                ) {
                    Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun SyncButtonRow(
    isSyncing: Boolean,
    onSyncClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onSyncClick,
            enabled = !isSyncing,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSyncing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(18.dp)
                        .width(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = stringResource(R.string.cd_sync),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(text = stringResource(R.string.action_sync))
        }
    }
}

@Composable
private fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.cd_edit_note),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.cd_delete_note),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun EmptyNotesPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.empty_notes_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.empty_notes_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

===== FILE: android-app/app/src/main/java/com/example/notesstorage/ui/theme/Theme.kt =====
package com.example.notesstorage.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme()
private val LightColorScheme = lightColorScheme()

@Composable
fun NotesStorageTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

===== FILE: android-app/app/src/main/java/com/example/notesstorage/viewmodel/NotesViewModel.kt =====
package com.example.notesstorage.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesstorage.R
import com.example.notesstorage.database.AppDatabase
import com.example.notesstorage.model.Note
import com.example.notesstorage.model.StorageType
import com.example.notesstorage.repository.NotesRepository
import com.example.notesstorage.repository.NotesRepositoryImpl
import com.example.notesstorage.storage.JsonNotesStorage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotesUiState(
    val currentStorage: StorageType = StorageType.SQLITE,
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val selectedNote: Note? = null
)

sealed class UiEvent {
    data class ShowSnackbar(val messageResId: Int, val formatArgs: List<Any> = emptyList()) : UiEvent()
    object NavigateBack : UiEvent()
}

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository = NotesRepositoryImpl(
        AppDatabase.getInstance(application).noteDao(),
        JsonNotesStorage(application)
    )

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private var notesObservationJob: Job? = null

    init {
        viewModelScope.launch {
            (repository as? NotesRepositoryImpl)?.initialize()
            loadNotesForCurrentStorage()
        }
    }

    fun switchStorage(storageType: StorageType) {
        if (_uiState.value.currentStorage == storageType) return
        _uiState.update { it.copy(currentStorage = storageType, selectedNote = null) }
        loadNotesForCurrentStorage()
    }

    private fun loadNotesForCurrentStorage() {
        val currentStorage = _uiState.value.currentStorage
        notesObservationJob?.cancel()
        notesObservationJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getNotesFlow(currentStorage).collect { noteList ->
                    _uiState.update { it.copy(notes = noteList, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        R.string.msg_operation_failed,
                        listOf(e.localizedMessage ?: "")
                    )
                )
            }
        }
    }

    fun loadNoteDetails(id: String) {
        viewModelScope.launch {
            val note = repository.getNoteById(id, _uiState.value.currentStorage)
            _uiState.update { it.copy(selectedNote = note) }
        }
    }

    fun saveNote(
        id: String?,
        rawTitle: String,
        rawContent: String,
        onSuccess: () -> Unit,
        onError: (titleError: Boolean, contentError: Boolean) -> Unit
    ) {
        val trimmedTitle = rawTitle.trim()
        val trimmedContent = rawContent.trim()

        val isTitleEmpty = trimmedTitle.isEmpty()
        val isContentEmpty = trimmedContent.isEmpty()

        if (isTitleEmpty || isContentEmpty) {
            onError(isTitleEmpty, isContentEmpty)
            return
        }

        viewModelScope.launch {
            val currentStorage = _uiState.value.currentStorage
            val now = System.currentTimeMillis()
            val noteToSave: Note
            val success = if (id == null) {
                noteToSave = Note(
                    title = trimmedTitle,
                    content = trimmedContent,
                    createdAt = now,
                    updatedAt = now
                )
                repository.insertNote(noteToSave, currentStorage)
            } else {
                val existing = repository.getNoteById(id, currentStorage)
                noteToSave = Note(
                    id = id,
                    title = trimmedTitle,
                    content = trimmedContent,
                    createdAt = existing?.createdAt ?: now,
                    updatedAt = now
                )
                repository.updateNote(noteToSave, currentStorage)
            }

            if (success) {
                _uiState.update { it.copy(selectedNote = noteToSave) }
                val msgResId = if (id == null) R.string.msg_note_created else R.string.msg_note_updated
                _eventFlow.emit(UiEvent.ShowSnackbar(msgResId))
                onSuccess()
            } else {
                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.msg_save_failed))
            }
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            val currentStorage = _uiState.value.currentStorage
            val success = repository.deleteNote(id, currentStorage)
            if (success) {
                _uiState.update { state ->
                    if (state.selectedNote?.id == id) state.copy(selectedNote = null) else state
                }
                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.msg_note_deleted))
            } else {
                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.msg_delete_failed))
            }
        }
    }

    fun syncSqliteToJson() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            val result = repository.syncSqliteToJson()
            _uiState.update { it.copy(isSyncing = false) }

            result.onSuccess { count ->
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        R.string.msg_sync_success,
                        listOf(count)
                    )
                )
            }.onFailure { err ->
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        R.string.msg_sync_failure,
                        listOf(err.localizedMessage ?: "")
                    )
                )
            }
        }
    }
}

===== FILE: android-app/app/src/main/res/values/strings.xml =====
<resources>
    <string name="app_name">Notes Storage</string>
    <string name="screen_title_notes">Notes</string>
    <string name="screen_title_note_details">Note Details</string>
    <string name="screen_title_new_note">New Note</string>
    <string name="screen_title_edit_note">Edit Note</string>

    <!-- Storage Switcher -->
    <string name="storage_sqlite">SQLite (Primary)</string>
    <string name="storage_json">JSON File (Secondary)</string>

    <!-- Buttons &amp; Actions -->
    <string name="action_sync">Sync SQLite → JSON</string>
    <string name="action_add_note">Create Note</string>
    <string name="action_save">Save</string>
    <string name="action_cancel">Cancel</string>
    <string name="action_edit">Edit</string>
    <string name="action_delete">Delete</string>
    <string name="action_confirm">Confirm</string>
    <string name="action_back">Back</string>

    <!-- Fields &amp; Labels -->
    <string name="label_title">Title</string>
    <string name="label_content">Content</string>
    <string name="label_created_at">Created: %1$s</string>
    <string name="label_updated_at">Updated: %1$s</string>

    <!-- Empty States -->
    <string name="empty_notes_title">No notes yet</string>
    <string name="empty_notes_subtitle">Tap the + button below to create your first note.</string>

    <!-- Validation Errors -->
    <string name="error_empty_title">Title cannot be empty</string>
    <string name="error_empty_content">Content cannot be empty</string>
    <string name="error_empty_both">Title and content cannot be empty</string>

    <!-- Delete Confirmation Dialog -->
    <string name="dialog_delete_title">Delete Note?</string>
    <string name="dialog_delete_message">Are you sure you want to delete note \"%1$s\"? This action cannot be undone.</string>

    <!-- Feedback Snackbars -->
    <string name="msg_note_created">Note created successfully</string>
    <string name="msg_note_updated">Note updated successfully</string>
    <string name="msg_note_deleted">Note deleted successfully</string>
    <string name="msg_sync_success">Synced successfully: %1$d notes exported to JSON</string>
    <string name="msg_sync_failure">Sync failed: %1$s</string>
    <string name="msg_storage_switched">Switched to %1$s</string>
    <string name="msg_operation_failed">Operation failed: %1$s</string>
    <string name="msg_note_not_found">Note not found</string>
    <string name="msg_save_failed">Failed to save note</string>
    <string name="msg_delete_failed">Failed to delete note</string>

    <!-- Content Descriptions -->
    <string name="cd_add_note">Add new note</string>
    <string name="cd_delete_note">Delete note</string>
    <string name="cd_edit_note">Edit note</string>
    <string name="cd_back">Navigate back</string>
    <string name="cd_sync">Sync SQLite to JSON file</string>
</resources>

===== FILE: android-app/app/src/main/res/values-ru/strings.xml =====
<resources>
    <string name="app_name">Notes Storage</string>
    <string name="screen_title_notes">Заметки</string>
    <string name="screen_title_note_details">Детали заметки</string>
    <string name="screen_title_new_note">Новая заметка</string>
    <string name="screen_title_edit_note">Редактировать заметку</string>

    <!-- Storage Switcher -->
    <string name="storage_sqlite">SQLite (Основное)</string>
    <string name="storage_json">JSON Файл (Вторичное)</string>

    <!-- Buttons &amp; Actions -->
    <string name="action_sync">Синхронизация SQLite → JSON</string>
    <string name="action_add_note">Создать заметку</string>
    <string name="action_save">Сохранить</string>
    <string name="action_cancel">Отмена</string>
    <string name="action_edit">Редактировать</string>
    <string name="action_delete">Удалить</string>
    <string name="action_confirm">Подтвердить</string>
    <string name="action_back">Назад</string>

    <!-- Fields &amp; Labels -->
    <string name="label_title">Заголовок</string>
    <string name="label_content">Содержимое</string>
    <string name="label_created_at">Создано: %1$s</string>
    <string name="label_updated_at">Обновлено: %1$s</string>

    <!-- Empty States -->
    <string name="empty_notes_title">Заметок пока нет</string>
    <string name="empty_notes_subtitle">Нажмите кнопку +, чтобы добавить свою первую заметку.</string>

    <!-- Validation Errors -->
    <string name="error_empty_title">Заголовок не может быть пустым</string>
    <string name="error_empty_content">Содержимое не может быть пустым</string>
    <string name="error_empty_both">Заголовок и содержимое не могут быть пустыми</string>

    <!-- Delete Confirmation Dialog -->
    <string name="dialog_delete_title">Удалить заметку?</string>
    <string name="dialog_delete_message">Вы действительно хотите удалить заметку \"%1$s\"? Это действие необратимо.</string>

    <!-- Feedback Snackbars -->
    <string name="msg_note_created">Заметка успешно создана</string>
    <string name="msg_note_updated">Заметка успешно обновлена</string>
    <string name="msg_note_deleted">Заметка успешно удалена</string>
    <string name="msg_sync_success">Синхронизация завершена: %1$d заметок сохранено в JSON</string>
    <string name="msg_sync_failure">Ошибка синхронизации: %1$s</string>
    <string name="msg_storage_switched">Переключено на %1$s</string>
    <string name="msg_operation_failed">Ошибка операции: %1$s</string>
    <string name="msg_note_not_found">Заметка не найдена</string>
    <string name="msg_save_failed">Не удалось сохранить заметку</string>
    <string name="msg_delete_failed">Не удалось удалить заметку</string>

    <!-- Content Descriptions -->
    <string name="cd_add_note">Добавить новую заметку</string>
    <string name="cd_delete_note">Удалить заметку</string>
    <string name="cd_edit_note">Редактировать заметку</string>
    <string name="cd_back">Вернуться назад</string>
    <string name="cd_sync">Синхронизировать SQLite с JSON-файлом</string>
</resources>

===== FILE: android-app/app/src/test/java/com/example/notesstorage/InMemoryMockNoteDao.kt =====
package com.example.notesstorage.database

import androidx.room.RoomDatabase
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.notesstorage.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryMockNoteDao : NoteDao {
    private val mutex = Mutex()
    private val notes = mutableMapOf<String, NoteEntity>()
    private val _flow = MutableStateFlow<List<NoteEntity>>(emptyList())

    private fun updateFlow() {
        _flow.value = notes.values.sortedByDescending { it.updatedAt }
    }

    override fun getAllNotesFlow(): Flow<List<NoteEntity>> = _flow.asStateFlow()

    override suspend fun getAllNotes(): List<NoteEntity> = mutex.withLock {
        notes.values.sortedByDescending { it.updatedAt }
    }

    override suspend fun getNoteById(id: String): NoteEntity? = mutex.withLock {
        notes[id]
    }

    override suspend fun insertNote(note: NoteEntity): Long = mutex.withLock {
        notes[note.id] = note
        updateFlow()
        1L
    }

    override suspend fun updateNote(note: NoteEntity): Int = mutex.withLock {
        if (notes.containsKey(note.id)) {
            notes[note.id] = note
            updateFlow()
            1
        } else {
            0
        }
    }

    override suspend fun deleteNote(note: NoteEntity): Int = mutex.withLock {
        val removed = notes.remove(note.id)
        if (removed != null) {
            updateFlow()
            1
        } else {
            0
        }
    }

    override suspend fun deleteById(id: String): Int = mutex.withLock {
        val removed = notes.remove(id)
        if (removed != null) {
            updateFlow()
            1
        } else {
            0
        }
    }

    override suspend fun insertAll(notesList: List<NoteEntity>) = mutex.withLock {
        notesList.forEach { notes[it.id] = it }
        updateFlow()
    }

    override suspend fun deleteAll() = mutex.withLock {
        notes.clear()
        updateFlow()
    }
}

===== FILE: android-app/app/src/test/java/com/example/notesstorage/NotesStorageUnitTest.kt =====
package com.example.notesstorage

import com.example.notesstorage.database.InMemoryMockNoteDao
import com.example.notesstorage.database.NoteDao
import com.example.notesstorage.model.Note
import com.example.notesstorage.model.StorageType
import com.example.notesstorage.repository.NotesRepositoryImpl
import com.example.notesstorage.storage.JsonNotesStorage
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class PureJsonStorageHelper(private val tempDir: File) {
    private val jsonStorage = JsonNotesStorageMock(tempDir)

    fun getStorage() = jsonStorage
}

class JsonNotesStorageMock(private val baseDir: File) {
    private val fileName = "notes.json"
    private val targetFile: File get() = File(baseDir, fileName)

    fun readNotes(): List<Note> {
        if (!targetFile.exists()) return emptyList()
        val text = targetFile.readText(Charsets.UTF_8)
        if (text.isBlank()) return emptyList()
        val array = org.json.JSONArray(text)
        val list = mutableListOf<Note>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                Note(
                    id = obj.getString("id"),
                    title = obj.getString("title"),
                    content = obj.getString("content"),
                    createdAt = obj.getLong("createdAt"),
                    updatedAt = obj.getLong("updatedAt")
                )
            )
        }
        return list.sortedByDescending { it.updatedAt }
    }

    fun writeNotes(notes: List<Note>): Boolean {
        val array = org.json.JSONArray()
        for (n in notes) {
            val obj = org.json.JSONObject().apply {
                put("id", n.id)
                put("title", n.title)
                put("content", n.content)
                put("createdAt", n.createdAt)
                put("updatedAt", n.updatedAt)
            }
            array.put(obj)
        }
        val tmp = File(baseDir, "$fileName.tmp")
        tmp.writeText(array.toString(2), Charsets.UTF_8)
        return tmp.renameTo(targetFile) || {
            tmp.copyTo(targetFile, overwrite = true)
            tmp.delete()
        }()
    }

    fun insertNote(note: Note): Boolean {
        val current = readNotes().toMutableList()
        val idx = current.indexOfFirst { it.id == note.id }
        if (idx >= 0) current[idx] = note else current.add(0, note)
        return writeNotes(current)
    }

    fun updateNote(note: Note): Boolean {
        val current = readNotes().toMutableList()
        val idx = current.indexOfFirst { it.id == note.id }
        if (idx >= 0) {
            current[idx] = note
            return writeNotes(current)
        }
        return false
    }

    fun deleteNote(id: String): Boolean {
        val current = readNotes().toMutableList()
        val removed = current.removeAll { it.id == id }
        return if (removed) writeNotes(current) else false
    }

    fun getNoteById(id: String): Note? = readNotes().find { it.id == id }
}

class NotesStorageUnitTest {

    private lateinit var tempDir: File
    private lateinit var noteDao: NoteDao
    private lateinit var jsonStorage: JsonNotesStorageMock

    @Before
    fun setup() {
        tempDir = File(System.getProperty("java.io.tmpdir"), "test_notes_" + System.currentTimeMillis())
        tempDir.mkdirs()
        noteDao = InMemoryMockNoteDao()
        jsonStorage = JsonNotesStorageMock(tempDir)
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun testRoomCrudOperations() = runBlocking {
        val note = Note(
            id = "test-1",
            title = "SQLite Title",
            content = "SQLite Content",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        // Insert
        val entity = com.example.notesstorage.database.NoteEntity.fromDomain(note)
        val insertId = noteDao.insertNote(entity)
        assertTrue(insertId > 0)

        // Read
        val fetched = noteDao.getNoteById("test-1")?.toDomain()
        assertNotNull(fetched)
        assertEquals("SQLite Title", fetched?.title)
        assertEquals("SQLite Content", fetched?.content)

        // Update
        val updated = note.copy(title = "Updated Title", content = "Updated Content", updatedAt = 3000L)
        val updateCount = noteDao.updateNote(com.example.notesstorage.database.NoteEntity.fromDomain(updated))
        assertTrue(updateCount > 0)

        val fetchedUpdated = noteDao.getNoteById("test-1")?.toDomain()
        assertEquals("Updated Title", fetchedUpdated?.title)

        // Delete
        val deleteCount = noteDao.deleteById("test-1")
        assertTrue(deleteCount > 0)

        val fetchedAfterDelete = noteDao.getNoteById("test-1")
        assertNull(fetchedAfterDelete)
    }

    @Test
    fun testJsonCrudOperations() {
        val note = Note(
            id = "json-1",
            title = "JSON Title",
            content = "JSON Content",
            createdAt = 1000L,
            updatedAt = 2000L
        )

        // Create
        val insertResult = jsonStorage.insertNote(note)
        assertTrue(insertResult)

        // Read
        val fetched = jsonStorage.getNoteById("json-1")
        assertNotNull(fetched)
        assertEquals("JSON Title", fetched?.title)
        assertEquals("JSON Content", fetched?.content)

        // Update
        val updated = note.copy(title = "JSON Updated Title", content = "JSON Updated Content", updatedAt = 4000L)
        val updateResult = jsonStorage.updateNote(updated)
        assertTrue(updateResult)

        val fetchedUpdated = jsonStorage.getNoteById("json-1")
        assertEquals("JSON Updated Title", fetchedUpdated?.title)

        // Delete
        val deleteResult = jsonStorage.deleteNote("json-1")
        assertTrue(deleteResult)

        val fetchedAfterDelete = jsonStorage.getNoteById("json-1")
        assertNull(fetchedAfterDelete)
    }

    @Test
    fun testOneWaySyncSqliteToJson() = runBlocking {
        // Prepare notes in SQLite
        val note1 = Note(id = "sync-1", title = "Note 1", content = "Content 1", createdAt = 100L, updatedAt = 100L)
        val note2 = Note(id = "sync-2", title = "Note 2", content = "Content 2", createdAt = 200L, updatedAt = 200L)
        noteDao.insertNote(com.example.notesstorage.database.NoteEntity.fromDomain(note1))
        noteDao.insertNote(com.example.notesstorage.database.NoteEntity.fromDomain(note2))

        // Put an old note into JSON that doesn't exist in SQLite
        val staleNote = Note(id = "stale-1", title = "Stale", content = "Stale content", createdAt = 50L, updatedAt = 50L)
        jsonStorage.insertNote(staleNote)

        // Perform Sync logic: read SQLite, overwrite JSON safely
        val sqliteNotes = noteDao.getAllNotes().map { it.toDomain() }
        val syncOk = jsonStorage.writeNotes(sqliteNotes)
        assertTrue(syncOk)

        // Verify JSON now mirrors SQLite exactly
        val jsonNotes = jsonStorage.readNotes()
        assertEquals(2, jsonNotes.size)
        assertTrue(jsonNotes.any { it.id == "sync-1" })
        assertTrue(jsonNotes.any { it.id == "sync-2" })
        assertFalse(jsonNotes.any { it.id == "stale-1" }) // Stale note removed
    }

    @Test
    fun testValidationLogic() {
        val emptyTitle = "   "
        val validTitle = "My Note"
        val emptyContent = ""
        val validContent = "Valid content"

        assertTrue(emptyTitle.trim().isEmpty())
        assertFalse(validTitle.trim().isEmpty())
        assertTrue(emptyContent.trim().isEmpty())
        assertFalse(validContent.trim().isEmpty())
    }
}

===== FILE: android-app/build.gradle.kts =====
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.ksp) apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10" apply false
}

===== FILE: android-app/gradle.properties =====
org.gradle.java.home=C:\\Program Files\\Android\\Android Studio\\jbr
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.disallowKotlinSourceSets=false

===== FILE: android-app/gradle/libs.versions.toml =====
[versions]
agp = "9.4.1"
ksp = "2.2.10-2.0.2"
coreKtx = "1.19.1"
junit = "4.13.2"
junitVersion = "1.3.0"
espressoCore = "3.7.0"
appcompat = "1.8.0"
material = "1.14.0"
activityKtx = "1.13.0"
constraintlayout = "2.2.2"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
androidx-activity-ktx = { group = "androidx.activity", name = "activity-ktx", version.ref = "activityKtx" }
androidx-constraintlayout = { group = "androidx.constraintlayout", name = "constraintlayout", version.ref = "constraintlayout" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }

===== FILE: android-app/gradle/wrapper/gradle-wrapper.properties =====
#Thu Oct 08 17:03:43 EEST 2026
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionSha256Sum=bbaeb2fef8710818cf0e261201dab964c572f92b942812df0c3620d62a529a01
distributionUrl=https\://services.gradle.org/distributions/gradle-9.6.0-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists

===== FILE: android-app/settings.gradle.kts =====
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NotesStorage"
include(":app")

===== FILE: docs/CODE_REVIEW.md =====
# Code Review Report: Notes Storage Android App

## 1. Review Overview
This review evaluated the complete architecture, source code, data layers, coroutines/flows, Jetpack Compose UI, and localization of the Notes Storage application.

### Key Strengths:
- Clean MVVM pattern with separation into database, model, repository, storage, UI, and ViewModel layers.
- Full Jetpack Compose Material 3 implementation supporting dynamic color and dark/light themes.
- Proper input validation preventing empty or whitespace-only note titles and content.
- Complete localization resources in English (`values`) and Russian (`values-ru`).
- Thread confinement using Kotlin Coroutines and Room `@Transaction` support.

---

## 2. Findings & Severity Ranking

| Rank | Severity | Location | Issue Description |
| :--- | :--- | :--- | :--- |
| **1** | **CRITICAL** | `JsonNotesStorage.kt` | `NoClassDefFoundError` on Android API 24/25 due to `java.nio.file.Files.move` usage with `minSdk = 24`. Causes fatal crash on Android 7.x. |
| **2** | **CRITICAL** | `NotesViewModel.kt` | Flow collection coroutine leak when switching storages. SQLite Room flow continues running in background and overwrites JSON state. |
| **3** | **HIGH** | `NotesViewModel.kt`, `NoteDetailScreen.kt` | Stale note displayed in `NoteDetailScreen` after editing because `selectedNote` was not updated in ViewModel on save. |
| **4** | **HIGH** | `NotesListScreen.kt` | Destructive "Sync SQLite → JSON" action lacks a confirmation dialog, risking immediate loss of JSON-only notes. |
| **5** | **MEDIUM** | `JsonNotesStorage.kt` | Tight coupling to Android `Context` prevented testing the production class in JVM unit tests, resulting in a duplicate mock. |
| **6** | **MEDIUM** | `NotesViewModel.kt` | Leaky abstraction downcasting `(repository as? NotesRepositoryImpl)?.initialize()`, and hardcoded instantiation without DI. |
| **7** | **LOW** | `NotesViewModel.kt` | Redundant manual list re-fetching calls after JSON mutations while flow observation was already in place. |
| **8** | **LOW** | `res/values/strings.xml` | Unused resource strings (`msg_storage_switched`, `error_empty_both`). |

---

## 3. Prioritized Recommendations
1. **[P1] Replace `java.nio.file.Files`** with an API 24-compatible atomic write mechanism (`.tmp`, `fd.sync()`, `.bak`, and `renameTo`) and support automatic `.bak` recovery.
2. **[P2] Fix Flow observation lifecycle in `NotesViewModel`** by tracking and canceling previous collection jobs on storage switch, and properly synchronizing `selectedNote`.
3. **[P3] Add confirmation dialog for SQLite → JSON sync** in `NotesListScreen` to safeguard secondary storage data.
4. **[P4] Directly unit test `JsonNotesStorage`** by providing a `filesDir: File` constructor.
5. **[P5] Clean up repository initialization and ViewModel dependencies** via a proper Factory pattern or DI.

---

## 4. Implemented Improvements

### Improvement 1: API 24/25 Crash Fix & Resilient Atomic File Storage (`JsonNotesStorage.kt`)
- **What was changed:** Replaced `java.nio.file.Files.move` with standard file-system atomic write: writes to `$fileName.tmp`, calls `fos.fd.sync()`, backs up the existing file to `$fileName.bak`, and executes atomic `renameTo`. If a crash occurs mid-write, `readNotesInternal` automatically restores the backup on next launch. Added secondary constructor accepting `File` directory for test decoupling.
- **Why:** Eliminates the fatal `NoClassDefFoundError` on Android 7.0/7.1 devices, guarantees data durability across unexpected process terminations, and enables direct unit testing without Android mocks.

### Improvement 2: Coroutine Flow Leak & Stale State Fix (`NotesViewModel.kt`)
- **What was changed:** 
  1. Maintained a `notesObservationJob: Job?` reference and cancelled any active job before launching a new flow collector in `loadNotesForCurrentStorage()`.
  2. Updated `selectedNote` directly in `saveNote()` with the saved note entity.
  3. Cleared `selectedNote` when switching storage modes or deleting the active note.
  4. Removed redundant imperative JSON list reloads.
- **Why:** Prevents Room SQLite flow emissions from polluting the JSON storage view, eliminates coroutine leaks, and ensures `NoteDetailScreen` immediately displays updated content and timestamps upon returning from `NoteEditScreen`.

---

## 5. Verification Plan
1. **Build:** Execute `./gradlew clean assembleDebug` to confirm clean compilation.
2. **Unit Tests:** Execute `./gradlew testDebugUnitTest` to verify repository, Room DAO, and JSON storage logic pass.
3. **Functional Checks:**
   - Verify CRUD operations in both SQLite and JSON modes.
   - Switch between storages multiple times to confirm strict data separation.
   - Edit an existing note, save, and confirm `NoteDetailScreen` immediately reflects the new title and content.
   - Test one-way synchronization from SQLite to JSON and verify both storages match.
   - Verify validation errors when submitting empty fields.
   - Switch device language to Russian and verify full UI localization.

===== FILE: docs/evidence/TEST_RESULTS.md =====
# Test results: Notes Storage (after the Session 2 code review changes)

Run by the Claude Code agent on 2026-11-05, 21:18–21:30 (UTC+02:00). The agent did not change any source code.

- Code under test: `android-app` after Session 1 (Antigravity) and the two Session 2 changes (Google AI Studio), see `docs/evidence/ai_studio/session2_review_changes.diff`.
- Build: Gradle wrapper from the command line with JAVA_HOME = Android Studio JBR (`gradlew clean assembleDebug testDebugUnitTest`). The build in the Android Studio UI is checked separately by the user.
- Device: Android Emulator AVD "Pixel_6", sdk_gphone64_x86_64, Android 14 (API 34).
- Method: the app was driven with `adb shell input` and `uiautomator dump`. Every "Actual result" comes from the screen text or `run-as … cat files/notes.json`. Screenshots are real `adb screencap` captures in `docs/evidence/screenshots/`.

| # | Test | Expected result | Actual result | Status | Evidence |
|---|------|-----------------|---------------|--------|----------|
| 1 | Gradle build | `assembleDebug` succeeds | BUILD SUCCESSFUL in 38s, 1 compiler warning in test code (parameter name in InMemoryMockNoteDao) | PASS | build output in docs/WORK_LOG.md |
| 2 | Unit tests | All tests pass | `NotesStorageUnitTest`: 4 tests, 0 failures, 0 errors. Note: the Room and JSON tests run against mock classes, not the real Room database or the real `JsonNotesStorage` (review finding 5) | PASS | app/build/test-results |
| 3 | App launch | Main screen without crash | "Notes" title, SQLite/JSON switch, "Sync SQLite → JSON" button, FAB; crash buffer empty | PASS | emulator_launch_after_session1_fix.png |
| 4 | Default storage | SQLite selected on start | "SQLite (Primary)" selected after every launch | PASS | 48_app_after_restart_sqlite.png |
| 5 | Validation: title and content empty | Saving blocked, error messages shown | "Title cannot be empty", "Content cannot be empty"; stayed on "New Note" | PASS | 30_app_validation_empty_fields.png |
| 6 | Validation: title of spaces only | Saving blocked (trim) | "Title cannot be empty" with title "   " and content "Some content" | PASS | 31_app_validation_blank_title.png |
| 7 | Validation: content of spaces only | Saving blocked (trim) | "Content cannot be empty" with title "Test Note" and content "  " | PASS | 32_app_validation_blank_content.png |
| 8 | Test 1, Create (SQLite) | Note "Test Note" / "This is a test note." saved and listed | Note appears at the top of the SQLite list | PASS | 33_app_create_note_form.png, 34_app_create_note_saved_sqlite.png |
| 9 | Test 2, Read | Note listed; opening it shows title and content | Detail screen "Note Details": title, Created/Updated time, content | PASS | 35_app_view_note_detail.png |
| 10 | Test 3, Update | Title and content changed and saved | Detail screen immediately shows "Test Note Edited" / "This is a test note. The content was changed.", Updated time changed; list updated | PASS | 36_app_edit_note_form.png, 37_app_edit_note_saved_detail.png, 38_app_list_after_edit.png |
| 11 | Test 4, Delete (SQLite) | Confirmation dialog; note disappears | Dialog "Delete Note?" with Cancel/Delete; after Delete, "Meeting Notes" gone, snackbar "Note deleted successfully" | PASS | 41_app_delete_confirmation.png, 42_app_delete_done_sqlite.png |
| 12 | Test 5, SQLite CRUD | Create, read, update and delete work with SQLite selected | Tests 8–11 were all run with SQLite selected | PASS | 33–42 |
| 13 | Test 6, JSON CRUD | Create, read, update and delete work with JSON File selected, and `notes.json` changes accordingly | Created "JSON Note" (file: 2 notes), edited to "JSON Note Edited" (file updated), deleted (file: 1 note) | PASS | 44_app_json_create.png, 45_app_json_edit.png, 47_app_json_delete.png |
| 14 | Test 7, Synchronization | SQLite notes copied to JSON with the same UUIDs; result shown | Snackbar "Synced successfully: 2 notes exported to JSON"; `notes.json` held "Test Note Edited" [b1f46060…] and "Meeting Notes" [8cc4c9c6…], same ids as SQLite; JSON tab shows both | PASS | 39_app_sync_success.png, 40_app_json_tab_after_sync.png |
| 15 | Repeated sync | No duplicates | Second sync: `notes.json` still 2 notes | PASS | docs/WORK_LOG.md |
| 16 | Deletion in SQLite, then sync | Deleted note removed from JSON | Before sync: 2 notes in JSON; after sync: 1 ("Test Note Edited") | PASS | 43_app_sync_removes_deleted.png |
| 17 | Storage isolation (review fix 2) | JSON-only note never shown in the SQLite list, also after repeated switching | 5 SQLite↔JSON toggles: JSON list = 2 notes, SQLite list = 1 note every time | PASS | 46_app_storage_isolation_sqlite.png |
| 18 | Persistence after restart | Notes remain after the app process is restarted | `am force-stop` + relaunch: SQLite and JSON lists unchanged | PASS | 48_app_after_restart_sqlite.png, 48b_app_after_restart_json.png |
| 19 | English text | All UI text in English by default | List, edit, detail, dialog and snackbar texts in English | PASS | 34–47 |
| 20 | Russian localization | All UI text in Russian | App locale ru-RU (`cmd locale set-app-locales`): "Заметки", "SQLite (Основное)", "JSON Файл (Вторичное)", "Синхронизация SQLite → JSON", validation errors, delete dialog and content descriptions in Russian; note texts stay as typed | PASS | 49_app_russian_list.png, 50_app_russian_validation.png, 51_app_russian_delete_dialog.png |
| 21 | Review fix 1 on Android 7.0/7.1 (API 24/25) | No crash when writing `notes.json` | Not run: no API 24/25 system image is installed (available: API 30, 33, 34, 37.2). Writing `notes.json` works on API 34 (tests 13–16) | NOT RUN | – |

Not covered here: the build and run inside the Android Studio UI (to be done and screenshotted by the user), and instrumented UI tests (the project has none).

