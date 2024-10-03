package com.daajeh.wizardworldapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.daajeh.wizardworldapp.data.local.dao.ElixirDao
import com.daajeh.wizardworldapp.data.local.dao.IngredientDao
import com.daajeh.wizardworldapp.data.local.dao.InventorDao
import com.daajeh.wizardworldapp.data.local.dao.WizardDao
import com.daajeh.wizardworldapp.data.local.dto.converter.Converters
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteWizardEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ingredient.IngredientEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.inventor.InventorEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.WizardEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.LightElixirEntity

@Database(entities = [WizardEntity::class, FavouriteWizardEntity::class , ElixirEntity::class, LightElixirEntity::class, FavouriteElixirEntity::class, IngredientEntity::class, InventorEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WizardWorldDatabase : RoomDatabase() {
    abstract fun wizardDao(): WizardDao
    abstract fun elixirDao(): ElixirDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun inventorDao(): InventorDao
}
