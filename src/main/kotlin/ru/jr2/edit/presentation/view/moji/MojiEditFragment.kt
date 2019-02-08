package ru.jr2.edit.presentation.view.moji

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation.VERTICAL
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import ru.jr2.edit.presentation.viewmodel.moji.MojiEditViewModel
import tornadofx.*

class MojiEditFragment : Fragment() {
    private val viewModel: MojiEditViewModel

    val mojiIdParam: Int by param(0)

    init {
        viewModel = MojiEditViewModel(mojiIdParam)
        if (mojiIdParam == 0) {
            this.title = "Добавить моджи"
        } else {
            this.title = "Редактировать моджи"
        }
    }

    override val root = borderpane {
        top = form {
            label("Моджи")
            textfield(viewModel.value) {
                filterInput { it.controlNewText.length == 1 }
                setMaxSize(48.0, 48.0)
                style {
                    alignment = Pos.CENTER
                    fontSize = 18.px
                }
                required(message = "Обязательное поле")
            }
            style {
                alignment = Pos.CENTER
            }
        }
        center = form {
            fieldset {
                field("Количество черт", VERTICAL) {
                    textfield(viewModel.strokeCount) {
                        required(message = "Обязательное поле")
                    }
                }
                field("Онные чтения") {
                    textarea(viewModel.onReading) {
                        prefRowCount = 3
                        vgrow = Priority.NEVER
                        isWrapText = true
                    }
                }
                field("Кунны чтения") {
                    textarea(viewModel.kunReading) {
                        prefRowCount = 3
                        vgrow = Priority.NEVER
                        isWrapText = true
                    }
                }
                field("Основные переводы") {
                    textarea(viewModel.basicInterpretation) {
                        prefRowCount = 3
                        vgrow = Priority.NEVER
                        isWrapText = true
                    }
                }
                field("Уровень JLPT") {
                    textfield(viewModel.jlptLevel) { }
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
                            action { viewModel.onShowMojiSearchFragment() }
                        }
                        button("Изменить") {
                            action { viewModel.onShowMojiEditComponentFragment() }
                        }
                    }
                }
                right = label(viewModel.mojiComponentStringProp) {
                    style {
                        alignment = Pos.BASELINE_LEFT
                    }
                }
            }
        }
        bottom = hbox {
            button("Сохранить") {
                enableWhen(viewModel.valid)
                action {
                    viewModel.commit { viewModel.onSaveClick() }
                    close()
                }
                setMinSize(120.0, 28.0)
            }
            alignment = Pos.BOTTOM_RIGHT
        }
        paddingAll = 15.0
        vgrow = Priority.ALWAYS
    }
}