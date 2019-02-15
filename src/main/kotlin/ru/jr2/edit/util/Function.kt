package ru.jr2.edit.util

import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import tornadofx.warning
import kotlin.coroutines.coroutineContext

// tornadofx
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

// coroutines
suspend fun withLoader(loader: Job, f: suspend () -> Unit) = with(coroutineContext) {
    loader.start()
    f.invoke()
    loader.cancelAndJoin()
}