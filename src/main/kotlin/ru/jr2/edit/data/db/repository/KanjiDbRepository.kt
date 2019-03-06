package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.table.KanjiTable
import ru.jr2.edit.domain.entity.KanjiEntity
import ru.jr2.edit.presentation.model.KanjiModel

class KanjiDbRepository(
    override val db: Database = EditApp.instance.db
) : BaseDbRepository<KanjiModel>(db) {
    override fun getById(kanjiId: Int): KanjiModel = transaction(db) {
        KanjiModel.fromEntity(KanjiEntity[kanjiId])
    }

    override fun getById(vararg kanjiId: Int): List<KanjiModel> = transaction(db) {
        kanjiId.map { KanjiModel.fromEntity(KanjiEntity[it]) }
    }

    override fun getAll(): List<KanjiModel> = transaction(db) {
        KanjiEntity.all().map { KanjiModel.fromEntity(it) }
    }

    fun getBySearchQuery(query: String): List<KanjiModel> = transaction(db) {
        KanjiEntity.find {
            KanjiTable.interpretation.upperCase() like "%$query%".toUpperCase()
        }.map {
            KanjiModel.fromEntity(it)
        }
    }

    override fun insert(kanji: KanjiModel): KanjiModel = transaction(db) {
        val newKanjiEntity = KanjiEntity.new {
            updateWithModel(kanji)
        }
        KanjiModel.fromEntity(newKanjiEntity)
    }

    override fun insertUpdate(kanji: KanjiModel): KanjiModel = transaction(db) {
        KanjiEntity.findById(kanji.id)?.run {
            updateWithModel(kanji)
            getById(kanji.id)
        } ?: insert(kanji)
    }

    fun insertUpdate(models: List<KanjiModel>): List<KanjiModel> = transaction(db) {
        models.map { insertUpdate(it) }
    }

    override fun delete(model: KanjiModel) = transaction(db) {
        KanjiEntity[model.id].delete()
    }
}