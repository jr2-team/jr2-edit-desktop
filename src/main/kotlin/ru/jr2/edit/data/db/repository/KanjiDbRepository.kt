package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.table.KanjiComponentTable
import ru.jr2.edit.data.db.table.KanjiTable
import ru.jr2.edit.domain.entity.KanjiEntity
import ru.jr2.edit.domain.model.Kanji

class KanjiDbRepository(
    override val db: Database = EditApp.instance.db
) : BaseDbRepository<Kanji>(db) {
    override fun getById(kanjiId: Int): Kanji = transaction(db) {
        Kanji.fromEntity(KanjiEntity[kanjiId])
    }

    override fun getById(vararg kanjiId: Int): List<Kanji> = transaction(db) {
        kanjiId.map { Kanji.fromEntity(KanjiEntity[it]) }
    }

    override fun getAll(): List<Kanji> = transaction(db) {
        KanjiEntity.all().map { Kanji.fromEntity(it) }
    }

    fun getBySearchQuery(query: String): List<Kanji> = transaction(db) {
        KanjiEntity.find {
            KanjiTable.interpretation.upperCase() like "%$query%".toUpperCase()
        }.map {
            Kanji.fromEntity(it)
        }
    }

    override fun insert(kanji: Kanji): Kanji = transaction(db) {
        val newKanjiEntity = KanjiEntity.new {
            updateWithModel(kanji)
        }
        Kanji.fromEntity(newKanjiEntity)
    }

    override fun insertUpdate(kanji: Kanji): Kanji = transaction(db) {
        KanjiEntity.findById(kanji.id)?.run {
            updateWithModel(kanji)
            getById(kanji.id)
        } ?: insert(kanji)
    }

    fun insertUpdate(models: List<Kanji>): List<Kanji> = transaction(db) {
        models.map { insertUpdate(it) }
    }

    override fun delete(model: Kanji) = transaction(db) {
        KanjiEntity[model.id].delete()
    }
}