package com.daajeh.wizardworldapp.presentation.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.daajeh.wizardworldapp.data.network.NetworkStatusProvider
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.domain.entity.Wizard
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
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
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    private val wizardsList = listOf(
        Wizard(id = "1", firstName = "Harry", lastName = "Potter"),
        Wizard(id = "2", firstName = "Hermione", lastName = "Granger")
    )

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
            networkStatusProvider, wizardRepository
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
        every { wizardRepository.getWizards() } returns flowOf(wizardsList)
        coEvery { networkStatusProvider.isNetworkAvailable() } returns false

        viewModel = WizardsViewModel(
            networkStatusProvider, wizardRepository
        )
        backgroundScope.launch(UnconfinedTestDispatcher(dispatcher.scheduler)) {
            viewModel.wizards.collect {}
        }

        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.wizards.value.isNotEmpty())
        assertNull(viewModel.error.value)
    }
}
