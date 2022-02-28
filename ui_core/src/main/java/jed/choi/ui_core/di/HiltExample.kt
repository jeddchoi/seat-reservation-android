package jed.choi.ui_core.di

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HiltExample @Inject constructor() {
    val counter = value
    init {
        value++
    }

    companion object {
        var value = 0
    }
}