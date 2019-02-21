package ru.jr2.edit.data.db.repository

import junit.framework.Assert.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import ru.jr2.edit.data.db.AppDatabase
import ru.jr2.edit.data.db.table.KanjiComponentTable
import ru.jr2.edit.domain.entity.KanjiReadingEntity
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.domain.model.KanjiReading
import ru.jr2.edit.domain.usecase.KanjiUseCase

internal class KanjiDbRepositoryTest {
    private val testDb: Database = AppDatabase(true).db
    private val repository = KanjiDbRepository(testDb)
    private val readingsRepository = KanjiReadingDbRepository(testDb)
    private val kanjiUseCase = KanjiUseCase(testDb)

    @Test
    fun getById() = transaction(testDb) {
        AppDatabase.updateSchema(testDb)
        var testMojiToInsert = getTestMoji()
        testMojiToInsert = repository.insert(testMojiToInsert)
        assertEquals(
            testMojiToInsert.kanji,
            repository.getById(testMojiToInsert.id).kanji
        )
    }

    @Test
    fun `save kanji with components and readings`() = transaction(testDb) {
        AppDatabase.updateSchema(testDb)
        val components = repository.insertUpdate(getKanjiComponents())
        val testMoji = kanjiUseCase.saveKanjiWithComponentsAndReadings(
            getTestMoji(),
            getKanjiReadings(),
            components
        )
        assertEquals(getTestMoji().kanji, repository.getById(testMoji.id).kanji)
        assertEquals(getKanjiReadings().count(), KanjiReadingEntity.all().count())
        assertEquals(
            getKanjiComponents().map { it.kanji },
            repository.getComponentsOfKanji(testMoji.id).map { it.kanji }
        )
    }

    @Test
    fun `delete kanji with components and readings`() = transaction(testDb) {
        AppDatabase.updateSchema(testDb)
        val components = repository.insertUpdate(getKanjiComponents())
        val testKanji = kanjiUseCase.saveKanjiWithComponentsAndReadings(
            getTestMoji(),
            getKanjiReadings(),
            components
        )
        kanjiUseCase.deleteKanjiWithComponentsAndReadings(testKanji)
        assertEquals(0, KanjiReadingEntity.all().count())
        assertEquals(0, KanjiComponentTable.selectAll().count())
    }

    private fun getTestMoji() = Kanji().apply {
        kanji = "生"
        strokeCount = 5
        jlptLevel = JlptLevel.JLPT4.str
        frequency = 29
        grade = 1
    }

    private fun getKanjiReadings() = listOf(
        KanjiReading(0, "セイ", 0, 0, false, 0),
        KanjiReading(0, "ショウ", 0, 1, false, 0),
        KanjiReading(0, "い.きる", 1, 0, false, 0),
        KanjiReading(0, "い.かす", 1, 1, false, 0)
    )

    private fun getKanjiComponents() = listOf(
        Kanji().apply { kanji = "A" },
        Kanji().apply { kanji = "B" },
        Kanji().apply { kanji = "C" },
        Kanji().apply { kanji = "D" },
        Kanji().apply { kanji = "E" }
    )
}