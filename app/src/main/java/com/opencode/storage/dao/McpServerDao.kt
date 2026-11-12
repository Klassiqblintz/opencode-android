package com.opencode.storage.dao

import androidx.room.*
import com.opencode.storage.entities.McpServerEntity
import androidx.lifecycle.Flow

@Dao
interface McpServerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMcpServer(server: McpServerEntity): Long

    @Update
    suspend fun updateMcpServer(server: McpServerEntity)

    @Delete
    suspend fun deleteMcpServer(server: McpServerEntity)

    @Query("SELECT * FROM mcp_servers ORDER BY createdAt ASC")
    fun getAllMcpServers(): Flow<List<McpServerEntity>>

    @Query("SELECT * FROM mcp_servers WHERE id = :id")
    suspend fun getMcpServerById(id: Long): McpServerEntity?

    @Query("SELECT * FROM mcp_servers WHERE isEnabled = 1 ORDER BY createdAt ASC")
    fun getEnabledMcpServers(): Flow<List<McpServerEntity>>

    @Query("DELETE FROM mcp_servers")
    suspend fun deleteAllMcpServers()
}
