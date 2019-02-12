package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.domain.JlptLevel
import ru.jr2.edit.domain.entity.WordEntity
import ru.jr2.edit.domain.model.Word

class WordDbRepository : BaseDbRepository<Word>() {
    override fun getById(id: Int): Word = transaction(db) {
        return@transaction Word.fromEntity(WordEntity[id])
    }

    override fun getById(vararg id: Int): List<Word> = transaction(db) {
        return@transaction id.map {
            Word.fromEntity(WordEntity[it])
        }
    }

    override fun getAll(): List<Word> = transaction(db) {
        return@transaction WordEntity.all().map { Word.fromEntity(it) }
    }

    override fun insert(word: Word): Word = transaction(db) {
        val newWord = WordEntity.new {
            value = word.value
            furigana = word.furigana
            interpretation = word.interpretation
            jlptLevel = JlptLevel.fromStr(word.jlptLevel).code
        }
        return@transaction Word.fromEntity(newWord)
    }

    override fun insertUpdate(word: Word): Word = transaction(db) {
        return@transaction WordEntity.findById(word.id)?.run {
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