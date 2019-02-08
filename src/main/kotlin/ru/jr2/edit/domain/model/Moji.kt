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

    val strokeCountProp = SimpleIntegerProperty(strokeCount)
    var strokeCount by strokeCountProp

    val onReadingProp = SimpleStringProperty(onReading)
    var onReading by onReadingProp

    val kunReadingProp = SimpleStringProperty(kunReading)
    var kunReading by kunReadingProp

    val basicInterpretationProp = SimpleStringProperty(basicInterpretation)
    var basicInterpretation by basicInterpretationProp

    val jlptLevelProp = SimpleIntegerProperty(jlptLevel)
    var jlptLevel by jlptLevelProp

    val mojiTypeProp = SimpleIntegerProperty(mojiType)
    var mojiType by mojiTypeProp

    override fun toString(): String {
        return "$id $value"
    }

    companion object {
        fun fromEntity(mojiEntity: MojiEntity): Moji = with(mojiEntity) {
            return Moji(
                id.value,
                value,
                strokeCount,
                onReading ?: "",
                kunReading ?: "",
                basicInterpretation,
                jlptLevel ?: 0,
                mojiType
            )
        }
    }
}