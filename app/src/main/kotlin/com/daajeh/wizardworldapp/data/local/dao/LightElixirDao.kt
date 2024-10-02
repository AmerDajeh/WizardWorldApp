package com.daajeh.wizardworldapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.daajeh.wizardworldapp.data.local.dto.LightElixirEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LightElixirDao {
    @Insert
    suspend fun insert(elixirEntity: LightElixirEntity)

    @Query("SELECT * FROM light_elixirs WHERE difficulty = :difficulty")
    fun getByDifficulty(difficulty: String): Flow<List<LightElixirEntity>>
}
