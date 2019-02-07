package ru.jr2.edit

import javafx.stage.StageStyle
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.expandArgs
import org.slf4j.LoggerFactory
import tornadofx.Fragment

object KotlinLoggingSqlLogger : SqlLogger {
    private val logger = LoggerFactory.getLogger(EditApp::class.java)
    override fun log(context: StatementContext, transaction: Transaction) {
        logger.info("SQL: ${context.expandArgs(transaction)}")
    }
}

inline fun <reified T : Fragment> T.openDialogFragment() {
    find<T>().openModal(StageStyle.UTILITY, resizable = false)
}

fun Query.andWhere(andPart: SqlExpressionBuilder.() -> Op<Boolean>) = adjustWhere {
    val expr = Op.build { andPart() }
    if (this == null) {
        expr
    } else {
        this and expr
    }
}