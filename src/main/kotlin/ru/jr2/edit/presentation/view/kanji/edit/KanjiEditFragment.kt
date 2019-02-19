package ru.jr2.edit.presentation.view.kanji.edit

import javafx.collections.ObservableList
import javafx.geometry.Orientation.VERTICAL
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import ru.jr2.edit.Style.Companion.largeButton
import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.model.KanjiReading
import ru.jr2.edit.domain.model.Kanji
import ru.jr2.edit.presentation.view.BaseEditFragment
import ru.jr2.edit.presentation.viewmodel.kanji.KanjiEditViewModel
import tornadofx.*

class KanjiEditFragment : BaseEditFragment<Kanji, KanjiEditViewModel>() {
    override val viewModel = KanjiEditViewModel(paramItemId)

    override val root = borderpane {
        top = form {
            label("Канджи")
            textfield(viewModel.pKanji) {
                setMaxSize(48.0, 48.0)
                filterInput {
                    with(it.controlNewText) { length == 1 }
                }
                style {
                    alignment = Pos.CENTER
                    fontSize = 18.px
                }
            }.required(message = requiredMsg)
            alignment = Pos.CENTER
        }
        center = form {
            fieldset {
                field("Количество черт", VERTICAL) {
                    textfield(viewModel.pStrokeCount) {
                        filterInput {
                            with(it.controlNewText) { isInt() && toInt() > 0 }
                        }
                    }.required(message = requiredMsg)
                }
                add(createReadingTable(viewModel.readings, "Чтения"))
                field("Основные переводы") {
                    textarea(viewModel.pInterpretation) { vgrow = Priority.NEVER }
                }
                field("Уровень JLPT") {
                    combobox(viewModel.pJlptLevel, JlptLevel.getNames())
                        .required(message = requiredMsg)
                }
            }
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
                right = label(viewModel.pComponents) {
                    alignment = Pos.BASELINE_LEFT
                }
            }
        }
        bottom = hbox {
            button("Сохранить") {
                enableWhen(viewModel.valid)
                action {
                    viewModel.onSaveClick()
                    close()
                }
                addClass(largeButton)
            }
            alignment = Pos.BOTTOM_RIGHT
        }
        paddingAll = 10.0
    }

    private fun createReadingTable(readings: ObservableList<KanjiReading>, title: String) = vbox {
        label(title)
        tableview(readings) {
            column("Чтение", KanjiReading::reading)
            column("Приоритет", KanjiReading::priority)
            column("Анахроизм", KanjiReading::isAnachronism)
            column("Вид чтения", KanjiReading::readingType)
            this.maxHeight = 128.0
        }
    }
}