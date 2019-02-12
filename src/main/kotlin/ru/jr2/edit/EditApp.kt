package ru.jr2.edit

import DbSetting
import javafx.stage.Stage
import org.jetbrains.exposed.sql.transactions.TransactionManager
import ru.jr2.edit.presentation.view.RootView
import tornadofx.App
import tornadofx.importStylesheet
import java.sql.Connection.TRANSACTION_READ_UNCOMMITTED

class EditApp : App(RootView::class, Style::class) {
    val db = DbSetting.db

    init {
        TransactionManager.manager.defaultIsolationLevel = TRANSACTION_READ_UNCOMMITTED
        instance = this
    }

    override fun start(stage: Stage) {
        importStylesheet(Style::class)
        super.start(stage)
    }

    companion object {
        lateinit var instance: EditApp
    }
}