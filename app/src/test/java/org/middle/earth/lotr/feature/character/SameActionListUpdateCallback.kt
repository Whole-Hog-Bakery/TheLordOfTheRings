package org.middle.earth.lotr.feature.character

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.withTimeout

private class SameActionListUpdateCallback(
    private val onChange: () -> Unit
) : ListUpdateCallback {

    override fun onInserted(position: Int, count: Int) {
        onChange()
    }

    override fun onRemoved(position: Int, count: Int) {
        onChange()
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        onChange()
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        onChange()
    }
}

private class NoDiffCallback<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean = false
    override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any): Boolean = false
}

@ExperimentalCoroutinesApi
suspend fun <T : Any> Flow<PagingData<T>>.testPages(
    validate: suspend Librarian<T>.() -> Unit
) {
    this.test {
        validate(TurbineLibrarian(this))
    }
}

interface Librarian<T : Any> {

    suspend fun awaitPages(testScope: TestScope): Pagination<T>

    suspend fun ignoreRemaining()

    suspend fun awaitNoMore()
}

@ExperimentalCoroutinesApi
private class TurbineLibrarian<T : Any>(
    private val turbine: ReceiveTurbine<PagingData<T>>
) : Librarian<T> {

    override suspend fun awaitPages(testScope: TestScope): Pagination<T> =
        turbine.awaitItem().toPagination(testScope)

    override suspend fun ignoreRemaining(): Unit = turbine.cancelAndIgnoreRemainingEvents()

    override suspend fun awaitNoMore(): Unit = turbine.expectNoEvents()
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun <T : Any> PagingData<T>.toPagination(testScope: TestScope): Pagination<T> {
    val pages: MutableList<List<T>> = mutableListOf()
    var currentPage: MutableList<T> = mutableListOf()

    val currentPosition = MutableStateFlow(0)
    val updateCallback = SameActionListUpdateCallback {
        pages.add(currentPage)
        currentPosition.value = currentPosition.value + currentPage.size
        currentPage = mutableListOf()
    }

    val differ = AsyncPagingDataDiffer<T>(
        diffCallback = NoDiffCallback(),
        updateCallback = updateCallback,
        mainDispatcher = Dispatchers.Main,
        workerDispatcher = Dispatchers.Default
    )

    currentPosition.filter { it > 0 }
        .onEach { differ.getItem(it - 1) }
        .launchIn(testScope)

    try {
        withTimeout(5) {
            differ.submitData(this@toPagination.onEach { currentPage.add(it) })
        }
    } catch (e: TimeoutCancellationException) {
        // Ignore exception we just need it in order to stop
        // the underlying implementation blocking the main thread
    }

    return Pagination(pages = pages)
}

private fun <T : Any> PagingData<T>.onEach(action: (T) -> Unit): PagingData<T> = this.map {
    action(it)
    it
}

data class Pagination<T : Any>(private val pages: MutableList<List<T>>) {

    val isEmpty: Boolean = pages.isEmpty()

    val count: Int = pages.count()

    fun first(): List<T> = pages.first()

    fun last(): List<T> = pages.last()

    fun pageAt(position: Int): List<T> = pages[position]

    fun loadedAt(point: Int): List<T> = pages.take(point).flatten()

    fun fullyLoaded(): List<T> = pages.flatten()
}