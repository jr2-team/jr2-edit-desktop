package ru.jr2.edit.util

import javafx.stage.StageStyle
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and
import tornadofx.Fragment

// tornadofx
inline fun <reified T : Fragment> T.openDialogFragment(params: Map<*, Any?>? = null) {
    find<T>(params).openModal(StageStyle.UTILITY, resizable = false)
}

// exposed
fun Query.andWhere(andPart: SqlExpressionBuilder.() -> Op<Boolean>) = adjustWhere {
    val expr = Op.build { andPart() }
    if (this !is Op<Boolean>) {
        expr
    } else {
        this and expr
    }
}

// coroutines
suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { withContext(coroutineContext) { f(it) } }
}