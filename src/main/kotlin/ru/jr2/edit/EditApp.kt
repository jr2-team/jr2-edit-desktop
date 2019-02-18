package ru.jr2.edit

import Jr2Database
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import javafx.stage.Stage
import org.jetbrains.exposed.sql.Database
import ru.jr2.edit.presentation.view.RootView
import tornadofx.App
import tornadofx.importStylesheet

class EditApp : App(RootView::class, Style::class) {
    val db: Database
    val xmlMapper = XmlMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        registerKotlinModule()
    }

    init {
        instance = this
        db = Jr2Database.db
    }

    override fun start(stage: Stage) {
        importStylesheet(Style::class)
        super.start(stage)
    }

    companion object {
        lateinit var instance: EditApp
    }
}