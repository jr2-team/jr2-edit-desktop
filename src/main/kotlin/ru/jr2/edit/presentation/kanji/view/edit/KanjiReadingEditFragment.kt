package ru.jr2.edit.presentation.kanji.view.edit

import ru.jr2.edit.Style
import ru.jr2.edit.domain.misc.KanjiReadingType
import ru.jr2.edit.presentation.kanji.model.KanjiReadingModel
import ru.jr2.edit.presentation.base.view.BaseEditFragment.Companion.requiredMsg
import ru.jr2.edit.presentation.kanji.viewmodel.edit.KanjiEditViewModel
import ru.jr2.edit.presentation.kanji.viewmodel.edit.KanjiReadingEditViewModel
import tornadofx.*

class KanjiReadingEditFragment(
    titleCreate: String = "Создать запись",
    titleEdit: String = "Редактировать запись"
) : Fragment() {
    val paramKanjiReading: KanjiReadingModel by param(KanjiReadingModel())

    private val kanjiEditViewModel: KanjiEditViewModel by inject()
    private val viewModel = KanjiReadingEditViewModel(paramKanjiReading)

    init {
        title = if (paramKanjiReading.id == 0) titleCreate else titleEdit
    }

    override val root = borderpane {
        center = renderKanjiReadingPropertiesForm()
        bottom = renderBottomBorderPane()
    }

    private fun renderKanjiReadingPropertiesForm() = form {
        fieldset {
            field("Чтение") {
                textfield(viewModel.pReading).required(message = requiredMsg)
            }
            field("Вид чтения") {
                combobox(viewModel.pReadingType, KanjiReadingType.getNames())
            }
            field("Приоритет") {
                textfield(viewModel.pPriority).required(message = requiredMsg)
            }
        }
        togglebutton("Анахроизм", selectFirst = viewModel.pIsAnachronism.value) {
            action {
                viewModel.pIsAnachronism.value = isSelected
            }
        }
    }

    private fun renderBottomBorderPane() = borderpane {
        right = button("Сохранить") {
            enableWhen(viewModel.valid)
            action {
                viewModel.commit()
                kanjiEditViewModel.onKanjiReadingSaveClick(viewModel.item)
                close()
            }
            addClass(Style.mediumButton)
        }
        left = button("Отмена") {
            action { close() }
            addClass(Style.mediumButton)
        }
        paddingAll = 10
    }
}