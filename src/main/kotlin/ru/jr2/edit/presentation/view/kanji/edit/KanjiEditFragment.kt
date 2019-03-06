package ru.jr2.edit.presentation.view.kanji.edit

import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.Priority
import ru.jr2.edit.Style.Companion.largeButton
import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.presentation.model.KanjiModel
import ru.jr2.edit.presentation.model.KanjiReadingModel
import ru.jr2.edit.presentation.view.BaseEditFragment
import ru.jr2.edit.presentation.viewmodel.kanji.edit.KanjiEditViewModel
import tornadofx.*

class KanjiEditFragment : BaseEditFragment<KanjiModel, KanjiEditViewModel>() {
    override val viewModel = KanjiEditViewModel(paramItemId)

    override val root = borderpane {
        paddingAll = 10
        center = form {
            add(renderKanjiPropertiesFieldSet())
            add(renderKanjiReadingBorderPane())
            add(renderKanjiComponentBorderPane())
        }
        bottom = renderSaveHBox()
    }

    private fun renderKanjiPropertiesFieldSet() = fieldset {
        field("Канджи", Orientation.VERTICAL) {
            textfield(viewModel.pKanji) {
                filterInput {
                    with(it.controlNewText) { length == 1 }
                }
                style {
                    setMaxSize(48.0, 48.0)
                    alignment = Pos.CENTER
                    fontSize = 18.px
                }
            }.required(message = requiredMsg)
        }
        field("Количество черт", Orientation.VERTICAL) {
            textfield(viewModel.pStrokeCount) {
                filterInput {
                    with(it.controlNewText) { isInt() && toInt() > 0 }
                }
            }.required(message = requiredMsg)
        }
        field("Основные переводы") {
            textarea(viewModel.pInterpretation) { vgrow = Priority.NEVER }
        }
        field("Частотность") { textfield(viewModel.pFrequency) }
        field("Класс") { textfield(viewModel.pGrade) }
        field("Уровень JLPT") {
            combobox(viewModel.pJlptLevel, JlptLevel.getNames()).required(message = requiredMsg)
        }
    }

    private fun renderKanjiReadingBorderPane() = borderpane {
        var btnEditKanjiReading: Button by singleAssign()
        var btnDeleteKanjiReading: Button by singleAssign()
        paddingTop = 5
        top = label("Чтения")
        center = tableview(viewModel.kanjiReadings) {
            maxHeight = 128.0
            column("Чтение", KanjiReadingModel::pReading)
            column("Приоритет", KanjiReadingModel::pPriority)
            column("Анахроизм", KanjiReadingModel::pIsAnachronism)
            column("Вид чтения", KanjiReadingModel::pReadingType)
            smartResize()
            onSelectionChange { kanjiReading ->
                (kanjiReading !is KanjiReadingModel).let {
                    btnEditKanjiReading.isDisable = it
                    btnDeleteKanjiReading.isDisable = it
                }
                viewModel.selectedKanjiReading = kanjiReading
            }
            onUserSelect(2) { viewModel.onKanjiReadingEditClick() }
        }
        bottom = hbox(10) {
            paddingTop = 5
            paddingBottom = 5
            button("Добавить") {
                addClass(mediumButton)
                action { viewModel.onKanjiReadingAddClick() }
            }
            btnEditKanjiReading = button("Изменить") {
                addClass(mediumButton)
                action { viewModel.onKanjiReadingEditClick() }
            }
            btnDeleteKanjiReading = button("Удалить") {
                addClass(mediumButton)
                action { viewModel.onKanjiReadingDeleteClick() }
            }
        }
    }

    private fun renderKanjiComponentBorderPane() = borderpane {
        left = vbox {
            label("Составные")
            hbox(10) {
                button("Добавить") {
                    addClass(mediumButton)
                }.action { viewModel.onKanjiSearchClick() }
                button("Изменить") {
                    addClass(mediumButton)
                }.action { viewModel.onEditComponentClick() }
            }
        }
        right = label(viewModel.pComponents) { alignment = Pos.BASELINE_LEFT }
    }

    private fun renderSaveHBox() = hbox {
        alignment = Pos.BOTTOM_RIGHT
        button("Сохранить") {
            enableWhen(viewModel.valid)
            action {
                viewModel.onSaveClick()
                close()
            }
            addClass(largeButton)
        }
    }
}