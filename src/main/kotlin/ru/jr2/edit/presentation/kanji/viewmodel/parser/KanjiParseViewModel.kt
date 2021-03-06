package ru.jr2.edit.presentation.kanji.viewmodel.parser

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.*
import ru.jr2.edit.domain.usecase.ParseKanjiEdictUseCase
import ru.jr2.edit.presentation.base.viewmodel.CoroutineViewModel
import ru.jr2.edit.util.withLoader
import tornadofx.getValue
import tornadofx.onChange
import tornadofx.setValue
import java.io.File

class KanjiParseViewModel(
    private val kanjiParsingEdictUseCase: ParseKanjiEdictUseCase = ParseKanjiEdictUseCase()
) : CoroutineViewModel() {
    val pIsBusy = SimpleBooleanProperty(false)
    val pProcessingStateMsg = SimpleStringProperty("Выбирете Edict файл для обработки")

    private var isBusy by pIsBusy
    private var processingStateMsg by pProcessingStateMsg

    init {
        kanjiParsingEdictUseCase.pParseStateMsg.onChange {
            processingStateMsg = it
        }
    }

    fun onKanjiEdictFileChoose(files: List<File>) {
        if (files.isNotEmpty()) {
            parseKanjiEdictFile(files.first())
            processingStateMsg = files.first().name
        }
    }

    fun onCancelProcessingClick() = cancelJob()

    private fun parseKanjiEdictFile(edictFile: File) = launch {
        isBusy = true
        withLoader(getLoader()) {
            kanjiParsingEdictUseCase.parseEdictAndSaveToDb(edictFile)
        }
    }.invokeOnCompletion {
        handelCompletion(it)
        isBusy = false
    }

    private fun getLoader(): Job = launch {
        var iter = 0
        repeat(Int.MAX_VALUE) {
            if (iter > 3) {
                processingStateMsg = processingStateMsg.removeSuffix("...")
            }
            processingStateMsg += '.'
            iter++
            delay(200L)
        }
    }

    private fun handelCompletion(throwable: Throwable?) {
        processingStateMsg = when (throwable) {
            is CancellationException -> "Обработка была отменена"
            null -> "Готово!"
            else -> {
                coroutineContext.cancelChildren()
                "Произошла ошибка во время обработки файла"
            }
        }
    }
}