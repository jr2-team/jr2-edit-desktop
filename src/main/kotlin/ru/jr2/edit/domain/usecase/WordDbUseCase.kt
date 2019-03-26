package ru.jr2.edit.domain.usecase

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.table.WordInterpretationTable
import ru.jr2.edit.data.db.table.WordTable
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.presentation.word.model.WordInterpretationModel
import ru.jr2.edit.presentation.word.model.WordModel

class WordDbUseCase(
    private val db: Database = EditApp.instance.db
) {
    fun saveParsedWordsWithInterpretations(
        wordsWithInterpretations: List<Pair<WordModel, List<WordInterpretationModel>?>>
    ) {
        val listSize = wordsWithInterpretations.size - 1
        for (idxFrom in 0..listSize step 5000) {
            val idxTo = if (idxFrom + 5000 > listSize) listSize else idxFrom + 5000
            savePortionOfWords(wordsWithInterpretations.slice(idxFrom..idxTo))
        }
    }

    private fun savePortionOfWords(
        wordsWithInterpretations: List<Pair<WordModel, List<WordInterpretationModel>?>>
    ) = transaction(db) {
        val insertedWordIds = WordTable.batchInsert(wordsWithInterpretations.asSequence().map { it.first }.toList()) {
            this[WordTable.word] = it.word
            this[WordTable.interpretation] = it.interpretation
            this[WordTable.furigana] = it.furigana
            this[WordTable.jlptLevel] = JlptLevel.fromStr(it.jlptLevel).code
        }.map { it[WordTable.id].value }

        insertedWordIds.forEachIndexed { idx, wordId ->
            wordsWithInterpretations[idx].second?.forEach { it.word = wordId }
        }
        WordInterpretationTable.batchInsert(
            wordsWithInterpretations.asSequence().filter { it.second != null }.toList().flatMap { it.second?.toList()!! }
        ) {
            this[WordInterpretationTable.interpretation] = it.interpretation
            this[WordInterpretationTable.language] = it.language
            this[WordInterpretationTable.pos] = it.pos
            this[WordInterpretationTable.word] = EntityID(it.word, WordTable)
        }
    }
}