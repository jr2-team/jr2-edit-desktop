package ru.jr2.edit.domain.usecase

import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import ru.jr2.edit.data.db.repository.KanjiDbRepository
import ru.jr2.edit.data.editc.mapping.KanjiEdictEntry
import ru.jr2.edit.data.editc.repository.KanjiEdictRepository
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.util.pmap
import java.io.File

class ParseKanjiUseCase(
    private val kanjiDbRepository: KanjiDbRepository = KanjiDbRepository(),
    private val kanjiEdictRepository: KanjiEdictRepository = KanjiEdictRepository()
) {
    val pParseStateMsg = SimpleStringProperty(String())

    suspend fun parseEdictAndSaveToDb(edictFile: File) = coroutineScope {
        changeStateMsg("Получение канджи из файла")
        val kanjiEntries = kanjiEdictRepository.getKanjiEntriesFromFile(edictFile)
        changeStateMsg("Обработка канджи")
        val kanjis = kanjiEntries.pmap { transformEntry(it) }
        changeStateMsg("Запись канджи в БД")
        kanjiDbRepository.insertUpdate(kanjis)
    }

    private fun transformEntry(kanjiEdictEntry: KanjiEdictEntry) = Kanji().apply {
        kanji = kanjiEdictEntry.literal
        strokeCount = kanjiEdictEntry.misc.strokeCount
        onReading = kanjiEdictEntry.readingMeaning?.rmgroup?.reading?.filter {
            it.type == "ja_on"
        }?.joinToString { it.value } ?: ""
        kunReading = kanjiEdictEntry.readingMeaning?.rmgroup?.reading?.filter {
            it.type == "ja_kun"
        }?.joinToString { it.value } ?: ""
        interpretation = kanjiEdictEntry.readingMeaning?.rmgroup?.meaning?.filter {
            it.language.isEmpty()
        }?.joinToString { it.value } ?: ""
        jlptLevel = JlptLevel.fromCode(kanjiEdictEntry.misc.jlpt).str
    }

    private suspend fun changeStateMsg(msg: String) {
        pParseStateMsg.value = msg
        delay(100L)
    }
}