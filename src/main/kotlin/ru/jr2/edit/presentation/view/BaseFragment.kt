package ru.jr2.edit.presentation.view

import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import tornadofx.Fragment
import tornadofx.warning

abstract class BaseFragment : Fragment() {
    override fun onBeforeShow() {
        super.onBeforeShow()
        currentStage?.setOnCloseRequest {
            showCloseWindowWarning()
            it.consume()
        }
    }

    override fun onDock() {
        root.requestFocus()
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

    companion object {
        internal const val requiredMsg = "Обязательное поле"
    }
}