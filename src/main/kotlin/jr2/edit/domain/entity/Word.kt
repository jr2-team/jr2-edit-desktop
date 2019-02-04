package jr2.edit.domain.entity

import jr2.edit.data.db.dao.WordDao
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Word(id: EntityID<Int>) : IntEntity(id) {
    var value: String by WordDao.value
    var furigana: String by WordDao.furigana
    var basicInterpretation: String by WordDao.basicInterpretation
    var jlptLevel: Int by WordDao.jlptLevel

    companion object : IntEntityClass<Word>(WordDao)

    override fun toString(): String = "$id.value $value"
}