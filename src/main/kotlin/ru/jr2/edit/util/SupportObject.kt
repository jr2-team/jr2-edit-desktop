package ru.jr2.edit.util

import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.expandArgs
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import ru.jr2.edit.EditApp

object KotlinLoggingSqlLogger : SqlLogger {
    private val logger = LoggerFactory.getLogger(EditApp::class.java)
    override fun log(context: StatementContext, transaction: Transaction) {
        logger.info("SQL: ${context.expandArgs(transaction)}")
    }
}

val MinDateTime = DateTime(2019, 0, 0, 0, 0)