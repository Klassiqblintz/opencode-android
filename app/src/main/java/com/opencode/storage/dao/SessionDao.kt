package com.opencode.storage.dao

import androidx.room.*
import com.opencode.storage.entities.SessionEntity
import androidx.lifecycle.Flow

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity): Long

    @Update
    suspend fun updateSession(session: SessionEntity)

    @Delete
    suspend fun deleteSession(session: SessionEntity)

    @Query("SELECT * FROM sessions WHERE projectId = :projectId ORDER BY lastMessageAt DESC")
    fun getSessionsForProject(projectId: Long): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSessionById(id: Long): SessionEntity?

    @Query("UPDATE sessions SET status = :status WHERE id = :id")
    suspend fun updateSessionStatus(id: Long, status: String)

    @Query("UPDATE sessions SET messageCount = messageCount + 1, lastMessageAt = :timestamp WHERE id = :id")
    suspend fun incrementMessageCount(id: Long, timestamp: Long)
}
