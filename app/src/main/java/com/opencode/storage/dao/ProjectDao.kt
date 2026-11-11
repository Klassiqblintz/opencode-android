package com.opencode.storage.dao

import androidx.room.*
import com.opencode.storage.entities.ProjectEntity

/**
 * DAO for project operations
 */
@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("SELECT * FROM projects ORDER BY lastUsed DESC")
    suspend fun getAllProjects(): List<ProjectEntity>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getProjectById(projectId: Long): ProjectEntity?

    @Query("UPDATE projects SET lastUsed = :timestamp WHERE id = :projectId")
    suspend fun touchProject(projectId: Long, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE projects SET lastModified = :timestamp WHERE id = :projectId")
    suspend fun markProjectModified(projectId: Long, timestamp: Long = System.currentTimeMillis())
}
