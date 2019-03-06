package ru.jr2.edit.presentation.viewmodel.kanji.edit

import ru.jr2.edit.data.db.repository.KanjiReadingDbRepository
import ru.jr2.edit.domain.model.KanjiReadingModel
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel

class KanjiReadingEditViewModel(
    kanjiReadingId: Int
) : BaseEditViewModel<KanjiReadingModel>(
    kanjiReadingId,
    KanjiReadingDbRepository(),
    KanjiReadingModel()
) {
    val pReading = bind(KanjiReadingModel::pReading)
    val pReadingType = bind(KanjiReadingModel::pReadingType)
    val pPriority = bind(KanjiReadingModel::pPriority)
    val pIsAnachronism = bind(KanjiReadingModel::pIsAnachronism)
}