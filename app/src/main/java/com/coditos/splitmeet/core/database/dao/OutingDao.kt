package com.coditos.splitmeet.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.coditos.splitmeet.core.database.entities.OutingEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Outing entity.
 * 
 * Single Source of Truth Pattern:
 * - Functions returning Flow<> are used for reactive UI updates
 * - All mutating operations (delete, update) are immediate and optimistic
 * - Repository handles API calls and rollback on failure
 */
@Dao
interface OutingDao {
    
    @Query("SELECT * FROM outings ORDER BY CASE WHEN createdAt IS NULL THEN 1 ELSE 0 END, createdAt DESC")
    fun getAllOutings(): Flow<List<OutingEntity>>

    @Query("SELECT * FROM outings WHERE id = :id")
    fun getOutingByIdAsFlow(id: Long): Flow<OutingEntity?>
    
    @Query("SELECT * FROM outings WHERE id = :id")
    suspend fun getOutingById(id: Long): OutingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutings(outings: List<OutingEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOuting(outing: OutingEntity)
    
    @Update
    suspend fun updateOuting(outing: OutingEntity)
    
    @Delete
    suspend fun deleteOuting(outing: OutingEntity)
    
    @Query("DELETE FROM outings WHERE id = :id")
    suspend fun deleteOutingById(id: Long)
    
    /**
     * Update only specific fields of an outing.
     * Used for optimistic updates without fetching full entity.
     */
    @Query("UPDATE outings SET name = :name, description = :description, categoryName = :categoryName WHERE id = :id")
    suspend fun updateOutingBasicInfo(id: Long, name: String, description: String?, categoryName: String)
}
