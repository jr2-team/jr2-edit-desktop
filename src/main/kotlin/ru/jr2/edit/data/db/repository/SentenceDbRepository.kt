package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.domain.entity.SentenceEntity
import ru.jr2.edit.domain.model.Sentence

class SentenceDbRepository : BaseDbRepository<Sentence>() {
    override fun getById(id: Int): Sentence = transaction(db) {
        return@transaction Sentence.fromEntity(SentenceEntity[id])
    }

    override fun getById(vararg id: Int): List<Sentence> = transaction(db) {
        return@transaction id.map {
            Sentence.fromEntity(SentenceEntity[it])
        }
    }

    override fun getAll(): List<Sentence> = transaction(db) {
        return@transaction SentenceEntity.all().map { Sentence.fromEntity(it) }
    }

    override fun insert(sentenceModel: Sentence): Sentence = transaction(db) {
        val newSentence = SentenceEntity.new {
            sentence = sentenceModel.sentence
            furigana = sentenceModel.furigana
            interpretation = sentenceModel.interpretation
        }
        return@transaction Sentence.fromEntity(newSentence)
    }

    override fun insertUpdate(sentenceModel: Sentence): Sentence = transaction(db) {
        return@transaction SentenceEntity.findById(sentenceModel.id)?.run {
            sentence = sentenceModel.sentence
            furigana = sentenceModel.furigana
            interpretation = sentenceModel.interpretation
            getById(sentenceModel.id)
        } ?: insert(sentenceModel)
    }

    override fun delete(sentence: Sentence) = transaction(db) {
        SentenceEntity[sentence.id].delete()
    }
}