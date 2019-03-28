package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.data.db.table.WordInterpTable
import ru.jr2.edit.domain.entity.WordInterpEntity
import ru.jr2.edit.presentation.word.model.WordInterpretationModel

class WordInterpDbRepository {
    fun getByWordId(wordId: Int): List<WordInterpretationModel> = transaction {
        WordInterpEntity.find {
            WordInterpTable.word eq wordId
        }.map {
            WordInterpretationModel.fromEntity(it)
        }
    }
}