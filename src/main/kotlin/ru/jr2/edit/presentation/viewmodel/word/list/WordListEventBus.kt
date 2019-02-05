package ru.jr2.edit.presentation.viewmodel.word.list

import ru.jr2.edit.domain.model.Word
import tornadofx.FXEvent

class WordSelectedEvent(val word: Word) : FXEvent()