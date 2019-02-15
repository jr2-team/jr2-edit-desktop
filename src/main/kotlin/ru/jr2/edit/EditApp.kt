package ru.jr2.edit

import DbSetting
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import javafx.stage.Stage
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import ru.jr2.edit.presentation.view.RootView
import tornadofx.App
import tornadofx.importStylesheet
import java.sql.Connection.TRANSACTION_READ_UNCOMMITTED

class EditApp : App(RootView::class, Style::class) {
    val db = DbSetting.db
    val xmlMapper = XmlMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        registerKotlinModule()
    }

    init {
        TransactionManager.manager.defaultIsolationLevel = TRANSACTION_READ_UNCOMMITTED
        instance = this
        DateTimeFormat.forPattern("dd.MM.yyyy")
    }

    override fun start(stage: Stage) {
        importStylesheet(Style::class)
        super.start(stage)
    }

    companion object {
        lateinit var instance: EditApp
    }
}