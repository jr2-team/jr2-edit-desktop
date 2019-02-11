package ru.jr2.edit.presentation.view.moji

import javafx.geometry.Orientation.VERTICAL
import javafx.geometry.Pos
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.layout.Priority
import ru.jr2.edit.domain.JlptLevel
import ru.jr2.edit.domain.MojiType
import ru.jr2.edit.presentation.viewmodel.moji.MojiEditViewModel
import tornadofx.*

class MojiEditFragment : Fragment() {
    private val viewModel: MojiEditViewModel

    val paramMojiId: Int by param(0)

    init {
        viewModel = MojiEditViewModel(paramMojiId)
        title = if (paramMojiId == 0) "Добавить моджи" else "Редактировать моджи"
    }

    override fun onBeforeShow() {
        super.onBeforeShow()
        currentStage?.setOnCloseRequest {
            showCloseWindowWarning()
            it.consume()
        }
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
                        required(message = "Обязательное поле")
                        filterInput {
                            with(it.controlNewText) {
                                return@filterInput isInt() && this.toInt() > 0
                            }
                        }
                    }
                }
                field("Онные чтения") {
                    textarea(viewModel.pOnReading) {
                        prefRowCount = 3
                        vgrow = Priority.NEVER
                        isWrapText = true
                        this.maxWidth = 300.0
                    }
                }
                field("Кунны чтения") {
                    textarea(viewModel.pKunReading) {
                        prefRowCount = 3
                        vgrow = Priority.NEVER
                        isWrapText = true
                        this.maxWidth = 300.0
                    }
                }
                field("Основные переводы") {
                    textarea(viewModel.pBasicInterpretation) {
                        prefRowCount = 3
                        vgrow = Priority.NEVER
                        isWrapText = true
                        this.maxWidth = 300.0
                    }
                }
                field("Уровень JLPT") {
                    combobox(viewModel.pJlptLevel, JlptLevel.getNames()) {
                        required(message = "Обязательное поле")
                    }
                }
                field("Вид моджи") {
                    combobox(viewModel.pMojiType, MojiType.getNames()) {
                        required(message = "Обязательное поле")
                    }
                }
            }
            borderpane {
                left = vbox {
                    label("Составные")
                    hbox(10.0) {
                        button("Добавить") {
                            action { viewModel.onMojiSearchClick() }
                        }
                        button("Изменить") {
                            action { viewModel.onEditComponentClick() }
                        }
                    }
                }
                right = label(viewModel.pComponents) {
                    style {
                        alignment = Pos.BASELINE_LEFT
                    }
                }
            }
        }
        bottom = hbox {
            button("Сохранить") {
                setMinSize(120.0, 28.0)
                enableWhen(viewModel.valid)
                action {
                    viewModel.commit { viewModel.onSaveClick() }
                    close()
                }
            }
            alignment = Pos.BOTTOM_RIGHT
        }
        paddingAll = 10.0
        vgrow = Priority.NEVER
    }

    private fun showCloseWindowWarning() = warning(
        "Закрыть без сохранения",
        "Вы уверены, что хотите закрыть не сохраняя изменения?",
        ButtonType.OK, ButtonType.CANCEL,
        title = "Закрыть без сохранения",
        actionFn = {
            when (it.buttonData) {
                ButtonBar.ButtonData.OK_DONE -> currentStage?.close()
                else -> this.close()
            }
        }
    )
}