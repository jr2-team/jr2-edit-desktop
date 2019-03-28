package ru.jr2.edit.presentation

import javafx.scene.control.TabPane
import javafx.stage.StageStyle
import ru.jr2.edit.presentation.group.view.GroupListView
import ru.jr2.edit.presentation.kanji.view.list.KanjiListView
import ru.jr2.edit.presentation.kanji.view.parser.KanjiParserFragment
import ru.jr2.edit.presentation.sentence.view.SentenceListView
import ru.jr2.edit.presentation.word.view.WordListView
import ru.jr2.edit.presentation.word.view.WordParseFragment
import ru.jr2.edit.util.TabType
import tornadofx.*

class RootView : View("JR2-Edit") {
    private val kanjiListView: KanjiListView by inject()
    private val wordListView: WordListView by inject()
    private val sentenceListView: SentenceListView by inject()
    private val groupListView: GroupListView by inject()

    private var contentTabPane: TabPane = TabPane()
    private val openedTabs = mutableListOf<TabType>()

    override val root = borderpane {
        top = renderMenuBar()
        center = contentTabPane
    }

    private fun renderMenuBar() = menubar {
        menu("Добавить вкладку") {
            item(TabType.KANJI_TAB.name).action { addTab(kanjiListView, TabType.KANJI_TAB) }
            item(TabType.WORD_TAB.name).action { addTab(wordListView, TabType.WORD_TAB) }
            item(TabType.SENTENCE_TAB.name).action { addTab(sentenceListView, TabType.SENTENCE_TAB) }
            item(TabType.GROUP_TAB.name).action { addTab(groupListView, TabType.KANJI_TAB) }
        }
        menu("Инструменты") {
            item("Парсер JMdict").action {
                find<WordParseFragment>().openModal(
                    StageStyle.UTILITY,
                    escapeClosesWindow = false,
                    resizable = false
                )
            }
            item("Парсер kanjidic2.xml").action {
                find<KanjiParserFragment>().openModal(
                    StageStyle.UTILITY,
                    escapeClosesWindow = false,
                    resizable = false
                )
            }
        }
    }

    private fun addTab(view: View, tabType: TabType) {
        if (!openedTabs.contains(tabType)) {
            contentTabPane.tab(text = tabType.name) {
                add(view)
                setOnCloseRequest { openedTabs.remove(tabType) }
            }
            openedTabs.add(tabType)
        }
    }
}