package ru.jr2.edit.domain.usecase

import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import ru.jr2.edit.data.editc.mapping.KanjiEdictEntry
import ru.jr2.edit.data.editc.repository.KanjiEdictRepository
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.domain.model.KanjiReading
import ru.jr2.edit.util.pmap
import java.io.File

class ParseKanjiUseCase(
    private val kanjiUseCase: KanjiUseCase = KanjiUseCase(),
    private val kanjiEdictRepository: KanjiEdictRepository = KanjiEdictRepository()
) {
    val pParseStateMsg = SimpleStringProperty(String())

    suspend fun parseEdictAndSaveToDb(edictFile: File) = coroutineScope {
        changeStateMsg("Получение канджи из файла")
        val kanjiEntries = kanjiEdictRepository.getKanjiEntriesFromFile(edictFile)
        changeStateMsg("Обработка канджи")
        val kanjisWithKanjiReadings = kanjiEntries.pmap { transformEntry(it) }
        changeStateMsg("Запись канджи в БД")
        kanjiUseCase.fastKanjiWithReadingsInsertion(kanjisWithKanjiReadings)
    }

    private fun transformEntry(kanjiEdictEntry: KanjiEdictEntry): Pair<Kanji, List<KanjiReading>?> {
        val kanji = Kanji().apply {
            kanji = kanjiEdictEntry.literal
            strokeCount = kanjiEdictEntry.misc.strokeCount
            interpretation = kanjiEdictEntry.readingMeaning?.rmgroup?.meanings?.filter {
                it.language.isEmpty()
            }?.joinToString { it.value }
            jlptLevel = JlptLevel.fromCode(kanjiEdictEntry.misc.jlpt).str
        }

        val kanjiReadings = kanjiEdictEntry.readingMeaning?.rmgroup?.readings?.filter {
            it.type == "ja_on" || it.type == "ja_kun"
        }?.map {
            KanjiReading().apply {
                reading = it.value
                readingType = if (it.type == "ja_on") 0 else 1
            }
        }
        return Pair(kanji, kanjiReadings)
    }

    private suspend fun changeStateMsg(msg: String) {
        pParseStateMsg.value = msg
        delay(100L)
    }
}