package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.domain.entity.WordEntity
import ru.jr2.edit.domain.model.Word
import ru.jr2.edit.util.KotlinLoggingSqlLogger

class WordDbRepository(
    private val db: Database = EditApp.instance.db
) {
    fun getById(id: Int): Word = transaction(db) {
        addLogger(KotlinLoggingSqlLogger)
        return@transaction Word.fromEntity(WordEntity[id])
    }

    fun getAll(): List<Word> = transaction(db) {
        addLogger(KotlinLoggingSqlLogger)
        return@transaction WordEntity.all().map { Word.fromEntity(it) }
    }

    fun insert(word: Word): Word = transaction(db) {
        addLogger(KotlinLoggingSqlLogger)
        val newWord = WordEntity.new {
            value = word.value
            furigana = word.furigana
            basicInterpretation = word.basicInterpretation
            jlptLevel = word.jlptLevel
        }
        return@transaction Word.fromEntity(newWord)
    }

    fun insertUpdate(word: Word): Word = transaction(db) {
        addLogger(KotlinLoggingSqlLogger)
        return@transaction WordEntity.findById(word.id)?.run {
            value = word.value
            furigana = word.furigana
            basicInterpretation = word.basicInterpretation
            jlptLevel = word.jlptLevel
            getById(word.id)
        } ?: insert(word)
    }
}