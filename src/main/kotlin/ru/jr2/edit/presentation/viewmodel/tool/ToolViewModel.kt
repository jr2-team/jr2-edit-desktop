package ru.jr2.edit.presentation.viewmodel.tool

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.jr2.edit.domain.usecase.ParseWordEdictUseCase
import ru.jr2.edit.presentation.viewmodel.CoroutineViewModel
import tornadofx.getValue
import tornadofx.onChange
import tornadofx.setValue
import java.io.File

class ToolViewModel(
    private val wordParsingUseCase: ParseWordEdictUseCase = ParseWordEdictUseCase()
) : CoroutineViewModel() {
    val pIsBusy = SimpleBooleanProperty(false)
    val pWordEdictProcessingState = SimpleStringProperty("Выбирете Edict файл для обработки")

    private var isBusy by pIsBusy

    init {
        wordParsingUseCase.pParseStateMsg.onChange {
            pWordEdictProcessingState.value = it
        }
    }

    fun onWordEdictFileChoose(files: List<File>) {
        if (files.isNotEmpty()) parseEdictFile(files.first())
    }

    fun onCancelProcessingClick() {
        cancelJob()
        pWordEdictProcessingState.value = "Обработка была отменена"
    }

    private fun parseEdictFile(edictFile: File) = launch {
        isBusy = true
        if (!edictFile.exists()) throw CancellationException()
        withLoader {
            wordParsingUseCase.parseEdictAndSaveToDb(edictFile)
        }
    }.invokeOnCompletion {
        pWordEdictProcessingState.value = when (it) {
            is CancellationException -> "Не удалось открыть файл по выбранному пути"
            null -> "Готово!"
            else -> "Произошла ошибка во время обработки файла"
        }
        isBusy = false
    }

    private suspend fun withLoader(f: suspend () -> Unit) = with(coroutineContext) {
        val loader = launch {
            var iter = 0
            repeat(Int.MAX_VALUE) {
                if (iter > 3) {
                    pWordEdictProcessingState.value =
                            pWordEdictProcessingState.value.removeSuffix("...")
                }
                pWordEdictProcessingState.value += '.'
                iter++
                delay(200L)
            }
        }
        f.invoke()
        loader.cancelAndJoin()
    }
}