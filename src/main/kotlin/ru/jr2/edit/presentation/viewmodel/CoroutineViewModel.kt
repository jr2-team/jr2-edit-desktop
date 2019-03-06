package ru.jr2.edit.presentation.viewmodel

import kotlinx.coroutines.*
import tornadofx.ViewModel
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.javafx.JavaFx as Main

abstract class CoroutineViewModel : ViewModel(), CoroutineScope {
    private val viewModelJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = viewModelJob + Dispatchers.Main


    internal fun cancelJob() = viewModelJob.cancelChildren()
}