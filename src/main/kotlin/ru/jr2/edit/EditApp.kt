package ru.jr2.edit

import DbSetting
import org.jetbrains.exposed.sql.transactions.TransactionManager
import ru.jr2.edit.presentation.view.RootView
import tornadofx.App
import java.sql.Connection.TRANSACTION_READ_UNCOMMITTED

class EditApp : App(RootView::class) {
    val db = DbSetting.db

    init {
        TransactionManager.manager.defaultIsolationLevel = TRANSACTION_READ_UNCOMMITTED
        instance = this
    }

    companion object {
        lateinit var instance: EditApp
    }
}