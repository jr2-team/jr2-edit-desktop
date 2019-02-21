package ru.jr2.edit.presentation.view.kanji.edit

import ru.jr2.edit.Style
import ru.jr2.edit.data.db.repository.KanjiReadingDbRepository
import ru.jr2.edit.domain.model.KanjiReading
import ru.jr2.edit.presentation.view.BaseEditFragment
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel
import ru.jr2.edit.presentation.viewmodel.kanji.KanjiEditViewModel
import tornadofx.*

class KanjiEditReadingFragment : BaseEditFragment<KanjiReading, KanjiEditReadingViewModel>() {
    override val viewModel = KanjiEditReadingViewModel(paramItemId)
    private val kanjiEditViewModel: KanjiEditViewModel by inject()

    override val root = borderpane {
        center = form {
            add(renderKanjiReadingPropertiesFieldSet())
        }
        bottom = form {
            add(renderBottomButtonBar())
        }
    }

    private fun renderKanjiReadingPropertiesFieldSet() = form {
        fieldset {
            field("Чтение") {
                textfield(viewModel.pReading) { }
            }
            field("Вид чтения") {
                textfield(viewModel.pReadingType) { }
            }
            field("Приоритет") {
                textfield(viewModel.pPriority) { }
            }
            togglebutton("Анахроизм", selectFirst = viewModel.pIsAnachronism.value)
        }
    }

    private fun renderBottomButtonBar() = borderpane {
        right = button("Сохранить") {
            action { }
            addClass(Style.mediumButton)
        }

        left = button("Отмена") {
            action { close() }
            addClass(Style.mediumButton)
        }
    }
}

class KanjiEditReadingViewModel(
    kanjiReadingId: Int
) : BaseEditViewModel<KanjiReading>(
    kanjiReadingId,
    KanjiReadingDbRepository(),
    KanjiReading()
) {
    val pReading = bind(KanjiReading::pReading)
    val pReadingType = bind(KanjiReading::pReadingType)
    val pPriority = bind(KanjiReading::pPriority)
    val pIsAnachronism = bind(KanjiReading::pIsAnachronism)
}