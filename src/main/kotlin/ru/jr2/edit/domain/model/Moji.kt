package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.JlptLevel
import ru.jr2.edit.domain.MojiType
import ru.jr2.edit.domain.entity.MojiEntity
import tornadofx.getValue
import tornadofx.setValue

class Moji(
    id: Int = 0,
    value: String = String(),
    strokeCount: Int = 0,
    onReading: String = String(),
    kunReading: String = String(),
    interpretation: String = String(),
    jlptLevel: String = JlptLevel.NO_LEVEL.str,
    mojiType: String = MojiType.KANJI.str
) : BaseModel(id, value) {
    val pStrokeCount = SimpleIntegerProperty(strokeCount)
    var strokeCount: Int by pStrokeCount

    val pOnReading = SimpleStringProperty(onReading)
    var onReading: String by pOnReading

    val pKunReading = SimpleStringProperty(kunReading)
    var kunReading: String by pKunReading

    val pInterpretation = SimpleStringProperty(interpretation)
    var interpretation: String by pInterpretation

    val pJlptLevel = SimpleStringProperty(jlptLevel)
    var jlptLevel: String by pJlptLevel

    val pMojiType = SimpleStringProperty(mojiType)
    var mojiType: String by pMojiType

    companion object {
        fun fromEntity(mojiEntity: MojiEntity): Moji = with(mojiEntity) {
            Moji(
                id.value,
                value,
                strokeCount,
                onReading ?: String(),
                kunReading ?: String(),
                interpretation ?: String(),
                JlptLevel.fromCode(jlptLevel).str,
                MojiType.fromCode(mojiType).str
            )
        }
    }
}