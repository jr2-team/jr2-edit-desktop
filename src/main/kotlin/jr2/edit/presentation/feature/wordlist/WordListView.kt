package jr2.edit.presentation.feature.wordlist

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import jr2.edit.EditApp
import jr2.edit.domain.entity.Word
import jr2.edit.presentation.feature.word.WordSavedEvent
import org.jetbrains.exposed.sql.transactions.transaction
import tornadofx.View
import tornadofx.ViewModel
import tornadofx.column
import tornadofx.tableview

class WordListView : View() {
    private val viewModel: WordListViewModel by inject()

    override val root = tableview(viewModel.words) {
        column("Value", Word::value)
        column("Furigana", Word::furigana)
    }
}

class WordListViewModel : ViewModel() {
    var words: ObservableList<Word> =
        FXCollections.observableArrayList(mutableListOf())

    init {
        fetchWords()
        subscribe<WordSavedEvent> {
            fetchWords()
        }
    }

    private fun fetchWords() {
        transaction(EditApp.instance.db) {
            words.clear()
            words.addAll(Word.all())
        }
    }
}