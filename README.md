# OpenCode Android — Universal Coding Agent Platform

A native Android app (Kotlin/Jetpack Compose) that lets users run multiple coding agents on their device.

## Features

- **Multi-agent platform**: OpenCode, DeepSeek, Claude Code, Pi, and Custom agents — each with unique accent colors
- **Dual mode**: On-device (pRoot Linux) or remote server connection per agent
- **Model support**: Cloud API keys + local GGUF models (via llama.cpp JNI), plus hybrid mode
- **APK building**: All agents can build Flutter, Kotlin/Compose, Java/Gradle, and Native C/C++ projects
- **Full coding platform**: Chat, terminal, preview panels, task/todo lists, smooth animations
- **File uploads**: Any file type into the workspace (images, videos, audio, documents, archives, models, code)
- **Project management**: Import ZIP, download ZIP, recent projects, file browser, auto-detection
- **All OpenCode features**: Sessions, skills, MCP servers, agents, permissions, Code Mode, plugins, subagents
- **Notifications**: Push notification toggle for agent completion, permissions, builds
- **Background service**: Engine survives app close (foreground service with persistent notification)
- **Terminal & Preview**: Embedded xterm.js-style terminal; Preview with WebView + native APK launch
- **License**: MIT

## Tech Stack

- **Language**: Kotlin (JVM 17+)
- **UI**: Jetpack Compose + Material 3
- **Navigation**: Navigation Compose
- **State**: Kotlin Flow + StateFlow + ViewModel
- **DI**: Hilt (Dagger)
- **DB**: Room (SQLite)
- **Networking**: OkHttp + kotlinx-serialization
- **Images**: Coil (image loading)
- **Markdown**: CommonMark (rendering)
- **Native**: NDK + CMake (llama.cpp JNI bridge)
- **Min SDK**: Android 8.0 (API 26)
- **Compile SDK**: 35

## Architecture

```
opencode-android/
├── app/                          # Main Android app
│   ├── src/main/java/com/opencode/
│   │   ├── app/                  # Hilt Application class, notification manager
│   │   ├── engine/               # AgentType, AgentRunMode, ModelConfig, AgentConfig, AgentEngineService
│   │   ├── bridge/               # Device bridge commands (tap, swipe, shell, etc.)
│   │   ├── storage/              # Room DB
│   │   │   ├── entities/         # Project, Session, Message, UploadedFile, Model, McpServer
│   │   │   ├── dao/              # ProjectDao, SessionDao, MessageDao, UploadedFileDao, ModelDao, McpServerDao
│   │   │   └── room/             # OpenCodeDatabase
│   │   ├── ui/                   # Compose screens & components
│   │   │   ├── main/             # MainActivity, AppNavigation, HomeScreen, MainViewModel
│   │   │   ├── chat/             # ChatScreen (with terminal/preview split panel)
│   │   │   ├── sessions/         # SessionsScreen (list, create, fork, compact, delete)
│   │   │   ├── projects/         # ProjectsScreen, ProjectImportScreen
│   │   │   ├── builds/           # BuildsScreen (all project types, debug/release)
│   │   │   ├── settings/         # SettingsScreen (all configuration entries)
│   │   │   ├── agent/            # AgentSelectionScreen (cards with accent colors)
│   │   │   ├── model/            # ModelSelectionScreen (Cloud/GGUF/Hybrid tabs)
│   │   │   ├── terminal/         # TerminalScreen (full-screen xterm.js-style terminal)
│   │   │   ├── preview/          # PreviewScreen (WebView + APK launch)
│   │   │   ├── upload/           # UploadScreen (any file type, progress, results)
│   │   │   ├── permissions/      # PermissionsScreen (shell/edit/subagent/skill/web/MCP)
│   │   │   ├── skills/           # SkillsScreen (discovery paths, available skills)
│   │   │   ├── mcps/             # McpServersScreen (remote HTTP / local stdio)
│   │   │   ├── about/            # AboutScreen (version, license, credits)
│   │   │   └── components/       # Reusable components
│   │   │       ├── chat/         # ChatMessages (all message types)
│   │   │       ├── terminal/     # TerminalView (embedded terminal)
│   │   │       ├── preview/      # WebPreview (WebView + APK launch)
│   │   │       ├── builds/       # BuildOutput (status indicator, scrollable output)
│   │   │       └── common/       # LoadingDialog (overlay + alert)
│   │   └── utils/                # Shared types
│   │       ├── FileCategory.kt   # CODE/IMAGE/VIDEO/AUDIO/DOCUMENT/ARCHIVE/BINARY/MODEL/CONFIG/OTHER
│   │       ├── FileUploadManager.kt # Upload/download/extract ZIP
│   │       ├── ProjectType.kt    # FLUTTER/KOTLIN_COMPOSE/JAVA_GRADLE/NATIVE_C_CPP/MIXED
│   │       └── ProjectTypeDetector.kt # Auto-detects project type from directory contents
│   ├── src/main/jni/             # Native (llama.cpp bridge)
│   │   └── CMakeLists.txt
│   └── build.gradle.kts
├── app/src/main/res/             # Resources
│   ├── values/                   # colors.xml, strings.xml, themes.xml
│   ├── drawable/                 # ic_engine.xml, ic_launcher.xml
│   ├── mipmap-*/                 # Launcher icons
│   └── xml/                      # backup_rules.xml, data_extraction_rules.xml
├── gradle.properties
├── settings.gradle.kts
└── proguard-rules.pro
```

## Per-Agent Accent Colors

| Agent | Accent Color | Description |
|-------|-------------|-------------|
| OpenCode | `0xFF1E88E5` (blue) | Default agent, full feature set |
| DeepSeek | `0xFF7C4DFF` (purple) | Specialized for code generation |
| Claude Code | `0xFF7E57C2` (deep purple) | Advanced reasoning, multi-modal |
| Pi Agent | `0xFF00BFA5` (teal) | Conversational coding assistant |
| Custom | `0xFF607D8B` (blue-gray) | User-configured endpoint/model |

## Screen Flow

```
MainActivity
├── Home (Chat) ──→ ChatScreen (terminal/preview buttons, split panel)
│   ├── → TerminalScreen (full-screen terminal)
│   ├── → PreviewScreen (WebView + APK launch)
│   ├── → SessionsScreen
│   │   ├── → New session
│   │   └── → ChatScreen (existing session)
│   ├── → ProjectsScreen
│   │   ├── → ProjectImportScreen (ZIP/folder import)
│   │   └── → UploadScreen
│   ├── → BuildsScreen (build all project types)
│   └── → SettingsScreen
│       ├── → AgentSelectionScreen
│       ├── → ModelSelectionScreen
│       ├── → McpServersScreen
│       ├── → SkillsScreen
│       ├── → PermissionsScreen
│       └── → AboutScreen
└── 5-tab Bottom Navigation: Home | Sessions | Projects | Builds | Settings
```

## Build Types

Each project type uses the appropriate build command:

| Project Type | Build Command |
|-------------|---------------|
| Flutter | `flutter build apk --release` |
| Kotlin/Compose | `./gradlew assembleRelease` |
| Java/Gradle | `./gradlew assembleRelease` |
| Native C/C++ | `cmake --build .` |

## Dependencies

- androidx.compose + Material 3 + Navigation Compose
- Hilt (DI) + Dagger
- Room (persistence) + KSP
- OkHttp (networking) + kotlinx-serialization
- Coil (image loading) + CommonMark (markdown)
- Apache Commons Compress (ZIP handling)
- Biometric API
- WorkManager (background tasks)
- DocumentFile (file picker)

## Status

- All 25+ screen files created and wired
- All component libraries built (ChatMessages, TerminalView, WebPreview, BuildOutput, LoadingDialog)
- Room database schema complete (6 entities, 6 DAOs)
- Background service with START_STICKY
- All compile errors fixed (imports, duplicates, type mismatches)
- Shared types in utils package prevent duplication
- Navigation routes properly wired through MainActivity

## License

MIT License — see the LICENSE file for details.
