package com.opencode.storage.dao

import androidx.room.*
import com.opencode.storage.entities.ModelEntity
import com.opencode.storage.entities.McpServerEntity

/**
 * DAO for model configurations
 */
@Dao
interface ModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(model: ModelEntity): Long

    @Update
    suspend fun updateModel(model: ModelEntity)

    @Delete
    suspend fun deleteModel(model: ModelEntity)

    @Query("SELECT * FROM models WHERE agentType = :agentType ORDER BY createdAt DESC")
    suspend fun getModelsForAgent(agentType: String): List<ModelEntity>

    @Query("SELECT * FROM models WHERE id = :modelId")
    suspend fun getModelById(modelId: Long): ModelEntity?
}

/**
 * DAO for MCP server configurations
 */
@Dao
interface McpServerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMcpServer(server: McpServerEntity): Long

    @Update
    suspend fun updateMcpServer(server: McpServerEntity)

    @Delete
    suspend fun deleteMcpServer(server: McpServerEntity)

    @Query("SELECT * FROM mcp_servers WHERE isEnabled = 1 ORDER BY name ASC")
    suspend fun getAllEnabledMcpServers(): List<McpServerEntity>

    @Query("SELECT * FROM mcp_servers WHERE id = :serverId")
    suspend fun getMcpServerById(serverId: Long): McpServerEntity?

    @Query("SELECT * FROM mcp_servers WHERE projectId = :projectId ORDER BY name ASC")
    suspend fun getMcpServersForProject(projectId: Long): List<McpServerEntity>
}
