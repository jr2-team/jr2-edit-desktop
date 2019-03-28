package ru.jr2.edit

import javafx.scene.Scene
import javafx.stage.Stage
import ru.jr2.edit.presentation.RootView
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.importStylesheet

class EditApp : App(RootView::class, Style::class) {
    override fun start(stage: Stage) {
        importStylesheet(Style::class)
        super.start(stage)
    }

    override fun createPrimaryScene(view: UIComponent) = Scene(view.root, 800.0, 600.0)
}