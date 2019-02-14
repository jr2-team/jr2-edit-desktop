package ru.jr2.edit.domain.usecase

import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.data.editc.mapping.WordEdictEntry
import ru.jr2.edit.data.editc.repository.WordEdictRepository
import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.util.pmap
import java.io.File

class ParseWordEdictUseCase(
    private val wordDbRepository: WordDbRepository = WordDbRepository(),
    private val wordEdictRepository: WordEdictRepository = WordEdictRepository()
) {
    val pParseStateMsg = SimpleStringProperty(String())

    suspend fun parseEdictAndSaveToDb(edictFile: File) = coroutineScope {
        changeStateMsg("Извлечение вхождений слов из файла")
        val wordEntries = wordEdictRepository.getWordEntriesFromFile(edictFile)
        changeStateMsg("Обработка слов")
        // Не могу понять, на солько pmap действительно работает
        val words = wordEntries.pmap { transformEntry(it) }
        changeStateMsg("Запись слов в БД")
        withContext(Dispatchers.Default) { wordDbRepository.insertAll(words) }
    }

    // TODO: Подстроить схему БД под JMdict более точно
    private fun transformEntry(wordEdictEntry: WordEdictEntry) = Word().apply {
        value = wordEdictEntry.kanjiElements
            ?.first()
            ?.reading ?: wordEdictEntry.readingElements
            ?.first()
            ?.reading ?: String()
        furigana = wordEdictEntry.readingElements?.first()?.reading ?: String()
        val glossaries = wordEdictEntry.senses
            ?.filter { it.glossaryEntries != null }
            ?.flatMap { it.glossaryEntries!! }

        val rusInterpretations = glossaries?.filter {
            it.language == "rus"
        }
            ?.mapIndexed { idx, it -> "${idx + 1} ${it.definition}\n" }
            ?.joinToString("") { it }
        val engInterpretations = glossaries?.filter {
            it.language == "eng"
        }
            ?.mapIndexed { idx, it -> "${idx + 1} ${it.definition}\n" }
            ?.joinToString("") { it }
        interpretation = if (rusInterpretations.isNullOrBlank()) {
            engInterpretations ?: String()
        } else {
            rusInterpretations
        }
    }

    private suspend fun changeStateMsg(msg: String) {
        pParseStateMsg.value = msg
        delay(100L)
    }
}