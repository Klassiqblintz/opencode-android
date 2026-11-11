package com.opencode.storage.dao

import androidx.room.*
import com.opencode.storage.entities.UploadedFileEntity

/**
 * DAO for uploaded file operations
 */
@Dao
interface UploadedFileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: UploadedFileEntity): Long

    @Update
    suspend fun updateFile(file: UploadedFileEntity)

    @Delete
    suspend fun deleteFile(file: UploadedFileEntity)

    @Query("SELECT * FROM uploaded_files WHERE projectId = :projectId ORDER BY uploadTimestamp DESC")
    suspend fun getFilesForProject(projectId: Long): List<UploadedFileEntity>

    @Query("SELECT * FROM uploaded_files WHERE id = :fileId")
    suspend fun getFileById(fileId: Long): UploadedFileEntity?

    @Query("SELECT * FROM uploaded_files WHERE filePath = :filePath AND projectId = :projectId")
    suspend fun getFileByPath(filePath: String, projectId: Long): UploadedFileEntity?

    @Query("DELETE FROM uploaded_files WHERE projectId = :projectId")
    suspend fun clearFilesForProject(projectId: Long)

    @Query("SELECT COUNT(*) FROM uploaded_files WHERE projectId = :projectId")
    suspend fun getFileCountForProject(projectId: Long): Int
}
