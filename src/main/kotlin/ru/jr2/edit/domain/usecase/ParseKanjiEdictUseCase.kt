package ru.jr2.edit.domain.usecase

import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import ru.jr2.edit.data.editc.mapping.KanjiDictionary
import ru.jr2.edit.data.editc.mapping.KanjiEdictEntry
import ru.jr2.edit.data.editc.repository.EdictParserRepository
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.misc.KanjiReadingType
import ru.jr2.edit.presentation.kanji.model.KanjiModel
import ru.jr2.edit.presentation.kanji.model.KanjiReadingModel
import java.io.File

class ParseKanjiEdictUseCase(
    private val kanjiDbUseCase: KanjiDbUseCase = KanjiDbUseCase(),
    private val kanjiEdictRepository: EdictParserRepository = EdictParserRepository()
) {
    val pParseStateMsg = SimpleStringProperty(String())

    suspend fun parseEdictAndSaveToDb(edictFile: File) = coroutineScope {
        changeStateMsg("Получение канджи из файла")
        val kanjiEntries = kanjiEdictRepository.getEdictEntries<KanjiDictionary, KanjiEdictEntry>(edictFile)
        changeStateMsg("Обработка канджи")
        val kanjisWithKanjiReadings = kanjiEntries.map { transformEntry(it) }
        changeStateMsg("Запись канджи в БД")
        kanjiDbUseCase.saveParsedKanjisWithReadings(kanjisWithKanjiReadings)
    }

    private fun transformEntry(
        kanjiEdictEntry: KanjiEdictEntry
    ): Pair<KanjiModel, List<KanjiReadingModel>?> {
        val kanji = KanjiModel().apply {
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
            KanjiReadingModel().apply {
                reading = it.value
                readingType = if (it.type == "ja_on") {
                    KanjiReadingType.ON_READING.str
                } else {
                    KanjiReadingType.KUN_READING.str
                }
            }
        }
        return Pair(kanji, kanjiReadings)
    }

    private suspend fun changeStateMsg(msg: String) {
        pParseStateMsg.value = msg
        delay(100L)
    }
}