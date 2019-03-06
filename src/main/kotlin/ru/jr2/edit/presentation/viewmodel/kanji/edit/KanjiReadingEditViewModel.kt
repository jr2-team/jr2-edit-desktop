package ru.jr2.edit.presentation.viewmodel.kanji.edit

import ru.jr2.edit.domain.model.KanjiReadingModel
import tornadofx.ItemViewModel

class KanjiReadingEditViewModel(
    kanjiReading: KanjiReadingModel
) : ItemViewModel<KanjiReadingModel>(kanjiReading) {
    val pReading = bind(KanjiReadingModel::pReading)
    val pReadingType = bind(KanjiReadingModel::pReadingType)
    val pPriority = bind(KanjiReadingModel::pPriority)
    val pIsAnachronism = bind(KanjiReadingModel::pIsAnachronism)

    init {
        item = kanjiReading
    }
}