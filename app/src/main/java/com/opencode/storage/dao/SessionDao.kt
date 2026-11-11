package com.opencode.storage.dao

import androidx.room.*
import com.opencode.storage.entities.SessionEntity
import com.opencode.storage.entities.SessionStatus

/**
 * DAO for session operations
 */
@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity): Long

    @Update
    suspend fun updateSession(session: SessionEntity)

    @Delete
    suspend fun deleteSession(session: SessionEntity)

    @Query("SELECT * FROM sessions WHERE projectId = :projectId ORDER BY lastMessageAt DESC")
    suspend fun getSessionsForProject(projectId: Long): List<SessionEntity>

    @Query("SELECT * FROM sessions WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: Long): SessionEntity?

    @Query("UPDATE sessions SET status = :status, lastMessageAt = :timestamp WHERE id = :sessionId")
    suspend fun updateSessionStatus(
        sessionId: Long,
        status: SessionStatus,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("UPDATE sessions SET messageCount = messageCount + 1, lastMessageAt = :timestamp WHERE id = :sessionId")
    suspend fun incrementMessageCount(
        sessionId: Long,
        timestamp: Long = System.currentTimeMillis()
    )
}
