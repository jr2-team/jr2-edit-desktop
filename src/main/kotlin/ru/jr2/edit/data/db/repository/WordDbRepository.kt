package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.domain.JlptLevel
import ru.jr2.edit.domain.entity.WordEntity
import ru.jr2.edit.domain.model.Word

class WordDbRepository(
    private val db: Database = EditApp.instance.db
) {
    fun getById(id: Int): Word = transaction(db) {
        return@transaction Word.fromEntity(WordEntity[id])
    }

    fun getAll(): List<Word> = transaction(db) {
        return@transaction WordEntity.all().map { Word.fromEntity(it) }
    }

    fun insert(word: Word): Word = transaction(db) {
        val newWord = WordEntity.new {
            value = word.value
            furigana = word.furigana
            interpretation = word.interpretation
            jlptLevel = JlptLevel.fromStr(word.jlptLevel).code
        }
        return@transaction Word.fromEntity(newWord)
    }

    fun insertUpdate(word: Word): Word = transaction(db) {
        return@transaction WordEntity.findById(word.id)?.run {
            value = word.value
            furigana = word.furigana
            interpretation = word.interpretation
            jlptLevel = JlptLevel.fromStr(word.jlptLevel).code
            getById(word.id)
        } ?: insert(word)
    }

    fun delete(word: Word) = transaction(db) {
        WordEntity[word.id].delete()
    }
}