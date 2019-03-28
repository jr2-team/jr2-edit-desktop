package ru.jr2.edit.presentation.base.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.javafx.JavaFx
import tornadofx.ViewModel
import kotlin.coroutines.CoroutineContext

abstract class CoroutineViewModel : ViewModel(), CoroutineScope {
    private val viewModelJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = viewModelJob + Dispatchers.JavaFx

    internal fun cancelJob() = viewModelJob.cancelChildren()
}