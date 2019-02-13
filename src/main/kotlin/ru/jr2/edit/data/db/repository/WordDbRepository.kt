package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.data.db.table.WordTable
import ru.jr2.edit.domain.entity.WordEntity
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.model.Word

class WordDbRepository : BaseDbRepository<Word>() {
    override fun getById(id: Int): Word = transaction(db) {
        return@transaction Word.fromEntity(WordEntity[id])
    }

    override fun getById(vararg id: Int): List<Word> = transaction(db) {
        id.map {
            Word.fromEntity(WordEntity[it])
        }
    }

    override fun getAll(): List<Word> = transaction(db) {
        WordEntity.all().map { Word.fromEntity(it) }
    }

    fun getWithOffset(n: Int, offset: Int): List<Word> = transaction(db) {
        WordTable.selectAll()
            .limit(n, offset)
            .map {
                Word.fromEntity(WordEntity.wrapRow(it))
            }
    }

    fun getCount(): Int = transaction(db) { WordEntity.count() }

    override fun insert(word: Word): Word = transaction(db) {
        val newWord = WordEntity.new {
            value = word.value
            furigana = word.furigana
            interpretation = word.interpretation
            jlptLevel = JlptLevel.fromStr(word.jlptLevel).code
        }
        Word.fromEntity(newWord)
    }

    fun insertAll(words: List<Word>) = transaction(db) {
        WordTable.batchInsert(words) {
            this[WordTable.value] = if (it.value.isBlank()) it.furigana else it.value
            this[WordTable.furigana] = it.furigana
            this[WordTable.interpretation] = it.interpretation
            this[WordTable.jlptLevel] = JlptLevel.fromStr(it.jlptLevel).code
        }
    }

    override fun insertUpdate(word: Word): Word = transaction(db) {
        WordEntity.findById(word.id)?.run {
            value = word.value
            furigana = word.furigana
            interpretation = word.interpretation
            jlptLevel = JlptLevel.fromStr(word.jlptLevel).code
            getById(word.id)
        } ?: insert(word)
    }

    override fun delete(word: Word) = transaction(db) {
        WordEntity[word.id].delete()
    }
}