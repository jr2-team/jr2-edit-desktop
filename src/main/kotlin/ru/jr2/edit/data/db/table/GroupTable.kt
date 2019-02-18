package ru.jr2.edit.data.db.table

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE

object GroupTable : IntIdTable("group") {
    val name = varchar("name", 100)
    val isUserGroup = bool("is_user_group").default(false)
    val groupType = integer("group_type")
    val creationDate = datetime("creation_date")
    val lastStudyDate = datetime("last_study_date").nullable()
    val section = reference("section_id", SectionTable, CASCADE, CASCADE).nullable()
}