package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.KotlinLoggingSqlLogger
import ru.jr2.edit.data.db.table.ComponentKanjiTable
import ru.jr2.edit.data.db.table.MojiTable
import ru.jr2.edit.domain.entity.MojiEntity
import ru.jr2.edit.domain.model.Moji

class MojiDbRepository(
    private val db: Database = EditApp.instance.db
) {
    fun getById(id: Int): Moji = transaction(db) {
        addLogger(KotlinLoggingSqlLogger)
        return@transaction Moji.fromEntity(MojiEntity[id])
    }

    fun getAll(): List<Moji> = transaction(db) {
        return@transaction MojiEntity.all().map { Moji.fromEntity(it) }
    }

    fun getComponentsOfKanji(mojiId: Int): List<Moji> = transaction(db) {
        val componentAlias = ComponentKanjiTable.alias("component_moji")
        return@transaction MojiTable
            .innerJoin(
                componentAlias,
                { id },
                { componentAlias[ComponentKanjiTable.mojiComponentId] }
            )
            .select {
                componentAlias[ComponentKanjiTable.moji] eq EntityID(mojiId, MojiTable)
            }
            .map {
                Moji.fromEntity(MojiEntity.wrapRow(it))
            }
    }

    // TODO: Придумать мхеанизм поиска, перевод каны в романджи?
    fun getBySearchQuery(query: String): List<Moji> = transaction(db) {
        return@transaction MojiTable
            .select {
                MojiTable.basicInterpretation.upperCase() like "%$query%".toUpperCase()
            }
            .map {
                Moji.fromEntity(MojiEntity.wrapRow(it))
            }
    }
}