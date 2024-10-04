package com.daajeh.wizardworldapp.presentation.ui.details

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
class WizardDetailsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: WizardDetailsViewModel

    @MockK
    private lateinit var wizardRepository: WizardRepository

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
    fun testLoadWizardWizardWizard() = runTest {
        val wizard = wizardsList.first()
        every { wizardRepository.getWizards() } returns flowOf(emptyList())
        every { savedStateHandle.getStateFlow(any(), "") } returns MutableStateFlow(wizard.id)
        every { wizardRepository.getWizardById(wizard.id) } returns flowOf(wizard)
        every { savedStateHandle[any()] = any<String>() } just Runs

        viewModel = WizardDetailsViewModel(
            wizardRepository = wizardRepository,
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
    }


    @Test
    fun testSaveFavouriteWizard() = runTest {
        val wizard = wizardsList.first()
        every { wizardRepository.getWizards() } returns flowOf(emptyList())
        every { savedStateHandle.getStateFlow(any(), "") } returns MutableStateFlow(wizard.id)
        every { wizardRepository.getWizardById(wizard.id) } returns flowOf(wizard)
        coEvery { wizardRepository.toggleFavorite(any()) } just Runs

        viewModel = WizardDetailsViewModel(
            wizardRepository = wizardRepository,
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

        viewModel = WizardDetailsViewModel(
            wizardRepository = wizardRepository,
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
}
