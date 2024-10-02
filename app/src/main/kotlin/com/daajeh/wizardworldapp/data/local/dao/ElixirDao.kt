package com.daajeh.wizardworldapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.daajeh.wizardworldapp.data.local.dto.ElixirEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ElixirDao {
    @Insert
    suspend fun insert(elixir: ElixirEntity)

    @Query("SELECT * FROM elixirs WHERE id=:elixirId")
    fun getElixirById(elixirId: String): Flow<ElixirEntity>
}
