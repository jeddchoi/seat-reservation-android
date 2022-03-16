/**
 * see https://medium.com/prnd/mvvm%EC%9D%98-viewmodel%EC%97%90%EC%84%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8%EB%A5%BC-%EC%B2%98%EB%A6%AC%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95-6%EA%B0%80%EC%A7%80-31bb183a88ce
 */

package jed.choi.ui_core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.atomic.AtomicBoolean


interface EventFlow<out T> : Flow<T> {
    companion object {
        const val DEFAULT_REPLAY: Int = 3
    }
}

interface MutableEventFlow<T> : EventFlow<T>, FlowCollector<T>

@Suppress("FunctionName")
fun <T> MutableEventFlow(
    replay: Int = EventFlow.DEFAULT_REPLAY
): MutableEventFlow<T> = EventFlowImpl(replay)

fun <T> MutableEventFlow<T>.asEventFlow(): EventFlow<T> = ReadOnlyEventFlow(this)

private class ReadOnlyEventFlow<T>(flow: EventFlow<T>) : EventFlow<T> by flow

private class EventFlowImpl<T>(
    replay: Int
) : MutableEventFlow<T> {

    private val flow: MutableSharedFlow<EventFlowSlot<T>> = MutableSharedFlow(replay = replay)

    override suspend fun collect(collector: FlowCollector<T>) = flow
        .collect { slot ->
            if (!slot.markConsumed()) {
                collector.emit(slot.value)
            }
        }

    override suspend fun emit(value: T) {
        flow.emit(EventFlowSlot(value))
    }
}

private class EventFlowSlot<T>(val value: T) {

    private val consumed: AtomicBoolean = AtomicBoolean(false)

    fun markConsumed(): Boolean = consumed.getAndSet(true)
}