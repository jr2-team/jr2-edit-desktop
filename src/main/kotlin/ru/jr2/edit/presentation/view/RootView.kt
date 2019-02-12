package ru.jr2.edit.presentation.view

import ru.jr2.edit.presentation.view.moji.list.MojiListView
import ru.jr2.edit.presentation.view.word.WordListView
import tornadofx.View
import tornadofx.tab
import tornadofx.tabpane

class RootView : View("JR2-Edit") {
    private val wordListView: WordListView by inject()
    private val mojiListView: MojiListView by inject()

    override val root = tabpane {
        tab("Кана") {
            isClosable = false
        }
        tab("Моджи") {
            isClosable = false
            add(mojiListView)
        }
        tab("Слова") {
            isClosable = false
            add(wordListView)
        }
        tab("Предложения") {
            isClosable = false
        }
    }
}