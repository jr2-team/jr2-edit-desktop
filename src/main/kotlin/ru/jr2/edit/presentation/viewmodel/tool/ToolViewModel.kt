package ru.jr2.edit.presentation.viewmodel.tool

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.*
import ru.jr2.edit.domain.usecase.ParseKanjiUseCase
import ru.jr2.edit.domain.usecase.ParseWordEdictUseCase
import ru.jr2.edit.presentation.viewmodel.CoroutineViewModel
import tornadofx.getValue
import tornadofx.onChange
import tornadofx.setValue
import java.io.File

class ToolViewModel(
    private val wordParsingUseCase: ParseWordEdictUseCase = ParseWordEdictUseCase(),
    private val kanjiParsingUseCase: ParseKanjiUseCase = ParseKanjiUseCase()
) : CoroutineViewModel() {
    val pIsBusy = SimpleBooleanProperty(false)
    val pWordEdictProcessingState = SimpleStringProperty(String())

    val pWordEdictFilename = SimpleStringProperty("Выбирете Edict файл для обработки")
    val pKanjiEdictFilename = SimpleStringProperty("Выбирете Edict файл для обработки")

    private var isBusy by pIsBusy

    init {
        wordParsingUseCase.pParseStateMsg.onChange {
            pWordEdictProcessingState.value = it
        }
    }

    fun onWordEdictFileChoose(files: List<File>) {
        if (files.isNotEmpty()) {
            parseWordEdictFile(files.first())
            pWordEdictFilename.value = files.first().name
        }
    }

    fun onKanjiFileChoose(files: List<File>) {
        if (files.isNotEmpty()) {
            parseKanjiEdictFile(files.first())
            pKanjiEdictFilename.value = files.first().name
        }
    }

    fun onCancelProcessingClick() {
        cancelJob()
        pWordEdictProcessingState.value = "Обработка была отменена"
    }

    // TODO: Убрать ToolView, перенести функции парсеров на соотвествующие вкладки
    private fun parseWordEdictFile(edictFile: File) = launch {
        isBusy = true
        if (!edictFile.exists()) throw CancellationException()
        withLoader {
            wordParsingUseCase.parseEdictAndSaveToDb(edictFile)
        }
    }.invokeOnCompletion {
        handelCompletion(it)
        isBusy = false
    }

    private fun parseKanjiEdictFile(edictFile: File) = launch {
        isBusy = true
        if (!edictFile.exists()) throw CancellationException()
        withLoader {
            kanjiParsingUseCase.parseEdictAndSaveToDb(edictFile)
        }
    }.invokeOnCompletion {
        handelCompletion(it)
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

    private fun handelCompletion(throwable: Throwable?) {
        pWordEdictProcessingState.value = when (throwable) {
            is CancellationException -> "Не удалось открыть файл по выбранному пути"
            null -> "Готово!"
            else -> {
                coroutineContext.cancelChildren()
                "Произошла ошибка во время обработки файла"
            }
        }
    }
}