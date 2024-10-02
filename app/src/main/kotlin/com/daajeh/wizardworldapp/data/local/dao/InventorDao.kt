package com.daajeh.wizardworldapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.daajeh.wizardworldapp.data.local.dto.InventorEntity

@Dao
interface InventorDao {
    @Insert
    suspend fun insert(inventor: InventorEntity)

    @Query("SELECT * FROM inventors WHERE elixirId = :elixirId")
    suspend fun getInventorsForElixir(elixirId: String): List<InventorEntity>
}
