package com.daajeh.wizardworldapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.daajeh.wizardworldapp.data.local.dao.ElixirDao
import com.daajeh.wizardworldapp.data.local.dao.FavouriteElixirDao
import com.daajeh.wizardworldapp.data.local.dao.IngredientDao
import com.daajeh.wizardworldapp.data.local.dao.InventorDao
import com.daajeh.wizardworldapp.data.local.dao.LightElixirDao
import com.daajeh.wizardworldapp.data.local.dto.Converters
import com.daajeh.wizardworldapp.data.local.dto.ElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.FavouriteElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.IngredientEntity
import com.daajeh.wizardworldapp.data.local.dto.InventorEntity
import com.daajeh.wizardworldapp.data.local.dto.LightElixirEntity

@Database(entities = [LightElixirEntity::class,ElixirEntity::class, IngredientEntity::class, InventorEntity::class, FavouriteElixirEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WizardWorldDatabase : RoomDatabase() {
    abstract fun lightElixirDao(): LightElixirDao
    abstract fun favouriteElixirDao(): FavouriteElixirDao
    abstract fun elixirDao(): ElixirDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun inventorDao(): InventorDao
}
