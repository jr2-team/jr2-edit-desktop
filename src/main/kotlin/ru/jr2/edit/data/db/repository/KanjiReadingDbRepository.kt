package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.table.KanjiReadingTable
import ru.jr2.edit.domain.entity.KanjiReadingEntity

class KanjiReadingDbRepository(
    private val db: Database = EditApp.instance.db
) {
    fun getByKanjiId(kanjiId: Int): List<KanjiReadingEntity> = transaction(db) {
        KanjiReadingEntity.find {
            KanjiReadingTable.kanji eq kanjiId
        }.map { it }
    }
}