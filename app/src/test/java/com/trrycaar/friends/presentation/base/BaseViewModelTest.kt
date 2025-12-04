package com.trrycaar.friends.presentation.base

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var viewModel: FakeViewModel

    @BeforeTest
    fun setup() {
        viewModel = FakeViewModel(dispatcher = dispatcher)
    }

    @Test
    fun `updateState SHOULD change state`() = runTest(dispatcher) {
        viewModel.setState("new value")
        assertEquals("new value", viewModel.state.value)
    }


    @Test
    fun `tryToExecute SHOULD update state on success`() = runTest(dispatcher) {
        viewModel.launchSuccess()
        advanceUntilIdle()
        assertEquals("success", viewModel.state.value)
    }

    @Test
    fun `tryToExecute SHOULD update state on error`() = runTest(dispatcher) {
        viewModel.launchFailure()
        advanceUntilIdle()
        assertEquals("error:boom", viewModel.state.value)
    }

    @Test
    fun `emitEffect SHOULD emit effect`() = runTest(dispatcher) {
        viewModel.effect.test {
            viewModel.sendEffect()
            assertEquals("effect", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}