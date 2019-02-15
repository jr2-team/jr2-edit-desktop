package ru.jr2.edit.presentation.view

import ru.jr2.edit.domain.model.BaseModel
import ru.jr2.edit.presentation.viewmodel.BaseEditViewModel
import ru.jr2.edit.util.showWarningMsg
import tornadofx.Fragment

abstract class BaseEditFragment<T : BaseModel, VT : BaseEditViewModel<T>>(
    titleCreate: String = "Создать запись",
    titleEdit: String = "Редактировать запись"
) : Fragment() {
    internal abstract val viewModel: VT
    val paramItemId: Int by param(0)

    init {
        title = if (paramItemId == 0) titleCreate else titleEdit
    }

    override fun onBeforeShow() {
        super.onBeforeShow()
        currentStage?.setOnCloseRequest {
            showCloseWindowWarning()
            it.consume()
        }
    }

    override fun onDock() {
        super.onDock()
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