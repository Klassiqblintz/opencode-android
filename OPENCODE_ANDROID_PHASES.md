# OPENCODE ANDROID — Phase-by-Feature Spec

---

## PHASE 1 — Project Setup, Dependencies, Manifest

**Create an Android project with these specs:**

- Kotlin, Jetpack Compose, Material 3
- Package: `com.opencode`, app name: "OpenCode Android"
- Min SDK 26, target SDK 34, compile SDK 35
- Compose compiler 1.5.14

**Dependencies to add:**
- androidx.core:core-ktx:1.12.0
- androidx.appcompat:appcompat:1.6.1
- com.google.android.material:material:1.11.0
- androidx.compose.material3:material3:1.2.0
- androidx.compose.material:material-icons-extended:1.6.0
- platform("androidx.compose:compose-bom:2023.10.01")
- androidx.compose.ui:ui, ui-graphics, ui-tooling-preview, foundation, animation, runtime-livedata
- debugImplementation androidx.compose.ui:ui-tooling
- androidx.navigation:navigation-compose:2.7.7
- androidx.activity:activity-compose:1.8.2
- androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0
- androidx.lifecycle:lifecycle-runtime-compose:2.7.0
- androidx.lifecycle:lifecycle-runtime-ktx:2.7.0
- androidx.room:room-runtime:2.7.0
- androidx.room:room-ktx:2.7.0
- ksp("androidx.room:room-compiler:2.7.0")
- org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3
- com.squareup.okhttp3:okhttp:4.12.0
- io.coil-kt:coil-compose:2.5.0
- androidx.biometric:biometric:1.1.0
- androidx.webkit:webkit:1.9.0

**AndroidManifest permissions:**
- INTERNET, ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE
- READ_EXTERNAL_STORAGE (maxSdk 32), WRITE_EXTERNAL_STORAGE (maxSdk 32), MANAGE_EXTERNAL_STORAGE
- USE_BIOMETRIC, USE_FINGERPRINT, RECORD_AUDIO
- POST_NOTIFICATIONS, FOREGROUND_SERVICE, FOREGROUND_SERVICE_SPECIAL_USE

**App component (com.opencode.app.App):**
- Class extending Application
- In onCreate: create 2 notification channels — "Engine Running" (persistent) and "Coding Events" (task notifications)

**Service (com.opencode.engine.AgentEngineService):**
- Extends Service, exported=false, foregroundServiceType=specialUse
- Starts with START_STICKY so it restarts if killed
- Has 2 notification channels: "Engine Running" (persistent notification) and "Coding Events" (task notifications)

**Theme:**
- Light theme as default (Material.Light.NoActionBar)
- Dark theme as alternate style
- Transparent status bar, transparent nav bar, fitsSystemWindows=false

**Colors defined in colors.xml:**
- Primary blue #1E88E5, secondary gray, tertiary purple, error red
- Dark theme equivalents for each
- 5 per-agent accent colors: OpenCode #1E88E5, DeepSeek #7C4DFF, Claude #7E57C2, Pi #00BFA5, Custom #607D8B
- Terminal colors: bg #1E1E1E, text #F8F8F2, prompt #448AFF

**Icon:**
- xhdpi mipmap only (96x96 PNGs)
- ic_launcher.png and ic_launcher_round.png — blue background with white circle

**Strings:**
- All navigation labels (Home, Sessions, Projects, Builds, Settings)
- All screen titles, button labels, placeholders
- Agent descriptions, permission names, model config labels
- Notification channel names, build messages, import messages

---

## PHASE 2 — Engine Layer (Types, Configs, Service)

**Create these files under `com.opencode.engine`:**

**AgentRunMode enum:**
- ON_DEVICE (pRoot Linux on device)
- REMOTE_SERVER (HTTP/WebSocket connection)

**ModelSource enum:**
- CLOUD_API (remote LLM provider)
- LOCAL_GGUF (llama.cpp local model)
- HYBRID (both, auto-switch)

**AgentType enum** with each value having properties: id (String), displayName (String), accentColor (Long as ARGB), description (String), boolean flags: supportsLocal, supportsRemote, supportsGguf, supportsCloudApi, supportsVision, supportsReasoning, supportsToolCalls.

5 entries:
- OPENCODE: id="opencode", blue accent 0xFF1E88E5, all flags true
- DEEPSEEK: id="deepseek", purple accent 0xFF7C4DFF, all flags true
- CLAUDE: id="claude", deep purple 0xFF7E57C2, all flags true
- PI: id="pi", teal 0xFF00BFA5, most flags true
- CUSTOM: id="custom", blue-gray 0xFF607D8B, all flags true

**AgentConfig data class** fields: agentType, runMode, rootfsType, rootfsPath, linuxPackages (List, default empty), ggufFilePath (File), ggufFileName (String), ggufQuantization ("Q4_K_M"), ggufContextLength (32768), ggufThreads (4), ggufGpuLayers (0), apiKey, endpointUrl, headers (Map, default empty), temperature (0.7f), topP (0.95f), maxTokens (4096), maxContextTokens (128000), enableReasoning (true), enableToolCalls (true), enableVision (false), modelSource (HYBRID), projectWorkspacePath

**ModelConfig data class** fields: agentType (AgentType), cloudProvider, cloudModelName, apiKey, endpointUrl, headers (Map, default empty), ggufFilePath (File?), ggufFileName (String?), ggufFileSizeBytes (0), ggufQuantization (""), ggufContextLength (32768), ggufParameters (0), ggufThreads (4), ggufGpuLayers (0), temperature (0.7f), topP (0.95f), maxTokens (4096), maxContextTokens (128000), enableReasoning (true), enableToolCalls (true), enableVision (false), modelSource, projectWorkspacePath

**DefaultModelConfigs object** functions:
- defaultOpenCodeConfig() → ModelConfig with anthropic/claude-sonnet-5-5
- defaultDeepSeekConfig() → ModelConfig with deepseek/deepseek-coder
- defaultClaudeConfig() → ModelConfig with anthropic/claude-3.5-sonnet
- defaultPiConfig() → ModelConfig with pi/pi-1
- defaultCustomConfig() → ModelConfig with user-defined values

**BridgeCommand sealed class** variants:
- Ping (health check)
- Shot(path: String?) — screenshot
- Dump — accessibility tree
- Tap(x: Int, y: Int)
- Swipe(startX, startY, endX, endY, durationMs: 300)
- Text(text: String)
- Key(key: SystemKey enum) — BACK, HOME, RECENT, VOLUME_UP/DOWN, POWER, ENTER, BACKSPACE, TAB, ESCAPE
- Open(packageName: String)

**AgentEngineService:**
- Extends Service with Binder
- Constants: TAG, NOTIFICATION_ID=1001, CHANNEL_ID="opencode_engine_channel"
- Actions: START_ENGINE, STOP_ENGINE, PAUSE_ENGINE, RESUME_ENGINE, NOTIFY_PERMISSION, NOTIFY_TASK_COMPLETE
- onCreate: createNotificationChannels() (creates both channels)
- onBind: return EngineBinder
- onStartCommand: switch on action — START_ENGINE creates foreground notification, STOP_ENGINE cancels it, PAUSE/RESUME update notification text, NOTIFY_PERMISSION/NOTIFY_TASK_COMPLETE post task notifications
- onDestroy: cancel notification

---

## PHASE 3 — Storage Layer (Entities, DAOs, Database)

**Create Room database with 6 entities and 6 DAOs. ALL ENUM FIELDS ARE `String` TYPE (NOT actual enum types) — critical for mobile IDEs.**

**Entities (com.opencode.storage.entities):**

1. **ProjectEntity** @Entity("projects") @PrimaryKey(id=0), name, path, projectType (String: "FLUTTER"/"KOTLIN_COMPOSE"/"JAVA_GRADLE"/"NATIVE_C_CPP"/"MIXED"), lastUsed, lastModified, createdAt, isFavorite (false), description

2. **SessionEntity** @Entity("sessions") @PrimaryKey(id=0), projectId, title, status (String: "IDLE"/"RUNNING"/"THINKING"/"WAITING_INPUT"/"WAITING_PERMISSION"/"COMPLETED"/"ERRORED"/"PAUSED"), messageCount (0), lastMessageAt, createdAt

3. **MessageEntity** @Entity("messages") @PrimaryKey(id=0), sessionId, role (String: "USER"/"AGENT"/"TOOL_CALL"/"TOOL_RESULT"/"ERROR"/"PERMISSION"), content, status, createdAt

4. **UploadedFileEntity** @Entity("uploaded_files") @PrimaryKey(id=0), projectId, fileName, filePath, mimeType, fileSize, category (String: "CODE"/"IMAGE"/"VIDEO"/"AUDIO"/"DOCUMENT"/"ARCHIVE"/"BINARY"/"MODEL"/"CONFIG"/"OTHER"), uploadTimestamp

5. **ModelEntity** @Entity("models") @PrimaryKey(id=0), agentType (String), cloudProvider, cloudModelName, apiKeyHash, endpointUrl, ggufFilePath, ggufFileName, temperature (0.7f), maxTokens (4096), maxContextTokens (128000), createdAt

6. **McpServerEntity** @Entity("mcp_servers") @PrimaryKey(id=0), name, url, command, headers (JSON String), envVars (JSON String), isEnabled (Int: 1/0), projectId (0), createdAt

**DAOs (com.opencode.storage.dao):**
- **ProjectDao:** insertProject, updateProject, deleteProject, getAllProjects (ORDER BY lastUsed DESC), getProjectById, touchProject, markProjectModified
- **SessionDao:** insertSession, updateSession, deleteSession, getSessionsForProject (ORDER BY lastMessageAt DESC), getSessionById, updateSessionStatus, incrementMessageCount
- **MessageDao:** insertMessage, insertAllMessages, updateMessage, deleteMessage, getMessagesForSession (ORDER BY createdAt ASC), getMessagesByRole (sessionId + role), clearMessagesForSession, getPaginatedMessages (limit+offset)
- **UploadedFileDao:** insertFile, updateFile, deleteFile, getFilesForProject (ORDER BY uploadTimestamp DESC), getFileById, getFileByPath (filePath+projectId), clearFilesForProject
- **ModelDao:** insertModel, updateModel, deleteModel, getModelsForAgent (agentType, ORDER BY createdAt DESC), getModelById
- **McpServerDao:** insertMcpServer, updateMcpServer, deleteMcpServer, getAllMcpServers (orderBy createdAt), getMcpServerById, getEnabledMcpServers, deleteAllMcpServers

**OpenCodeDatabase:**
- @Database(entities=all 6, version=1, exportSchema=false)
- Abstract class extending RoomDatabase
- Abstract methods returning each DAO
- Companion object with getInstance(context): OpenCodeDatabase using Room.databaseBuilder with fallbackToDestructiveMigration

---

## PHASE 4 — Utilities Layer

**FileCategory enum (com.opencode.utils):**
- CODE, IMAGE, VIDEO, AUDIO, DOCUMENT, ARCHIVE, BINARY, MODEL, CONFIG, OTHER

**ProjectType enum (com.opencode.utils):**
- FLUTTER (displayName="Flutter", buildCommand="flutter build apk --release")
- KOTLIN_COMPOSE (displayName="Kotlin / Compose", buildCommand="./gradlew assembleRelease")
- JAVA_GRADLE (displayName="Java / Gradle", buildCommand="./gradlew assembleRelease")
- NATIVE_C_CPP (displayName="Native C/C++", buildCommand="cmake --build .")
- MIXED (displayName="Mixed / Unknown", buildCommand="")
- Companion: fromString(value: String) — parses to enum, returns MIXED on error

**ProjectTypeDetector object:**
- detectType(directory: String): ProjectType
- Scores each type by checking directory contents:
  - Flutter: pubspec.yaml (+3), lib/ (+1)
  - Gradle: settings.gradle/kts (+2 each), build.gradle/kts (+1 each), gradlew (+1 each)
  - Kotlin: each .kt file (+1 to KOTLIN_COMPOSE)
  - Java: each .java file (+1 to JAVA_GRADLE)
  - Native: CMakeLists.txt/Android.mk (+2 each), each .cpp/.c/.h/.hpp (+1 each)
  - If multiple types score > 0 → return MIXED
  - Otherwise return highest score or MIXED

**FileUploadManager class:**
- Constructor takes Context
- Extension sets for each category: IMAGE (jpg/png/gif/webp/bmp/svg), VIDEO (mp4/mov/avi/mkv/webm), AUDIO (mp3/wav/ogg/flac/aac), DOCUMENT (pdf/doc/txt/md/html/csv), ARCHIVE (zip/tar/gz/7z), MODEL (gguf/pt/onnx/tflite), CODE (kt/dart/java/py/js/ts/html/css/xml/json/yaml/toml/gradle/cpp/h/sh/go/rs/swift/php/vue/sql)
- suspend fun uploadFile(uri, path, subdirectory?): UploadResult — copies file, categorizes, unique name, auto-extracts archive
- suspend fun uploadMultipleFiles(uris, path, subdirectory): List<UploadResult>
- fun downloadProjectAsZip(projectPath, outputDir): File — ZIP of project
- fun listUploadedFiles(projectPath): List<FileInfo> — walks directory with categories

**UploadResult sealed class:**
- Success(fileName, path, mimeType, fileSize, category, isMedia, isArchive)
- Error(message)

**FileInfo data class:** name, path, size, category, lastModified

---

## PHASE 5 — UI Components (5 reusable composables)

**ChatMessages (com.opencode.ui.components.chat):**
- Params: messages (List<MessageEntity>), onPermissionGrant, onToolExecute
- LazyColumn with padding and spacing
- Each message by role:
  - USER: blue bubble right, user icon, timestamp
  - AGENT: white bubble left, robot icon, timestamp, content
  - TOOL_CALL: expandable card (name bold, params monospace, output preview, expand/collapse)
  - TOOL_RESULT: green-tinted card with output
  - PERMISSION: yellow-tinted card, approve/reject buttons
  - ERROR: red-tinted card, retry button

**TerminalView (com.opencode.ui.components.terminal):**
- Params: history, currentInput, onInputChange, onExecute, onClear, prompt (default "opencode@android:~$ "), isRunning, onBack (nullable)
- Full black background
- If onBack != null: header with title, clear, close buttons
- LazyColumn colored text lines (monospace, 11sp):
  - "ERROR" → red, "WARNING" → yellow, "SUCCESS"/"BUILD" → green, "$"/"opencode" → blue, others → white 85% alpha
  - Empty: welcome message 50% white
- Bottom card: prompt (blue monospace) + TextField + execute button
- Blinking cursor after input
- Execute button disabled when running or empty input

**WebPreview (com.opencode.ui.components.preview):**
- Params: url (default "http://localhost:8080"), onUrlChange, onReload, onBack
- Header row (if onBack != null): title, reload, close buttons
- Card with URL TextField + load button
- Loading indicator when loading
- Area (weight 1f) with border:
  - Loading: spinner + "Loading preview..."
  - Otherwise: AndroidView with WebView (javaScriptEnabled, domStorageEnabled)
- Bottom card: "APK Launch" section + "Launch APK" outlined button

**BuildOutput (com.opencode.ui.components.builds):**
- Params: output, isBuilding, buildStatus (enum: IN_PROGRESS/SUCCESS/FAILURE/CANCELLED)
- Card with "Build Output" title
- Status row: spinner+"Building..." (IN_PROGRESS), green "BUILD SUCCESSFUL" (SUCCESS), red "BUILD FAILED" (FAILURE), gray "CANCELLED" (CANCELLED)
- Scrollable area (200dp) monospace on dark bg
- Placeholder text when empty
- If SUCCESS + "BUILD SUCCESSFUL" in output: Install APK + Download ZIP buttons

**LoadingDialog (com.opencode.ui.components.common):**
- Params: message (default "Loading..."), onDismiss (nullable)
- If onDismiss != null: AlertDialog with spinner+message
- If onDismiss == null: full-screen overlay (70% alpha) with spinner+message

---

## PHASE 6 — Main App (MainActivity, Navigation, ViewModel, Home)

**MainViewModel (com.opencode.ui.main):**
- ViewModel with StateFlow<AppUiState>
- AppUiState with all app state fields
- MutableStateProperties: selectedAgentType (AgentType?), selectedModelConfig (ModelConfig?), selectedProjectId (Long?), currentRunMode (AgentRunMode=ON_DEVICE), notificationsEnabled (Boolean=true), projectSettings (Map<Long, ProjectSettings>)
- ProjectSettings: projectId, projectType (String), buildVariant (String), terminalHistory (List<String>), uploadPath (String), mcpServerIds (List<Long>), skillIds (List<Long}), permissionOverrides (Map<String, String>)
- Functions: selectAgentType, selectModelConfig, selectProject, setRunMode, setNotificationsEnabled, setThemeMode, setBiometricLock, saveProjectSettings, getProjectSettings

**HomeScreen (com.opencode.ui.main):**
- Scaffold: title "OpenCode", settings icon in top bar → onNavigateToSettings
- Column with padding:
  - Greeting text
  - 2x2 grid of action Cards each with icon + title + subtitle):
    - "New Session" + Add icon → onNewSession callback
    - "Import Project + Download icon → onImportProject callback
    - "Build" + Build icon → onBuild callback
    - "Upload Files + UploadFile icon → onUploadFiles callback

**AppNavigation (com.opencode.ui.main):**
- @Composable with onNavigateToSettings
- rememberNavController
- remember state vars for all 12 dialog screens (each Boolean, default false)
- Top-level Scaffold with SnackbarHost
- Inside padding: Column with NavHost (rememberNavController, "main" route, "home" start)
  - composable("home") → HomeScreen with all callbacks
  - composable("sessions") → SessionsScreen with callbacks
  - composable("projects") → ProjectsScreen with callbacks
  - composable("builds") → BuildsScreen with callbacks
  - composable("settings") → SettingsScreen with callbacks to each dialog
  - Each dialog screen as full-screen composable with back + dismiss callback
- After NavHost: 5-tab NavigationBar with NavigationBarItem each tab (Home/Message/Folder/Build/Settings icons each with label from strings.xml, selected state, click callback to navigate + close all dialogs
- Each dialog screen conditionally rendered (if dialogVisible state true) as full-screen composable

**MainActivity (com.opencode.ui.main):**
- ComponentActivity, enableEdgeToEdge (light status bar, dark nav bar)
- onCreate: request permissions (STORAGE runtime, NOTIFICATION runtime API 33+)
- Content: AppContent composable
  - MaterialTheme with Light theme
  - AppNavigation with all dialog callbacks as lambdas setting their respective state true
  - Each dialog has back button calling dismiss (sets state to false)
  - SnackbarHost at top level
  - All 12 dialog screens rendered as full-screen composables

---

## PHASE 7 — All Screen Composables

**SessionsScreen (com.opencode.ui.sessions):**
- Scaffold: title "Sessions", add icon → onNewSession
- Column: title, description
- "Start New Session") card (primary container) with add icon, title, subtitle → onNewSession
- "Recent Sessions" title, LazyColumn of SessionCard
- SessionCard: Card with status dot (color based on status: IDLE=primary, RUNNING=secondary, THINKING=tertiary, WAITING_INPUT=warning, COMPLETED=primary, ERRORED=error, else=primary), title, message count+status text, Compact/Fork/Delete icon buttons

**ProjectsScreen (com.opencode.ui.projects):**
- Scaffold: title "Projects", action icons: UploadFile, Add, Terminal, Web, Settings
- Column: title, description
- Row: "Import ZIP" (solid) → showImportDialog=true, "Build" (outlined) → onOpenBuild
- "Recent Projects" title, LazyColumn of project Cards (folder icon, name, type+last used, Download/Delete buttons)
- If showImportDialog: AlertDialog with "Import Project" title, "From ZIP" button, "From Folder" button, Cancel button

**BuildsScreen (com.opencode.ui.builds):**
- Scaffold: title "Builds", action icons: Terminal, Web, Settings
- Column: "Project Type" title, Row of FilterChips for each ProjectType
- "Build Variant" title, Row: "Debug APK" (DEBUG) / "Release APK" (RELEASE)
- "Build APK" button → isBuilding=true, spinner while building
- If building or output: Card with "Build Output" title, scrollable monospace text, if successful: Install APK + Download ZIP

**ChatScreen (com.opencode.ui.chat):**
- Scaffold: title "Chat", actions: Add (new session), Terminal, Web, Settings
- Column: "New Session" button, ChatMessages component
- Bottom: Row TextField (hint, weight 1f + Send button (arrow icon)
- Send: if input not blank, adds user message, clears input, triggers streaming

**TerminalScreen (full-screen, com.opencode.ui.terminal):**
- Scaffold: title "Terminal", back arrow, clear icon
- Column: full-width black Box (weight 1f) with LazyColumn of terminal lines (monospace colored), bottom Card with prompt + TextField + execute
- Execute: adds command line to history, simulates output, clears input

**PreviewScreen (full-screen, com.opencode.ui.preview):**
- Scaffold: title "Preview", back arrow
- Row of FilterChips: "Web Preview" (web icon) / "APK Launch" (devices icon)
- If WEB: Card with title, URL TextField, "Load Preview" button → isPreviewing=true, then AndroidView with WebView
- If APK: Card with title, "No apps built yet" text, disabled "Launch APK" button

**SettingsScreen (com.opencode.ui.settings):**
- Scaffold: title "Settings", back arrow
- Column: "Settings" title, notification toggle Card (Switch), "Configuration" title
- SettingsItem Card each: Agent Selection → AgentSelection, Models & API Keys → ModelSelection, MCP Servers → McpServers, Skills → Skills, Permissions → Permissions, Storage → empty, About → About
- Each: Card with icon, title, subtitle, ChevronRight, clickable

**AboutScreen (full-screen, com.opencode.ui.about):**
- Scaffold: title "About), back arrow
- Centered: Info icon (64dp, primary), "OpenCode Android title, "Universal Coding Agent Platform, Card with version/license/min SDK, "2026" footer

**AgentSelectionScreen (full-screen, com.opencode.ui.agent):**
- Scaffold: title "Select Coding Agent, back arrow
- Column: title, subtitle
- 5 Cards each with left border colored by accent, icon, displayName, description, capability badges, tap → onSelected then onDismiss

**ModelSelectionScreen (full-screen, com.opencode.ui.model):**
- Scaffold: title "Configure Model), back arrow, check (save)
- TabRow: "Cloud API") / "Local GGUF") / "Hybrid"
- Each tab: Cloud API: API key field (placeholder "sk-"), endpoint field, model name field, Local GGUF: file path field, select button, Hybrid: both
- Shared: Temperature slider (0-1, default 0.7f, label "X/100"), Max Tokens slider (1024-131072, default 4096), label shows value
- "Save Model Config") → creates ModelConfig, calls onSelected

**UploadScreen (full-screen, com.opencode.ui.upload):**
- Scaffold: title "Upload Files), back arrow, UploadFile icon (pick)
- Card: CloudUpload icon (48dp), "Upload any file type title, subtitle, "Select Files" button
- If selectedFiles: "Selected Files (count title, LazyColumn each file (type icon, filename, remove button), "Upload X files") button → showProgress=true, results, showProgress=false
- If uploadResults: "Upload Results title, each result (green check/filename or red error message)
- "Done") button → onUploadComplete() then onDismiss()

**ProjectImportScreen (full-screen, com.opencode.ui.projects.import):
- Scaffold: title "Import Project", back arrow, check (confirm, shown when type detected, not importing)
- "Import mode title, Row: "ZIP File" (compress icon) / "Folder (folder icon)
- If ZIP: Card with CloudUpload icon, description, "Select ZIP File") button → isImporting=true
- If FOLDER: Card with FolderOpen icon, description, "Select Folder") button
- If detectedType: Card with Info icon, "Project Type Detected title in primary container, detected type name
- If isImporting: Card with status text, LinearProgressIndicator (progress), percentage
- If progress >= 1f (complete): Card with CheckCircle icon (48dp, primary), "Imported successfully!" title, "Open Project") solid (calls onImported(1L)), "Cancel") outlined (onDismiss)

**PermissionsScreen (full-screen, com.opencode.ui.permissions):
- Scaffold: title "Permissions", back arrow, check (save)
- Column: "Tool & Action Permissions title, description
- PermissionItem Card each (6 total): Shell Commands (Terminal, File Editing (Edit, Subagent Spawning (Person, Skill Loading (SmartButton), Web Fetch (Web, MCP Tool Calls (Devices
- Each: icon, title, description, clickable button cycling ALLOW→ASK→DENY
- Each state: ALLOW=green check, ASK=blue question, DENY=red cancel

**McpServersScreen (full-screen, com.opencode.ui.mcps):
- Scaffold: title "MCP Servers, back arrow, Add icon
- Column: title, description
- Row: "Remote HTTP (Devices icon) / "Local Stdio" (Terminal icon)
- Card: server name field, conditional URL (remote) or command (local) field, optional headers/env fields, "Add Server") button
- "Connected Servers title, LazyColumn of server Cards (icon, name, "Connected" status, Cancel icon)

**SkillsScreen (full-screen, com.opencode.ui.skills):
- Scaffold: title "Skills, back arrow, Add icon
- Column: title, description about SKILL.md
- Card about skill discovery paths (4 paths as text lines)
- "Available Skills title, LazyColumn of skill Cards (SmartButton icon, title, description)

---

## THAT'S THE FULL APP

Each phase builds on the previous. After Phase 1, your project compiles. After each phase, more features are wired in. By Phase 7, the entire app is complete.

**Start with Phase 1, verify it compiles, then proceed to the next phase.**
