package jr2.edit

import DbSetting
import javafx.stage.Stage
import jr2.edit.presentation.feature.root.RootView
import org.jetbrains.exposed.sql.transactions.TransactionManager
import tornadofx.App
import java.sql.Connection

class EditApp : App(RootView::class) {
    val db = DbSetting.db

    init {
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_READ_UNCOMMITTED
        instance = this
    }

    companion object {
        lateinit var instance: EditApp
    }
}