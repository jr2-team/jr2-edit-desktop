package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.MojiEntity
import tornadofx.getValue
import tornadofx.setValue

class Moji(
    id: Int = 0,
    value: String = "",
    strokeCount: Int = 0,
    onReading: String = "",
    kunReading: String = "",
    basicInterpretation: String = "",
    jlptLevel: Int = 0,
    mojiType: Int = 1
) {
    val idProp = SimpleIntegerProperty(id)
    var id by idProp

    val valueProp = SimpleStringProperty(value)
    var value by valueProp

    val basicInterpretationProp = SimpleStringProperty(basicInterpretation)
    var basicInterpretation by basicInterpretationProp

    companion object {
        fun fromEntity(mojiEntity: MojiEntity) = with(mojiEntity) {
            Moji(id.value, value, basicInterpretation = basicInterpretation)
        }
    }
}