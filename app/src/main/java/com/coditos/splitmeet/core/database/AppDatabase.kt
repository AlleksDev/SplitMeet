package com.coditos.splitmeet.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.coditos.splitmeet.core.database.dao.OutingDao
import com.coditos.splitmeet.core.database.dao.ParticipantDao
import com.coditos.splitmeet.core.database.entities.OutingEntity
import com.coditos.splitmeet.core.database.entities.ParticipantEntity



@Database(
    entities = [OutingEntity::class, ParticipantEntity::class],
    version = 4,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun outingDao(): OutingDao
    abstract fun participantDao(): ParticipantDao
}