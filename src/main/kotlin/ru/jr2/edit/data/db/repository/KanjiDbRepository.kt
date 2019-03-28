package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upperCase
import ru.jr2.edit.data.db.table.KanjiTable
import ru.jr2.edit.domain.entity.KanjiEntity
import ru.jr2.edit.presentation.kanji.model.KanjiModel

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class KanjiDbRepository : BaseDbRepository<KanjiModel>() {
    override fun getById(kanjiId: Int): KanjiModel = transaction {
        KanjiModel.fromEntity(KanjiEntity[kanjiId])
    }

    override fun getById(vararg kanjiId: Int): List<KanjiModel> = transaction {
        kanjiId.map { KanjiModel.fromEntity(KanjiEntity[it]) }
    }

    override fun getAll(): List<KanjiModel> = transaction {
        KanjiEntity.all().map { KanjiModel.fromEntity(it) }
    }

    fun getBySearchQuery(query: String): List<KanjiModel> = transaction {
        KanjiEntity.find {
            KanjiTable.interp.upperCase() like "%$query%".toUpperCase()
        }.map {
            KanjiModel.fromEntity(it)
        }
    }

    override fun insert(kanji: KanjiModel): KanjiModel = transaction {
        val newKanjiEntity = KanjiEntity.new {
            updateWithModel(kanji)
        }
        KanjiModel.fromEntity(newKanjiEntity)
    }

    override fun insertUpdate(kanji: KanjiModel): KanjiModel = transaction {
        KanjiEntity.findById(kanji.id)?.run {
            updateWithModel(kanji)
            getById(kanji.id)
        } ?: insert(kanji)
    }

    fun insertUpdate(models: List<KanjiModel>): List<KanjiModel> = transaction {
        models.map { insertUpdate(it) }
    }

    override fun delete(model: KanjiModel) = transaction {
        KanjiEntity[model.id].delete()
    }
}