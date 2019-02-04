package jr2.edit.data.db.dao

import org.jetbrains.exposed.dao.IntIdTable

object WordDao: IntIdTable("Word") {
    val value = varchar("value", 100)
    val furigana = varchar("furigana", 500)
    val basicInterpretation = varchar("basicInterpretation", 500)
    val jlptLevel = integer("jlptLevel")
}