package com.daajeh.wizardworldapp.data.repository

import com.daajeh.wizardworldapp.data.ElixirRepositoryImpl
import com.daajeh.wizardworldapp.data.local.dao.ElixirDao
import com.daajeh.wizardworldapp.data.local.dao.IngredientDao
import com.daajeh.wizardworldapp.data.local.dao.InventorDao
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ElixirEntity
import com.daajeh.wizardworldapp.data.network.WizardWorldApi
import com.daajeh.wizardworldapp.domain.ElixirRepository
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ElixirRepositoryTest {

    private lateinit var elixirRepository: ElixirRepository

    @MockK
    private lateinit var mockkElixirDao: ElixirDao

    @MockK
    private lateinit var mockkIngredientDao: IngredientDao

    @MockK
    private lateinit var mockkInventorDao: InventorDao

    @MockK
    private lateinit var mockkApi: WizardWorldApi

    private val elixir =
        ElixirEntity(id = "1", "")


    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        clearAllMocks()
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test get elixir by id`() = runTest {
        every { mockkElixirDao.getById(elixir.id) } returns flowOf(elixir)
        coEvery { mockkIngredientDao.getForElixir(any()) } returns flowOf(emptyList())
        coEvery { mockkInventorDao.getForElixir(any()) } returns flowOf(emptyList())
        coEvery { mockkElixirDao.isFavourite(elixir.id) } returns  true

        elixirRepository = ElixirRepositoryImpl(
            dao = mockkElixirDao,
            ingredientDao = mockkIngredientDao,
            inventorDao = mockkInventorDao
        )

        val result = elixirRepository.getElixirById(elixir.id)

        result.collect { elixir ->
            Assertions.assertNotNull(elixir)
        }

        coVerify { mockkElixirDao.getById(elixir.id) }
        coVerify(exactly = 1) { mockkElixirDao.isFavourite(any()) }
        coVerify(exactly = 1) { mockkIngredientDao.getForElixir(any()) }
        coVerify(exactly = 1) { mockkInventorDao.getForElixir(any()) }
    }

    @Test
    fun `test save favorite elixir`() = runTest {
        coEvery { mockkElixirDao.getById(elixir.id) } returns flowOf(elixir)
        coEvery { mockkElixirDao.saveFavourite(FavouriteElixirEntity(elixir.id)) } just Runs

        elixirRepository = ElixirRepositoryImpl(
            dao = mockkElixirDao,
            ingredientDao = mockkIngredientDao,
            inventorDao = mockkInventorDao
        )

        elixirRepository.toggleFavourite(elixir.id)

        coVerify(exactly = 1) { mockkElixirDao.getById(elixir.id) }
        coVerify(exactly = 1) { mockkElixirDao.saveFavourite(any()) }
    }

    @Test
    fun `test remove favourite elixir`() = runTest {
        coEvery { mockkElixirDao.removeFavourite(elixir.id) } just Runs

        elixirRepository = ElixirRepositoryImpl(
            dao = mockkElixirDao,
            ingredientDao = mockkIngredientDao,
            inventorDao = mockkInventorDao
        )

        elixirRepository.removeFavorite(elixir.id)

        coVerify(exactly = 1) { mockkElixirDao.removeFavourite(elixir.id) }
    }

}
