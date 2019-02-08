package ru.jr2.edit.util

import javafx.stage.StageStyle
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and
import tornadofx.Fragment

// tornadofx
inline fun <reified T : Fragment> T.openDialogFragment() {
    find<T>().openModal(StageStyle.UTILITY, resizable = false)
}

// exposed
fun Query.andWhere(andPart: SqlExpressionBuilder.() -> Op<Boolean>) = adjustWhere {
    val expr = Op.build { andPart() }
    if (this == null) {
        expr
    } else {
        this and expr
    }
}