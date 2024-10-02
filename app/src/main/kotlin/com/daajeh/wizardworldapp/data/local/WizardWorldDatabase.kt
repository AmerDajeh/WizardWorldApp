package com.daajeh.wizardworldapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.daajeh.wizardworldapp.data.local.dao.ElixirDao
import com.daajeh.wizardworldapp.data.local.dao.IngredientDao
import com.daajeh.wizardworldapp.data.local.dao.InventorDao
import com.daajeh.wizardworldapp.data.local.dto.Converters
import com.daajeh.wizardworldapp.data.local.dto.ElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.IngredientEntity
import com.daajeh.wizardworldapp.data.local.dto.InventorEntity

@Database(entities = [ElixirEntity::class, IngredientEntity::class, InventorEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class WizardWorldDatabase : RoomDatabase() {
    abstract fun elixirDao(): ElixirDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun inventorDao(): InventorDao
}
