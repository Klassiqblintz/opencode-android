package com.opencode.storage.dao

import androidx.room.*
import com.opencode.storage.entities.ModelEntity

@Dao
interface ModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(model: ModelEntity): Long

    @Update
    suspend fun updateModel(model: ModelEntity)

    @Delete
    suspend fun deleteModel(model: ModelEntity)

    @Query("SELECT * FROM models WHERE agentType = :agentType ORDER BY createdAt DESC")
    fun getModelsForAgent(agentType: String): androidx.room.Flow<List<ModelEntity>>

    @Query("SELECT * FROM models WHERE id = :id")
    suspend fun getModelById(id: Long): ModelEntity?
}
