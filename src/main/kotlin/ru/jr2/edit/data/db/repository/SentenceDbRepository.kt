package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.domain.entity.SentenceEntity
import ru.jr2.edit.presentation.sentence.model.SentenceModel

class SentenceDbRepository : BaseDbRepository<SentenceModel>() {
    override fun getById(id: Int): SentenceModel = transaction(db) {
        return@transaction SentenceModel.fromEntity(SentenceEntity[id])
    }

    override fun getById(vararg id: Int): List<SentenceModel> = transaction(db) {
        return@transaction id.map {
            SentenceModel.fromEntity(SentenceEntity[it])
        }
    }

    override fun getAll(): List<SentenceModel> = transaction(db) {
        return@transaction SentenceEntity.all().map { SentenceModel.fromEntity(it) }
    }

    override fun insert(model: SentenceModel): SentenceModel = transaction(db) {
        val newSentence = SentenceEntity.new {
            sentence = model.sentence
            furigana = model.furigana
            interpretation = model.interpretation
        }
        return@transaction SentenceModel.fromEntity(newSentence)
    }

    override fun insertUpdate(model: SentenceModel): SentenceModel = transaction(db) {
        return@transaction SentenceEntity.findById(model.id)?.run {
            sentence = model.sentence
            furigana = model.furigana
            interpretation = model.interpretation
            getById(model.id)
        } ?: insert(model)
    }

    override fun delete(model: SentenceModel) = transaction(db) {
        SentenceEntity[model.id].delete()
    }
}