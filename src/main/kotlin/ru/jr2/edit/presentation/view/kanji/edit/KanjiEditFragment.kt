package ru.jr2.edit.presentation.view.kanji.edit

import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import ru.jr2.edit.Style.Companion.largeButton
import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.domain.model.KanjiReading
import ru.jr2.edit.presentation.view.BaseEditFragment
import ru.jr2.edit.presentation.viewmodel.kanji.KanjiEditViewModel
import tornadofx.*

class KanjiEditFragment : BaseEditFragment<Kanji, KanjiEditViewModel>() {
    override val viewModel = KanjiEditViewModel(paramItemId)

    override val root = borderpane {
        paddingAll = 10.0
        center = form {
            add(renderKanjiPropertiesFieldSet())
            add(renderReadingTable())
            borderpane {
                left = vbox {
                    label("Составные")
                    hbox(10.0) {
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
        }
        bottom = hbox {
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
        field("Частотность") {
            textfield(viewModel.pFrequency)
        }
        field("Класс") {
            textfield(viewModel.pGrade)
        }
        field("Уровень JLPT") {
            combobox(viewModel.pJlptLevel, JlptLevel.getNames())
                .required(message = requiredMsg)
        }
    }

    private fun renderReadingTable() = borderpane {
        top = label("Чтения")
        center = tableview(viewModel.readings) {
            maxHeight = 128.0
            column("Чтение", KanjiReading::reading)
            column("Приоритет", KanjiReading::priority)
            column("Анахроизм", KanjiReading::isAnachronism)
            column("Вид чтения", KanjiReading::readingType)
        }
        bottom = borderpane {
            left = buttonbar {
                button("Добавить") {
                    addClass(mediumButton)
                }.action { viewModel.onKanjiReadingAddClick() }
                button("Изменить") {
                    addClass(mediumButton)
                }.action { viewModel.onKanjiReadingEditClick() }
                button("Удалить") {
                    addClass(mediumButton)
                }.action { viewModel.onKanjiReadingDeleteClick() }
            }
            paddingTop = 10.0
        }
    }
}