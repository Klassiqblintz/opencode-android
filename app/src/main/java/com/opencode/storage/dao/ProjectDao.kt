package com.opencode.storage.dao

import androidx.room.*
import com.opencode.storage.entities.ProjectEntity
import androidx.lifecycle.Flow

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("SELECT * FROM projects ORDER BY lastUsed DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: Long): ProjectEntity?

    @Query("UPDATE projects SET lastUsed = :timestamp WHERE id = :id")
    suspend fun touchProject(id: Long, timestamp: Long)

    @Query("UPDATE projects SET lastModified = :timestamp WHERE id = :id")
    suspend fun markProjectModified(id: Long, timestamp: Long)
}
