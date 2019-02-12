package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

abstract class BaseModel(
    id: Int,
    value: String
) {
    val pId = SimpleIntegerProperty(id)
    var id: Int by pId

    val pValue = SimpleStringProperty(value)
    var value: String by pValue

    override fun toString() = "$id: $value"
}