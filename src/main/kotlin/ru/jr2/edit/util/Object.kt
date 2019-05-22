package ru.jr2.edit.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.expandArgs
import org.slf4j.LoggerFactory
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.AppDatabase
import com.ctc.wstx.api.WstxInputProperties
import com.ctc.wstx.stax.WstxInputFactory


object SqlLogger : SqlLogger {
    private val logger = LoggerFactory.getLogger(EditApp::class.java)
    override fun log(context: StatementContext, transaction: Transaction) {
        logger.info("SQL: ${context.expandArgs(transaction)}")
    }
}

object Injectable {
    val xmlMapper: XmlMapper by lazy {
        val inputFactory = WstxInputFactory().apply {
            setProperty(WstxInputProperties.P_MAX_ENTITY_COUNT, 2_000_000)
        }
        XmlMapper(inputFactory).apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            registerKotlinModule()
        }
    }

    val db = AppDatabase().db
}