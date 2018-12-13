@file:Suppress("NOTHING_TO_INLINE")

package womenproject.com.mybury.util

import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

@Suppress("unused")
class LifecycleUtil

/**
 * 현재 [state] 상태이면 [function]을 즉시 실행하고,
 * 현재 [state] 상태가 아닐 경우 다음 [state] 상태에서 [function]을 1회 실행한다.
 */
inline fun LifecycleOwner.runOnState(state: Lifecycle.State, crossinline function: () -> Unit) {
    runOnState({ it.isAtLeast(state) }, function)
}

/**
 * 현재 [predicate]를 만족하면, [function]을 실행한다.
 * 현재 [predicate]를 만족하지 않으면, [predicate]를 만족하는 상태일 때 [function]을  1회 실행한다.
 */
inline fun LifecycleOwner.runOnState(crossinline predicate: (Lifecycle.State) -> Boolean, crossinline function: () -> Unit) {
    if (predicate(lifecycle.currentState)) {
        function()
    } else {
        lifecycle.addObserver(object : GenericLifecycleObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                val lifecycle = source.lifecycle
                if (predicate(lifecycle.currentState)) {
                    function()
                    lifecycle.removeObserver(this)
                } else if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
                    lifecycle.removeObserver(this)
                }
            }
        })
    }
}

/**
 * 다음 [state] 상태에서 [function]을 1회 실행한다.
 */
inline fun LifecycleOwner.runOnNextState(state: Lifecycle.State, crossinline function: () -> Unit): LifecycleObserver {
    val observer = object : GenericLifecycleObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            source.lifecycle.also { lifecycle ->
                if (lifecycle.currentState == state) {
                    function()
                    lifecycle.removeObserver(this)
                } else if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
                    lifecycle.removeObserver(this)
                }
            }
        }
    }
    lifecycle.addObserver(observer)
    return observer
}

/**
 * [state] 상태마다 [function]을 실행한다.
 */
inline fun LifecycleOwner.onState(state: Lifecycle.State, crossinline function: () -> Unit): LifecycleObserver {
    val observer = object : GenericLifecycleObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            source.lifecycle.also { lifecycle ->
                if (lifecycle.currentState == state) {
                    function()
                }
                if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
                    lifecycle.removeObserver(this)
                }
            }
        }
    }
    lifecycle.addObserver(observer)
    return observer
}
