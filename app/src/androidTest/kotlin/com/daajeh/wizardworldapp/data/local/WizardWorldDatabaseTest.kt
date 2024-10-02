package com.daajeh.wizardworldapp.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.daajeh.wizardworldapp.data.local.dao.ElixirDao
import com.daajeh.wizardworldapp.data.local.dao.IngredientDao
import com.daajeh.wizardworldapp.data.local.dao.InventorDao
import com.daajeh.wizardworldapp.data.local.dto.ElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.IngredientEntity
import com.daajeh.wizardworldapp.data.local.dto.InventorEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WizardWorldDatabaseTest {

    private lateinit var database: WizardWorldDatabase
    private lateinit var elixirDao: ElixirDao
    private lateinit var ingredientDao: IngredientDao
    private lateinit var inventorDao: InventorDao

    // Dummy Data
    private val dummyElixirEntity = ElixirEntity(
        id = "elixir_001",
        characteristics = "Healing properties",
        difficulty = "Medium",
        effect = "Restores health",
        manufacturer = "Herb Co.",
        name = "Healing Elixir",
        sideEffects = "None",
        time = "5 minutes"
    )

    private val dummyIngredients = listOf(
        IngredientEntity(
            id = "ingredient_001",
            name = "Herb",
            elixirId = "elixir_001"
        ),
        IngredientEntity(
            id = "ingredient_002",
            name = "Water",
            elixirId = "elixir_001"
        )
    )

    private val dummyInventors = listOf(
        InventorEntity(
            id = "inventor_001",
            firstName = "John",
            lastName = "Doe",
            elixirId = "elixir_001"
        ),
        InventorEntity(
            id = "inventor_002",
            firstName = "Jane",
            lastName = "Smith",
            elixirId = "elixir_001"
        )
    )

    @Before
    fun setUp() {
        // Initialize Room Database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WizardWorldDatabase::class.java
        ).build()

        elixirDao = database.elixirDao()
        ingredientDao = database.ingredientDao()
        inventorDao = database.inventorDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieveElixir() = runBlocking {
        // Insert Dummy Data
        elixirDao.insert(dummyElixirEntity)
        dummyIngredients.forEach { ingredientDao.insert(it) }
        dummyInventors.forEach { inventorDao.insert(it) }

        // Retrieve Elixir
        val retrievedElixir = elixirDao.getElixirById(dummyElixirEntity.id)
        assert(retrievedElixir == dummyElixirEntity)

        // Retrieve Ingredients
        val retrievedIngredients = ingredientDao.getIngredientsForElixir(dummyElixirEntity.id)
        assert(retrievedIngredients.size == dummyIngredients.size)
        assert(retrievedIngredients.containsAll(dummyIngredients))

        // Retrieve Inventors
        val retrievedInventors = inventorDao.getInventorsForElixir(dummyElixirEntity.id)
        assert(retrievedInventors.size == dummyInventors.size)
        assert(retrievedInventors.containsAll(dummyInventors))
    }
}