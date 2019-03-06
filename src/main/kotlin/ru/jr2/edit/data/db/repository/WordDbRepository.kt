package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.data.db.table.WordTable
import ru.jr2.edit.domain.entity.WordEntity
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.presentation.model.WordModel

class WordDbRepository : BaseDbRepository<WordModel>() {
    override fun getById(id: Int): WordModel = transaction(db) {
        return@transaction WordModel.fromEntity(WordEntity[id])
    }

    override fun getById(vararg id: Int): List<WordModel> = transaction(db) {
        id.map {
            WordModel.fromEntity(WordEntity[it])
        }
    }

    override fun getAll(): List<WordModel> = transaction(db) {
        WordEntity.all().map { WordModel.fromEntity(it) }
    }

    fun getWithOffset(n: Int, offset: Int): List<WordModel> = transaction(db) {
        WordTable.selectAll()
            .limit(n, offset)
            .map {
                WordModel.fromEntity(WordEntity.wrapRow(it))
            }
    }

    fun getCount(): Int = transaction(db) { WordEntity.count() }

    override fun insert(model: WordModel): WordModel = transaction(db) {
        val newWord = WordEntity.new {
            this.word = model.word
            furigana = model.furigana
            interpretation = model.interpretation
            jlptLevel = JlptLevel.fromStr(model.jlptLevel).code
        }
        WordModel.fromEntity(newWord)
    }

    fun insertAll(words: List<WordModel>) {
        transaction(db) {
            WordTable.batchInsert(words) {
                this[WordTable.word] = it.word
                this[WordTable.furigana] = it.furigana
                this[WordTable.interpretation] = it.interpretation
                this[WordTable.jlptLevel] = JlptLevel.fromStr(it.jlptLevel).code
            }
        }
    }

    override fun insertUpdate(model: WordModel): WordModel = transaction(db) {
        WordEntity.findById(model.id)?.run {
            word = model.word
            furigana = model.furigana
            interpretation = model.interpretation
            jlptLevel = JlptLevel.fromStr(model.jlptLevel).code
            getById(model.id)
        } ?: insert(model)
    }

    override fun delete(model: WordModel) = transaction(db) {
        WordEntity[model.id].delete()
    }
}