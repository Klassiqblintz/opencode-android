package com.opencode.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.opencode.storage.dao.*
import com.opencode.storage.entities.*

@Database(
    entities = [
        ProjectEntity::class,
        SessionEntity::class,
        MessageEntity::class,
        UploadedFileEntity::class,
        ModelEntity::class,
        McpServerEntity::class
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
        @Volatile
        private var INSTANCE: OpenCodeDatabase? = null

        fun getInstance(context: Context): OpenCodeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OpenCodeDatabase::class.java,
                    "opencode_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
