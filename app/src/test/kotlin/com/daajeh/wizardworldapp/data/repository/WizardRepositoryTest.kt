package com.daajeh.wizardworldapp.data.repository

import com.daajeh.wizardworldapp.data.WizardRepositoryImpl
import com.daajeh.wizardworldapp.data.local.dao.WizardDao
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteWizardEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.WizardEntity
import com.daajeh.wizardworldapp.data.network.WizardWorldApi
import com.daajeh.wizardworldapp.data.network.dto.WizardDto
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.domain.entity.Wizard
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

class WizardRepositoryTest {

    private lateinit var wizardRepository: WizardRepository

    @MockK
    private lateinit var mockkElixirRepository: ElixirRepository

    @MockK
    private lateinit var mockkDao: WizardDao

    @MockK
    private lateinit var mockkApi: WizardWorldApi

    private val wizardsEntityList = listOf(
        WizardEntity(id = "1", firstName = "Harry", lastName = "Potter"),
        WizardEntity(id = "2", firstName = "Hermione", lastName = "Granger")
    )

    private val wizardsList = listOf(
        Wizard(id = "1", firstName = "Harry", lastName = "Potter"),
        Wizard(id = "2", firstName = "Hermione", lastName = "Granger")
    )

    private val wizardsNetworkList = listOf(
        WizardDto(id = "1", firstName = "Harry", lastName = "Potter", elixirs = listOf()),
        WizardDto(id = "2", firstName = "Hermione", lastName = "Granger", elixirs = listOf())
    )

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
    fun `test getWizards returns wizards with elixirs and favourite`() = runTest {
        every { mockkDao.getWizards() } returns flowOf(wizardsEntityList)
        coEvery { mockkElixirRepository.getWizardLightElixirs(any()) } returns emptyList()
        coEvery { mockkDao.isFavourite(any()) } returns true

        wizardRepository = WizardRepositoryImpl(
            api = mockkApi,
            dao = mockkDao,
            elixirRepository = mockkElixirRepository
        )

        val result = wizardRepository.getWizards()

        result.collect { wizards ->
            Assertions.assertEquals(wizardsEntityList.size, wizards.size)
        }

        coVerify { mockkDao.getWizards() }
        coVerify(exactly = wizardsEntityList.size) { mockkDao.isFavourite(any()) }
        coVerify(exactly = wizardsEntityList.size) { mockkElixirRepository.getWizardLightElixirs(any()) }
    }

    @Test
    fun `test getWizard by id`() = runTest {
        val wizard = wizardsEntityList.first()

        every { mockkDao.getById(wizard.id) } returns flowOf(wizard)
        coEvery { mockkElixirRepository.getWizardLightElixirs(any()) } returns emptyList()
        coEvery { mockkDao.isFavourite(any()) } returns true

        wizardRepository = WizardRepositoryImpl(
            api = mockkApi,
            dao = mockkDao,
            elixirRepository = mockkElixirRepository
        )

        val result = wizardRepository.getWizardById(wizard.id)

        result.collect { nullableWizard ->
            Assertions.assertNotNull(nullableWizard)
        }

        coVerify { mockkDao.getById(wizard.id) }
        coVerify(exactly = 1) { mockkDao.isFavourite(any()) }
        coVerify(exactly = 1) { mockkElixirRepository.getWizardLightElixirs(any()) }
    }

    @Test
    fun `test save favourite wizard`() = runTest {
        val wizard = wizardsEntityList.first()

        every { mockkDao.getById(wizard.id) } returns flowOf(wizard)
        coEvery { mockkDao.saveFavourite(FavouriteWizardEntity(wizard.id)) } just Runs

        wizardRepository = WizardRepositoryImpl(
            api = mockkApi,
            dao = mockkDao,
            elixirRepository = mockkElixirRepository
        )

        wizardRepository.toggleFavorite(wizard.id)

        coVerify { mockkDao.getById(wizard.id) }
        coVerify(exactly = 1) { mockkDao.saveFavourite(any()) }
    }

    @Test
    fun `test remove favourite wizard`() = runTest {
        val wizard = wizardsEntityList.first()

        coEvery { mockkDao.removeFavourite(wizard.id) } just Runs

        wizardRepository = WizardRepositoryImpl(
            api = mockkApi,
            dao = mockkDao,
            elixirRepository = mockkElixirRepository
        )

        wizardRepository.removeFavorite(wizard.id)

        coVerify(exactly = 1) { mockkDao.removeFavourite(any()) }
    }
}
