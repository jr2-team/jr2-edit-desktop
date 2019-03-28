package ru.jr2.edit.domain.usecase

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.leftJoin
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.data.db.table.WordInterpTable
import ru.jr2.edit.data.db.table.WordTable
import ru.jr2.edit.domain.dto.WordDto
import ru.jr2.edit.presentation.word.model.WordInterpretationModel
import ru.jr2.edit.presentation.word.model.WordModel
import ru.jr2.edit.util.JlptLevel

class WordDbUseCase {
    fun getWordWithInterps(n: Int, offset: Int, langFilter: String = "rus") = transaction {
        val wordMap = hashMapOf<Int, WordDto>()
        val wordAlias = WordTable.selectAll().limit(n, offset).alias("word")
        wordAlias.leftJoin(
            WordInterpTable,
            { wordAlias[WordTable.id] },
            { WordInterpTable.word }
        ).selectAll().map {
            val wordId = it[wordAlias[WordTable.id]].value
            if (!wordMap.containsKey(wordId)) {
                wordMap[wordId] = WordDto(
                    id = wordId,
                    word = it[wordAlias[WordTable.word]],
                    furigana = it[wordAlias[WordTable.furigana]] ?: String(),
                    interps = StringBuilder()
                )
            }
            val lang = it[WordInterpTable.language]
            val interp = it[WordInterpTable.interp]
            if (langFilter == lang) {
                wordMap[wordId]?.interps?.apply {
                    if (isNotEmpty()) append("\n")
                    append(interp)
                }
            }
        }
        return@transaction wordMap.values.sortedBy { it.id }
    }

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
    ) = transaction {
        val insertedWordIds = WordTable.batchInsert(wordsWithInterpretations.asSequence().map { it.first }.toList()) {
            this[WordTable.word] = it.word
            this[WordTable.furigana] = it.furigana
            this[WordTable.jlptLevel] = JlptLevel.fromStr(it.jlptLevel).code
        }.map { it[WordTable.id].value }

        insertedWordIds.forEachIndexed { idx, wordId ->
            wordsWithInterpretations[idx].second?.forEach { it.word = wordId }
        }
        WordInterpTable.batchInsert(
            wordsWithInterpretations.asSequence().filter { it.second != null }.toList().flatMap { it.second?.toList()!! }
        ) {
            this[WordInterpTable.interp] = it.interpretation
            this[WordInterpTable.language] = it.language
            this[WordInterpTable.pos] = it.pos
            this[WordInterpTable.word] = EntityID(it.word, WordTable)
        }
    }
}