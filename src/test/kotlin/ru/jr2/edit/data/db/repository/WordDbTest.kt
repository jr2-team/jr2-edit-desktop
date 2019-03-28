package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.domain.usecase.WordDbUseCase
import ru.jr2.edit.util.Injectable
import kotlin.test.Test

internal class WordDbTest {
    private val db = Injectable.db
    private val wordDbUseCase = WordDbUseCase()

    @Test
    fun `word with interpretation fetch`() = transaction(db) {
        val words = wordDbUseCase.getWordWithInterps(100, 200)
    }
}