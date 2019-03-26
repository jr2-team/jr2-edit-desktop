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
import ru.jr2.edit.domain.usecase.KanjiDbUseCase
import ru.jr2.edit.presentation.kanji.model.KanjiModel
import ru.jr2.edit.presentation.kanji.model.KanjiReadingModel

internal class KanjiDbRepositoryTest {
    private val testDb: Database = AppDatabase(true).db
    private val repository = KanjiDbRepository(testDb)
    private val kanjiUseCase = KanjiDbUseCase(testDb)

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
            kanjiUseCase.getKanjiComponents(testMoji.id).map { it.kanji }
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

    private fun getTestMoji() = KanjiModel().apply {
        kanji = "生"
        strokeCount = 5
        jlptLevel = JlptLevel.JLPT4.str
        frequency = 29
        grade = 1
    }

    // TODO: Add test data
    private fun getKanjiReadings() = emptyList<KanjiReadingModel>()

    private fun getKanjiComponents() = listOf(
        KanjiModel().apply { kanji = "A" },
        KanjiModel().apply { kanji = "B" },
        KanjiModel().apply { kanji = "C" },
        KanjiModel().apply { kanji = "D" },
        KanjiModel().apply { kanji = "E" }
    )
}