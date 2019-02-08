package ru.jr2.edit.presentation.viewmodel

import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.presentation.viewmodel.word.WordSavedEvent
import tornadofx.ViewModel

class RootViewModel : ViewModel() {
    val wordSavedMessage = SimpleStringProperty()

    init {
        subscribe<WordSavedEvent> {
            wordSavedMessage.value = it.message
        }
    }
}