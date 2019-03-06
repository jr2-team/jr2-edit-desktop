package ru.jr2.edit.presentation.view.kanji.edit

import ru.jr2.edit.Style
import ru.jr2.edit.domain.model.KanjiReadingModel
import ru.jr2.edit.presentation.view.BaseEditFragment
import ru.jr2.edit.presentation.viewmodel.kanji.edit.KanjiReadingEditViewModel
import ru.jr2.edit.presentation.viewmodel.kanji.edit.KanjiEditViewModel
import tornadofx.*

class KanjiEditReadingFragment : BaseEditFragment<KanjiReadingModel, KanjiReadingEditViewModel>() {
    override val viewModel = KanjiReadingEditViewModel(paramItemId)

    private val kanjiEditViewModel: KanjiEditViewModel by inject()

    override val root = borderpane {
        center = renderKanjiReadingPropertiesForm()
        bottom = renderBottomBorderPane()
    }

    private fun renderKanjiReadingPropertiesForm() = form {
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

    private fun renderBottomBorderPane() = borderpane {
        right = button("Сохранить") {
            action { }
            addClass(Style.mediumButton)
        }
        left = button("Отмена") {
            action { close() }
            addClass(Style.mediumButton)
        }
        paddingAll = 10
    }
}