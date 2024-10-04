package com.daajeh.wizardworldapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daajeh.wizardworldapp.data.local.dto.wizard.WizardEntity
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteWizardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WizardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wizard: WizardEntity)

    @Query("SELECT * FROM wizards WHERE id=:wizardId")
    fun getById(wizardId: String): Flow<WizardEntity?>

    @Query("SELECT * FROM wizards")
    fun getWizards(): Flow<List<WizardEntity>>

    // favourite
    @Insert
    suspend fun saveFavourite(elixir: FavouriteWizardEntity)

    @Query("DELETE FROM favourite_wizards WHERE id=:wizardId")
    suspend fun removeFavorite(wizardId: String)

    @Query("SELECT EXISTS(SELECT * FROM favourite_wizards WHERE id = :wizardId)")
    suspend fun isFavourite(wizardId: String): Boolean
}
