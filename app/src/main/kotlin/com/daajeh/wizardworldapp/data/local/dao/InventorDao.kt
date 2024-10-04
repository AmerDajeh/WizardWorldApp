package com.daajeh.wizardworldapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.inventor.InventorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inventor: InventorEntity)

    @Query("SELECT * FROM inventors WHERE elixirId = :elixirId")
    fun getForElixir(elixirId: String): Flow<List<InventorEntity>>
}
