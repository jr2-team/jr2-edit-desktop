package ru.jr2.edit.domain.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import ru.jr2.edit.domain.entity.KanjiReadingEntity
import ru.jr2.edit.domain.entity.MojiEntity
import ru.jr2.edit.domain.misc.JlptLevel
import ru.jr2.edit.domain.misc.MojiType
import tornadofx.getValue
import tornadofx.setValue

class Moji(id: Int = 0) : BaseModel(id) {
    val pMoji = SimpleStringProperty()
    var moji: String by pMoji

    val pStrokeCount = SimpleIntegerProperty()
    var strokeCount: Int by pStrokeCount

    val pOnReading = SimpleStringProperty()
    var onReading: String by pOnReading

    val pKunReading = SimpleStringProperty()
    var kunReading: String by pKunReading

    val pInterpretation = SimpleStringProperty()
    var interpretation: String by pInterpretation

    val pJlptLevel = SimpleStringProperty(JlptLevel.JLPT1.str)
    var jlptLevel: String by pJlptLevel

    val pMojiType = SimpleStringProperty(MojiType.KANJI.str)
    var mojiType: String by pMojiType

    val pFrequency = SimpleIntegerProperty()
    var frequency: Int by pFrequency

    val pGrade = SimpleIntegerProperty()
    var grade: Int by pGrade

    var svg: String? = null

    companion object {
        fun fromEntity(
            mojiEntity: MojiEntity,
            kanjiReadingEntities: List<KanjiReadingEntity>? = null
        ): Moji {
            val moji = Moji(mojiEntity.id.value).apply {
                moji = mojiEntity.moji
                strokeCount = mojiEntity.strokeCount
                interpretation = mojiEntity.interpretation ?: String()
                frequency = mojiEntity.frequency
                grade = mojiEntity.grade ?: 0
                svg = mojiEntity.svg
                jlptLevel = JlptLevel.fromCode(mojiEntity.jlptLevel).str // TODO: Переделать
                mojiType = MojiType.fromCode(mojiEntity.mojiType).str
            }
            /**
             * Если у моджи есть Он-/Кун- чтения
             */
            if (kanjiReadingEntities is List<KanjiReadingEntity>) {
                moji.onReading = kanjiReadingEntities
                    .filter { it.readingType == 0 }
                    .joinToString { it.reading }
                moji.kunReading = kanjiReadingEntities
                    .filter { it.readingType == 1 }
                    .joinToString { it.reading }
            }
            return moji
        }
    }
}