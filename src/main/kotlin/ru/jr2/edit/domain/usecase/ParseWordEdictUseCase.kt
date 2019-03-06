package ru.jr2.edit.domain.usecase

import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.data.editc.mapping.WordDictionary
import ru.jr2.edit.data.editc.mapping.WordEdictEntry
import ru.jr2.edit.data.editc.repository.EdictParserRepository
import ru.jr2.edit.domain.model.WordModel
import ru.jr2.edit.util.pmap
import java.io.File

class ParseWordEdictUseCase(
    private val wordDbRepository: WordDbRepository = WordDbRepository(),
    private val wordEdictRepository: EdictParserRepository = EdictParserRepository()
) {
    val pParseStateMsg = SimpleStringProperty(String())

    suspend fun parseEdictAndSaveToDb(edictFile: File) = coroutineScope {
        changeStateMsg("Извлечение слов из файла")
        val wordEntries = wordEdictRepository.getEdictEntries<WordDictionary, WordEdictEntry>(edictFile)
        changeStateMsg("Обработка слов")
        val words = wordEntries.pmap { transformEntry(it) }
        changeStateMsg("Запись слов в БД")
        wordDbRepository.insertAll(words)
    }

    // TODO: Подстроить схему БД под JMdict более точно
    private fun transformEntry(wordEdictEntry: WordEdictEntry) = WordModel().apply {
        word = wordEdictEntry.kanjiElements
            ?.first()
            ?.reading ?: wordEdictEntry.readingElements
            ?.first()
            ?.reading ?: String()
        furigana = wordEdictEntry.readingElements?.first()?.reading ?: String()
        val glossaries = wordEdictEntry.senses
            ?.filter { it.glossaryEntries != null }
            ?.flatMap { it.glossaryEntries!! }
        val rusInterpretations = glossaries
            ?.filter { it.language == "rus" }
            ?.mapIndexed { idx, it -> "${idx + 1} ${it.definition}\n" }
            ?.joinToString("") { it }
        val engInterpretations = glossaries
            ?.filter { it.language == "eng" }
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