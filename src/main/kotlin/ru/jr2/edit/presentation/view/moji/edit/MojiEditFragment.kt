package ru.jr2.edit.presentation.view.moji.edit

import javafx.geometry.Orientation.VERTICAL
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import ru.jr2.edit.Style.Companion.largeButton
import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.domain.JlptLevel
import ru.jr2.edit.domain.MojiType
import ru.jr2.edit.presentation.view.BaseFragment
import ru.jr2.edit.presentation.viewmodel.moji.MojiEditViewModel
import tornadofx.*

class MojiEditFragment : BaseFragment() {
    private val viewModel: MojiEditViewModel

    val paramMojiId: Int by param(0)

    init {
        viewModel = MojiEditViewModel(paramMojiId)
        title = if (paramMojiId == 0) "Добавить моджи" else "Редактировать моджи"
    }

    override val root = borderpane {
        top = form {
            label("Моджи")
            textfield(viewModel.pValue) {
                setMaxSize(48.0, 48.0)
                required(message = "Обязательное поле")
                filterInput {
                    with(it.controlNewText) { return@filterInput length == 1 }
                }
                style {
                    alignment = Pos.CENTER
                    fontSize = 18.px
                }
            }
            alignment = Pos.CENTER
        }

        center = form {
            fieldset {
                field("Количество черт", VERTICAL) {
                    textfield(viewModel.pStrokeCount) {
                        required(message = requiredMsg)
                        filterInput {
                            with(it.controlNewText) {
                                return@filterInput isInt() && this.toInt() > 0
                            }
                        }
                    }
                }
                field("Онные чтения") {
                    textarea(viewModel.pOnReading) {
                        vgrow = Priority.NEVER
                    }
                }
                field("Кунны чтения") {
                    textarea(viewModel.pKunReading) {
                        vgrow = Priority.NEVER
                    }
                }
                field("Основные переводы") {
                    textarea(viewModel.pInterpretation) {
                        vgrow = Priority.NEVER
                    }
                }
                field("Уровень JLPT") {
                    combobox(viewModel.pJlptLevel, JlptLevel.getNames())
                        .required(message = requiredMsg)
                }
                field("Вид моджи") {
                    combobox(viewModel.pMojiType, MojiType.getNames())
                        .required(message = requiredMsg)
                }
            }
            borderpane {
                left = vbox {
                    label("Составные")
                    hbox(10.0) {
                        button("Добавить") {
                            action { viewModel.onMojiSearchClick() }
                            addClass(mediumButton)
                        }
                        button("Изменить") {
                            action { viewModel.onEditComponentClick() }
                            addClass(mediumButton)
                        }
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
                    viewModel.commit { viewModel.onSaveClick() }
                    close()
                }
                addClass(largeButton)
            }
            alignment = Pos.BOTTOM_RIGHT
        }
        paddingAll = 10.0
    }
}