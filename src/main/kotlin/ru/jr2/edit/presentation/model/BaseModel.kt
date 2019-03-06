package ru.jr2.edit.presentation.model

import javafx.beans.property.SimpleIntegerProperty
import tornadofx.getValue
import tornadofx.setValue

abstract class BaseModel(id: Int) {
    val pId = SimpleIntegerProperty(id)
    var id: Int by pId
}