package jr2.edit.presentation.feature.root

import javafx.beans.property.SimpleStringProperty
import jr2.edit.presentation.feature.word.WordSavedEvent
import jr2.edit.presentation.feature.word.WordView
import jr2.edit.presentation.feature.wordlist.WordListView
import tornadofx.View
import tornadofx.label
import tornadofx.separator
import tornadofx.vbox

class RootView : View("JR2-Edit") {
    val wordView: WordView by inject()
    val wordListView: WordListView by inject()

    val wordSavedMessage = SimpleStringProperty()

    init {
        subscribe<WordSavedEvent> {
            wordSavedMessage.value = it.message
        }
    }

    override val root = vbox {
        add(wordView)
        separator()
        label(wordSavedMessage)
        add(wordListView)
    }
}