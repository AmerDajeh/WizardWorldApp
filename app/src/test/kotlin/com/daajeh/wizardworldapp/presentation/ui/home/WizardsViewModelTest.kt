package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.daajeh.wizardworldapp.data.network.NetworkStatusProvider
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.domain.entity.Elixir
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class WizardsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: WizardsViewModel

    @MockK
    private lateinit var wizardRepository: WizardRepository

    @MockK
    private lateinit var elixirRepository: ElixirRepository

    @MockK
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    private val wizardsList = listOf(
        Wizard(id = "1", firstName = "Harry", lastName = "Potter"),
        Wizard(id = "2", firstName = "Hermione", lastName = "Granger")
    )

    private val elixir = Elixir(id = "1")

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        clearAllMocks()
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testNetworkStatusCheckSetsErrorWhenOfflineAndNoCacheAvailable() = runTest {
        every { savedStateHandle.getStateFlow(any(), "") } returns MutableStateFlow("")
        every { wizardRepository.getWizards() } returns flowOf(emptyList())
        coEvery { networkStatusProvider.isNetworkAvailable() } returns false

        viewModel = WizardsViewModel(
            wizardRepository = wizardRepository,
            elixirRepository = elixirRepository,
            networkStatusProvider = networkStatusProvider,
            savedStateHandle = savedStateHandle
        )

        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.scheduler)) {
            viewModel.wizards.collect {}
        }

        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.wizards.value.isEmpty())
        assertNotNull(viewModel.error.value)
    }

    @Test
    fun testNetworkStatusCheckNOErrorWhenOfflineAndCacheAvailable() = runTest {
        every { savedStateHandle.getStateFlow(any(), "") } returns MutableStateFlow("")
        every { wizardRepository.getWizards() } returns flowOf(wizardsList)
        coEvery { networkStatusProvider.isNetworkAvailable() } returns false

        viewModel = WizardsViewModel(
            wizardRepository = wizardRepository,
            elixirRepository = elixirRepository,
            networkStatusProvider = networkStatusProvider,
            savedStateHandle = savedStateHandle
        )

        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.scheduler)) {
            viewModel.wizards.collect {}
        }

        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.wizards.value.isNotEmpty())
        assertEquals(null, viewModel.error.value)
    }

    @Test
    fun testLoadWizardWizardWizard() = runTest {
        val wizard = wizardsList.first()
        every { wizardRepository.getWizards() } returns flowOf(emptyList())
        every { savedStateHandle.getStateFlow(any(), "") } returns MutableStateFlow(wizard.id)
        every { wizardRepository.getWizardById(wizard.id) } returns flowOf(wizard)
        every { savedStateHandle[any()] = any<String>() } just Runs

        viewModel = WizardsViewModel(
            wizardRepository = wizardRepository,
            elixirRepository = elixirRepository,
            networkStatusProvider = networkStatusProvider,
            savedStateHandle = savedStateHandle
        )

        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.scheduler)) {
            viewModel.wizard.collect {
                println(it)
            }
        }
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.loadWizard(wizard.id)

        assertNotNull(viewModel.wizard.value)
        coVerify { wizardRepository.getWizardById(wizard.id) }
    }

    @Test
    fun testLoadWizardElixir() = runTest {
        every { wizardRepository.getWizards() } returns flowOf(emptyList())
        every { savedStateHandle.getStateFlow(any(), "") } returns MutableStateFlow(elixir.id)
        every { elixirRepository.getElixirById(elixir.id) } returns flowOf(elixir)
        every { savedStateHandle[any()] = any<String>() } just Runs

        viewModel = WizardsViewModel(
            wizardRepository = wizardRepository,
            elixirRepository = elixirRepository,
            networkStatusProvider = networkStatusProvider,
            savedStateHandle = savedStateHandle
        )

        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.scheduler)) {
            viewModel.elixir.collect {
                println(it)
            }
        }
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.loadElixir(elixir.id)
        coVerify { elixirRepository.getElixirById(elixir.id) }

        assertNotNull(viewModel.elixir.value)
    }


    @Test
    fun testSaveFavouriteWizard() = runTest {
        val wizard = wizardsList.first()
        every { wizardRepository.getWizards() } returns flowOf(emptyList())
        every { savedStateHandle.getStateFlow(any(), "") } returns MutableStateFlow(wizard.id)
        every { wizardRepository.getWizardById(wizard.id) } returns flowOf(wizard)
        coEvery { wizardRepository.toggleFavorite(any()) } just Runs

        viewModel = WizardsViewModel(
            wizardRepository = wizardRepository,
            elixirRepository = elixirRepository,
            networkStatusProvider = networkStatusProvider,
            savedStateHandle = savedStateHandle
        )

        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.scheduler)) {
            viewModel.wizard.collect {
                println(it)
            }
        }

        dispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.wizard.value)
        coVerify { wizardRepository.getWizardById(wizard.id) }
        viewModel.toggleWizardFavouriteState()
        dispatcher.scheduler.advanceUntilIdle()
        coVerify { wizardRepository.toggleFavorite(wizard.id) }
    }

    @Test
    fun testRemoveFavouriteWizard() = runTest {
        val wizard = wizardsList.first().copy(isFavorite = true)
        every { wizardRepository.getWizards() } returns flowOf(emptyList())
        every { savedStateHandle.getStateFlow(any(), "") } returns MutableStateFlow(wizard.id)
        every { wizardRepository.getWizardById(wizard.id) } returns flowOf(wizard)
        coEvery { wizardRepository.removeFavorite(any()) } just Runs

        viewModel = WizardsViewModel(
            wizardRepository = wizardRepository,
            elixirRepository = elixirRepository,
            networkStatusProvider = networkStatusProvider,
            savedStateHandle = savedStateHandle
        )

        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.scheduler)) {
            viewModel.wizard.collect {
                println(it)
            }
        }

        dispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.wizard.value)
        coVerify { wizardRepository.getWizardById(wizard.id) }
        viewModel.toggleWizardFavouriteState()
        dispatcher.scheduler.advanceUntilIdle()
        coVerify { wizardRepository.removeFavorite(wizard.id) }
    }

    @Test
    fun testSaveFavouriteElixir() = runTest {
        val elixir = elixir
        every { wizardRepository.getWizards() } returns flowOf(emptyList())
        every { savedStateHandle.getStateFlow(any(), "") } returns MutableStateFlow(elixir.id)
        every { elixirRepository.getElixirById(elixir.id) } returns flowOf(elixir)
        coEvery { elixirRepository.toggleFavourite(any()) } just Runs

        viewModel = WizardsViewModel(
            wizardRepository = wizardRepository,
            elixirRepository = elixirRepository,
            networkStatusProvider = networkStatusProvider,
            savedStateHandle = savedStateHandle
        )

        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.scheduler)) {
            viewModel.elixir.collect {
                println(it)
            }
        }
        dispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.elixir.value)
        coVerify { elixirRepository.getElixirById(elixir.id) }

        viewModel.toggleElixirFavouriteState()
        dispatcher.scheduler.advanceUntilIdle()

        coVerify { elixirRepository.toggleFavourite(elixir.id) }
    }

    @Test
    fun testRemoveFavouriteElixir() = runTest {
        val elixir = elixir.copy(isFavorite = true)
        every { wizardRepository.getWizards() } returns flowOf(emptyList())
        every { savedStateHandle.getStateFlow(any(), "") } returns MutableStateFlow(elixir.id)
        every { elixirRepository.getElixirById(elixir.id) } returns flowOf(elixir)
        coEvery { elixirRepository.removeFavorite(any()) } just Runs

        viewModel = WizardsViewModel(
            wizardRepository = wizardRepository,
            elixirRepository = elixirRepository,
            networkStatusProvider = networkStatusProvider,
            savedStateHandle = savedStateHandle
        )

        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.scheduler)) {
            viewModel.elixir.collect {
                println(it)
            }
        }

        dispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.elixir.value)
        coVerify { elixirRepository.getElixirById(elixir.id) }
        viewModel.toggleElixirFavouriteState()

        dispatcher.scheduler.advanceUntilIdle()
        coVerify { elixirRepository.removeFavorite(elixir.id) }
    }
}
