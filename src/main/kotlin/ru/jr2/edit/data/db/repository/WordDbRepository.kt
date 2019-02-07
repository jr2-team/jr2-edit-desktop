package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.KotlinLoggingSqlLogger
import ru.jr2.edit.domain.entity.WordEntity
import ru.jr2.edit.domain.model.Word

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

    fun create(word: Word) = transaction(db) {
        addLogger(KotlinLoggingSqlLogger)
        WordEntity.new {
            value = word.value
            furigana = word.furigana
            basicInterpretation = word.basicInterpretation
            jlptLevel = word.jlptLevel
        }
    }

    fun update(word: Word) = transaction(db) {
        addLogger(KotlinLoggingSqlLogger)
        WordEntity.findById(word.id)?.run {
            this.value = word.value
            this.furigana = word.furigana
            this.basicInterpretation = word.basicInterpretation
            this.jlptLevel = word.jlptLevel
        }
    }
}