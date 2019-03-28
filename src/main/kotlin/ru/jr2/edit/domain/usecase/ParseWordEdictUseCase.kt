package ru.jr2.edit.domain.usecase

import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.jr2.edit.data.editc.mapping.WordDictionary
import ru.jr2.edit.data.editc.mapping.WordEdictEntry
import ru.jr2.edit.data.editc.repository.EdictParserRepository
import ru.jr2.edit.presentation.word.model.WordInterpretationModel
import ru.jr2.edit.presentation.word.model.WordModel
import ru.jr2.edit.util.JlptLevel
import java.io.File

class ParseWordEdictUseCase(
    private val wordDbUseCase: WordDbUseCase = WordDbUseCase(),
    private val wordEdictRepository: EdictParserRepository = EdictParserRepository()
) {
    val pParseStateMsg = SimpleStringProperty(String())

    suspend fun parseEdictAndSaveToDb(edictFile: File) = coroutineScope {
        changeStateMsg("Извлечение слов из файла")
        val wordEntries = wordEdictRepository.getEdictEntries<WordDictionary, WordEdictEntry>(edictFile)
        changeStateMsg("Обработка слов")
        changeStateMsg("Запись слов в БД")
        withContext(Dispatchers.IO) {
            wordDbUseCase.saveParsedWordsWithInterpretations(wordEntries.map { transformEntry(it) })
        }
    }

    private fun transformEntry(
        wordEdictEntry: WordEdictEntry
    ): Pair<WordModel, List<WordInterpretationModel>?> {
        val word = WordModel().apply {
            word = wordEdictEntry.kanjiElements
                ?.first()
                ?.reading ?: wordEdictEntry.readingElements
                ?.first()
                ?.reading ?: String()
            furigana = wordEdictEntry.readingElements?.first()?.reading ?: String()
            jlptLevel = JlptLevel.fromCode(0).str
        }

        val wordInterpretations = mutableListOf<WordInterpretationModel>()
        if (wordEdictEntry.senses != null) {
            for (sense in wordEdictEntry.senses) {
                val pos = sense.partsOfSpeach?.joinToString()
                sense.glossaryEntries?.map {
                    WordInterpretationModel().apply {
                        interpretation = it.definition
                        this.pos = pos ?: String()
                        language = it.language ?: String()
                    }
                }?.let { interpretations ->
                    wordInterpretations.addAll(interpretations)
                }
            }
        }
        return Pair(word, wordInterpretations)
    }

    private suspend fun changeStateMsg(msg: String) {
        pParseStateMsg.value = msg
        delay(100L)
    }
}