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
    override fun getById(id: Int): Kanji = transaction(db) {
        Kanji.fromEntity(KanjiEntity[id])
    }

    override fun getById(vararg id: Int): List<Kanji> = transaction(db) {
        id.map { Kanji.fromEntity(KanjiEntity[it]) }
    }

    override fun getAll(): List<Kanji> = transaction(db) {
        KanjiEntity.all().map { Kanji.fromEntity(it) }
    }

    fun getComponentsOfKanji(kanjiId: Int): List<Kanji> = transaction(db) {
        val componentAlias = KanjiComponentTable.alias("kanji_component")
        KanjiTable
            .innerJoin(
                componentAlias,
                { KanjiTable.id },
                { componentAlias[KanjiComponentTable.kanjiComponentId] }
            )
            .slice(KanjiTable.columns)
            .select {
                componentAlias[KanjiComponentTable.kanji] eq KanjiEntity[kanjiId].id
            }
            .orderBy(componentAlias[KanjiComponentTable.order])
            .map {
                Kanji.fromEntity(KanjiEntity.wrapRow(it))
            }
    }

    fun getBySearchQuery(query: String): List<Kanji> = transaction(db) {
        KanjiEntity.find {
            KanjiTable.interpretation.upperCase() like "%$query%".toUpperCase()
        }.map {
            Kanji.fromEntity(it)
        }
    }

    override fun insert(model: Kanji): Kanji = transaction(db) {
        val newKanji = KanjiEntity.new {
            updateWithModel(model)
        }
        Kanji.fromEntity(newKanji)
    }

    override fun insertUpdate(model: Kanji): Kanji = transaction(db) {
        KanjiEntity.findById(model.id)?.run {
            updateWithModel(model)
            getById(model.id)
        } ?: insert(model)
    }

    fun insertUpdate(models: List<Kanji>): List<Kanji> = transaction(db) {
        models.map { insertUpdate(it) }
    }

    override fun delete(model: Kanji) = transaction(db) {
        KanjiEntity[model.id].delete()
    }

    fun deleteKanjiComponents(model: Kanji) = transaction(db) {
        KanjiComponentTable.deleteWhere { KanjiComponentTable.kanji eq model.id }
    }
}