package ru.jr2.edit.presentation.view

import ru.jr2.edit.presentation.view.word.WordListView
import ru.jr2.edit.presentation.viewmodel.RootViewModel
import tornadofx.*

class RootView : View("JR2-Edit") {
    private val viewModel: RootViewModel by inject()

    private val wordListView: WordListView by inject()

    override val root = form {
        add(wordListView)
    }
}