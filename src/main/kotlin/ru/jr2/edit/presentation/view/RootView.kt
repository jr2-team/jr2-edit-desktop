package ru.jr2.edit.presentation.view

import ru.jr2.edit.presentation.view.moji.MojiListView
import ru.jr2.edit.presentation.view.word.WordListView
import ru.jr2.edit.presentation.viewmodel.RootViewModel
import tornadofx.View
import tornadofx.tab
import tornadofx.tabpane

class RootView : View("JR2-Edit") {
    private val viewModel: RootViewModel by inject()

    private val wordListView: WordListView by inject()
    private val mojiListView: MojiListView by inject()

    override val root = tabpane {
        tab("Слова") {
            isClosable = false
            add(wordListView)
        }
        tab("Канджи") {
            isClosable = false
            add(mojiListView)
        }
        tab("Предложения") {

        }
    }
}