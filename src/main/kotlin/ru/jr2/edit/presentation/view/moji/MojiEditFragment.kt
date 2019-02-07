package ru.jr2.edit.presentation.view.moji

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation.VERTICAL
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import ru.jr2.edit.presentation.viewmodel.moji.MojiEditItemViewModel
import tornadofx.*

class MojiEditFragment : Fragment() {
    private val itemViewModel: MojiEditItemViewModel

    val mojiIdParam: Int by param(1)

    init {
        itemViewModel = MojiEditItemViewModel(mojiIdParam)
        if (mojiIdParam == 0) {
            this.title = "Добавить моджи"
        } else {
            this.title = "Редактировать моджи"
        }
    }

    override val root = borderpane {
        top = form {
            label("Моджи")
            textfield(itemViewModel.value) {
                filterInput { it.controlNewText.length == 1 }
                setMaxSize(48.0, 48.0)
                style {
                    alignment = Pos.CENTER
                    fontSize = 18.px
                }
            }
            style {
                alignment = Pos.CENTER
            }
        }
        center = form {
            fieldset {
                field("Количество черт", VERTICAL) {
                    textfield(itemViewModel.strokeCount) { }
                }
                field("Онные чтения") {
                    textarea(itemViewModel.onReading) {
                        prefRowCount = 3
                        vgrow = Priority.NEVER
                        isWrapText = true
                    }
                }
                field("Кунны чтения") {
                    textarea(itemViewModel.kunReading) {
                        prefRowCount = 3
                        vgrow = Priority.NEVER
                        isWrapText = true
                    }
                }
                field("Основны переводы") {
                    textarea {
                        prefRowCount = 3
                        vgrow = Priority.NEVER
                        isWrapText = true
                    }
                }
                field("Уровень JLPT") {
                    textfield { }
                }
                field("Вид моджи") {
                    combobox(
                        SimpleStringProperty(),
                        observableList("Радикал", "Канджи")
                    )
                }
            }
            borderpane {
                left = vbox {
                    label("Составные")
                    hbox(10.0) {
                        button("Добавить") {
                            action { itemViewModel.onShowMojiSearchFragment() }
                        }
                        button("Изменить") {

                        }
                    }
                }
                right = label(itemViewModel.mojiComponentStringProp) {
                    style {
                        alignment = Pos.BASELINE_LEFT
                    }
                }
            }
        }
        bottom = hbox {
            button("Сохранить") {
                action { close() }
                setMinSize(120.0, 28.0)
            }
            alignment = Pos.BOTTOM_RIGHT
        }
        paddingAll = 15.0
        vgrow = Priority.ALWAYS
    }
}