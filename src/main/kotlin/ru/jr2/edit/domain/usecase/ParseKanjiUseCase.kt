package ru.jr2.edit.domain.usecase

import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ru.jr2.edit.data.db.repository.MojiDbRepository
import ru.jr2.edit.data.editc.mapping.KanjiEdictEntry
import ru.jr2.edit.data.editc.repository.KanjiEdictRepository
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.model.Moji
import ru.jr2.edit.util.pmap
import java.io.File

class ParseKanjiUseCase(
    private val mojiDbRepository: MojiDbRepository = MojiDbRepository(),
    private val kanjiEdictRepository: KanjiEdictRepository = KanjiEdictRepository()
) {
    val pParseStateMsg = SimpleStringProperty(String())

    suspend fun parseEdictAndSaveToDb(edictFile: File) = coroutineScope {
        val kanjiEntries = kanjiEdictRepository.getKanjiEntriesFromFile(edictFile)
        val kanjis = kanjiEntries.pmap { transformEntry(it) }
        withContext(Dispatchers.Default) { mojiDbRepository.insertAll(kanjis) }
    }

    // TODO: Пределать схему БД
    private fun transformEntry(kanjiEdictEntry: KanjiEdictEntry) = Moji().apply {
        value = kanjiEdictEntry.literal
        strokeCount = kanjiEdictEntry.strokeCount
        jlptLevel = JlptLevel.fromCode(kanjiEdictEntry.jlpt).str
    }
}