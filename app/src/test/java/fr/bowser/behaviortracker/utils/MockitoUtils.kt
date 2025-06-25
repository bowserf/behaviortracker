package fr.bowser.behaviortracker.utils

import org.mockito.Mockito

object MockitoUtils {

    fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}
