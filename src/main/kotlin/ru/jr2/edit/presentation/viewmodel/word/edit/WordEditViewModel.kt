package ru.jr2.edit.presentation.viewmodel.word.edit

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.KotlinLoggingSqlLogger
import ru.jr2.edit.domain.entity.WordEntity
import ru.jr2.edit.domain.model.Word
import tornadofx.ViewModel

class WordEditViewModel(
    wordId: Int,
    private val mode: WordEditMode =
        if (wordId == 0) WordEditMode.CREATE else WordEditMode.UPDATE,
    private val db: Database = EditApp.instance.db
) : ViewModel() {
    var observableWord = Word()

    init {
        if (mode == WordEditMode.UPDATE) {
            // TODO: Создать репозитории
            transaction(db) {
                observableWord = Word.fromEntity(WordEntity[wordId])
            }
        }
    }

    fun onWordSave() = transaction(db) {
        addLogger(KotlinLoggingSqlLogger)
        when (mode) {
            WordEditMode.UPDATE -> {
                WordEntity.findById(observableWord.id)?.run {
                    this.value = observableWord.value
                    this.furigana = observableWord.furigana
                    this.basicInterpretation = observableWord.basicInterpretation
                    this.jlptLevel = observableWord.jlptLevel
                }
            }
            WordEditMode.CREATE -> {
                WordEntity.new {
                    value = observableWord.value
                    furigana = observableWord.furigana
                    basicInterpretation = observableWord.basicInterpretation
                    jlptLevel = observableWord.jlptLevel
                }
            }
        }
        fire(WordSavedEvent(""))
    }
}

enum class WordEditMode(val code: Int) {
    UPDATE(0),
    CREATE(1);
}