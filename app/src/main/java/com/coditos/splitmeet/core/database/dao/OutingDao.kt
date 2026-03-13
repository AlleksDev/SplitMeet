package com.coditos.splitmeet.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.coditos.splitmeet.core.database.entities.OutingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OutingDao {
    @Query("SELECT * FROM outings")
    fun getAllOutings(): Flow<List<OutingEntity>>


    @Query("SELECT * FROM outings WHERE id = :id")
    fun getOutingById(id: Int): OutingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOutings(outings: List<OutingEntity>)

}