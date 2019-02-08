package ru.jr2.edit.presentation.viewmodel.word

import ru.jr2.edit.domain.model.Word
import tornadofx.FXEvent

class WordSavedEvent(val message: String) : FXEvent()
class WordSelectedEvent(val word: Word) : FXEvent()