package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.table.KanjiReadingTable
import ru.jr2.edit.domain.entity.KanjiReadingEntity
import ru.jr2.edit.presentation.model.KanjiReadingModel

class KanjiReadingDbRepository(
    override val db: Database = EditApp.instance.db
) : BaseDbRepository<KanjiReadingModel>(db) {
    override fun getById(id: Int): KanjiReadingModel = transaction(db) {
        KanjiReadingModel.fromEntity(KanjiReadingEntity[id])
    }

    override fun getById(vararg id: Int): List<KanjiReadingModel> = transaction(db) {
        id.map { KanjiReadingModel.fromEntity(KanjiReadingEntity[it]) }
    }

    override fun getAll(): List<KanjiReadingModel> = transaction(db) {
        KanjiReadingEntity.all().map { KanjiReadingModel.fromEntity(it) }
    }

    fun getByKanjiId(kanjiId: Int): List<KanjiReadingModel> = transaction(db) {
        KanjiReadingEntity.find {
            KanjiReadingTable.kanji eq kanjiId
        }.map {
            KanjiReadingModel.fromEntity(it)
        }
    }

    override fun insert(model: KanjiReadingModel): KanjiReadingModel = transaction(db) {
        val newKanjiReading = KanjiReadingEntity.new {
            updateWithModel(model)
        }
        KanjiReadingModel.fromEntity(newKanjiReading)
    }

    override fun insertUpdate(model: KanjiReadingModel): KanjiReadingModel = transaction(db) {
        KanjiReadingEntity.findById(model.id)?.run {
            updateWithModel(model)
            getById(model.id)
        } ?: insert(model)
    }

    fun insertUpdate(models: List<KanjiReadingModel>): List<KanjiReadingModel> = transaction(db) {
        models.map { insertUpdate(it) }
    }

    override fun delete(model: KanjiReadingModel) = transaction(db) {
        KanjiReadingEntity[model.id].delete()
    }

    fun deleteByKanjiId(kanjiId: Int) = transaction(db) {
        KanjiReadingEntity.find {
            KanjiReadingTable.kanji eq kanjiId
        }.forEach { it.delete() }
    }
}