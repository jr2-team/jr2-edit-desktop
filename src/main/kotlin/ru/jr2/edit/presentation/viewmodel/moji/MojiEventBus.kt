package ru.jr2.edit.presentation.viewmodel.moji

import ru.jr2.edit.domain.model.Moji
import tornadofx.FXEvent

class MojiSelectedEvent(val moji: Moji) : FXEvent()
class MojiSavedEvent(val isSaved: Boolean) : FXEvent()