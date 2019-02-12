package ru.jr2.edit.presentation.view

import ru.jr2.edit.util.showWarningMsg
import tornadofx.Fragment

abstract class BaseEditFragment(baseModelName: String = "") : Fragment() {
    val baseModelId: Int by param(0)

    init {
        // TODO: Использовать baseModelName - плохо, поскольку добавляет семантическое поведения
        title = if (baseModelId == 0) {
            "Добавить $baseModelName"
        } else {
            "Редактировать $baseModelName"
        }
    }

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

    private fun showCloseWindowWarning() = showWarningMsg(
        "Закрыть без сохранения",
        "Вы уверены, что хотите закрыть не сохраняя изменения?"
    ) { currentStage?.close() }

    companion object {
        internal const val requiredMsg = "Обязательное поле"
    }
}