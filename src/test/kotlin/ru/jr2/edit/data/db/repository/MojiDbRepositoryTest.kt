package ru.jr2.edit.data.db.repository

import junit.framework.Assert.assertEquals
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import ru.jr2.edit.data.db.AppDatabase
import ru.jr2.edit.domain.entity.KanjiReadingEntity
import ru.jr2.edit.domain.entity.MojiEntity
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.model.KanjiReading
import ru.jr2.edit.domain.model.Moji

internal class MojiDbRepositoryTest {
    private val testDb: Database = AppDatabase(true).db
    private val repository = MojiDbRepository(testDb)

    @Test
    fun getById() = transaction(testDb) {
        AppDatabase.updateSchema(testDb)
        var testMojiToInsert = getTestMoji()
        testMojiToInsert = repository.insert(testMojiToInsert)
        assertEquals(
            testMojiToInsert.moji,
            repository.getById(testMojiToInsert.id).moji
        )
    }

    @Test
    fun insertUpdate() = transaction(testDb) {
        AppDatabase.updateSchema(testDb)
        val components = repository.insertAll(getKanjiComponents())
        val testMoji = repository.insertUpdate(
            getTestMoji(),
            getKanjiReadings(),
            components
        )
        assertEquals(getTestMoji().moji, repository.getById(testMoji.id).moji)
        assertEquals(getKanjiReadings().count(), KanjiReadingEntity.all().count())
        assertEquals(
            getKanjiComponents().map { it.moji },
            repository.getComponentsOfMoji(testMoji.id).map { it.moji }
        )
    }

    @Test
    fun delete() = transaction(testDb) {
        AppDatabase.updateSchema(testDb)
        val components = repository.insertAll(getKanjiComponents())
        val testMoji = repository.insertUpdate(
            getTestMoji(),
            getKanjiReadings(),
            components
        )
        repository.delete(testMoji)
        assertEquals(0, KanjiReadingEntity.all().count())
        assertEquals(0, MojiEntity.all().count())
    }

    private fun getTestMoji() = Moji().apply {
        moji = "生"
        strokeCount = 5
        jlptLevel = JlptLevel.JLPT4.str
        frequency = 29
        grade = 1
    }

    private fun getKanjiReadings() = listOf(
        KanjiReading("セイ", 0, 0, false),
        KanjiReading("ショウ", 0, 1, false),
        KanjiReading("い.きる", 1, 0, false),
        KanjiReading("い.かす", 1, 1, false)
    )

    private fun getKanjiComponents() = listOf(
        Moji().apply { moji = "A" },
        Moji().apply { moji = "B" },
        Moji().apply { moji = "C" },
        Moji().apply { moji = "D" },
        Moji().apply { moji = "E" }
    )
}