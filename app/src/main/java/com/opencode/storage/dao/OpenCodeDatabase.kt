package com.opencode.storage.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.opencode.storage.entities.*

/**
 * Room database for OpenCode Android
 * All entities: projects, sessions, messages, uploaded files, models, MCP servers
 */
@Database(
    entities = [
        ProjectEntity::class,
        SessionEntity::class,
        MessageEntity::class,
        UploadedFileEntity::class,
        ModelEntity::class,
        McpServerEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class OpenCodeDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun sessionDao(): SessionDao
    abstract fun messageDao(): MessageDao
    abstract fun uploadedFileDao(): UploadedFileDao
    abstract fun modelDao(): ModelDao
    abstract fun mcpServerDao(): McpServerDao

    companion object {
        private const val DATABASE_NAME = "opencode_db"

        @Volatile
        private var instance: OpenCodeDatabase? = null

        fun getInstance(context: Context): OpenCodeDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): OpenCodeDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                OpenCodeDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
