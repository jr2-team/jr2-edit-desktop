package ru.jr2.edit.util

import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import tornadofx.warning

fun showWarningMsg(
    title: String = "",
    content: String,
    okAction: () -> Unit
) = warning(
    title, content, ButtonType.OK, ButtonType.CANCEL,
    title = title,
    actionFn = {
        when (it.buttonData) {
            ButtonBar.ButtonData.OK_DONE -> okAction.invoke()
            else -> this.close()
        }
    }
)