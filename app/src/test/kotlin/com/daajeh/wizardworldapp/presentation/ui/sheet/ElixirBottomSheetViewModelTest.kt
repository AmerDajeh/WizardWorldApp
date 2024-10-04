package com.daajeh.wizardworldapp.presentation.ui.sheet

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
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class ElixirBottomSheetViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ElixirBottomSheetViewModel

    @MockK
    private lateinit var elixirRepository: ElixirRepository

    @MockK
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

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
        every { savedStateHandle.get<String>(any()) } returns elixir.id
        every { elixirRepository.getElixirById(elixir.id) } returns flowOf(null)
        coEvery { networkStatusProvider.isNetworkAvailable() } returns false

        viewModel = ElixirBottomSheetViewModel(
            elixirRepository = elixirRepository,
            networkStatusProvider = networkStatusProvider,
            savedStateHandle = savedStateHandle
        )

        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.scheduler)) {
            viewModel.elixir.collect {}
        }

        dispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.elixir.value)
        assertNotNull(viewModel.error.value)
    }

    @Test
    fun testNetworkStatusCheckNOErrorWhenOfflineAndCacheAvailable() = runTest {
        every { savedStateHandle.get<String>(any()) } returns elixir.id
        every { elixirRepository.getElixirById(elixir.id) } returns flowOf(elixir)
        coEvery { networkStatusProvider.isNetworkAvailable() } returns false

        viewModel = ElixirBottomSheetViewModel(
            elixirRepository = elixirRepository,
            networkStatusProvider = networkStatusProvider,
            savedStateHandle = savedStateHandle
        )

        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.scheduler)) {
            viewModel.elixir.collect {}
        }

        dispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.elixir.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun testLoadElixir() = runTest {
        every { elixirRepository.getElixirById(elixir.id) } returns flowOf(elixir)
        every { savedStateHandle.get<String>(any()) } returns elixir.id
        every { savedStateHandle[any()] = any<String>() } just Runs

        viewModel = ElixirBottomSheetViewModel(
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
    }



    @Test
    fun testSaveFavouriteElixir() = runTest {
        val elixir = elixir
        every { savedStateHandle.get<String>(any()) } returns elixir.id
        every { elixirRepository.getElixirById(elixir.id) } returns flowOf(elixir)
        coEvery { elixirRepository.toggleFavourite(any()) } just Runs

        viewModel = ElixirBottomSheetViewModel(
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
        every { savedStateHandle.get<String>(any()) } returns elixir.id
        every { elixirRepository.getElixirById(elixir.id) } returns flowOf(elixir)
        coEvery { elixirRepository.toggleFavourite(any()) } just Runs

        viewModel = ElixirBottomSheetViewModel(
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
}
