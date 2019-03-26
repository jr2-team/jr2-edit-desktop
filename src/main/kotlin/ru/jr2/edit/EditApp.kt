package ru.jr2.edit

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import javafx.stage.Stage
import ru.jr2.edit.data.db.AppDatabase
import ru.jr2.edit.presentation.RootView
import tornadofx.App
import tornadofx.importStylesheet

class EditApp : App(RootView::class, Style::class) {
    val db = AppDatabase().db
    val xmlMapper = XmlMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        registerKotlinModule()
    }

    init {
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