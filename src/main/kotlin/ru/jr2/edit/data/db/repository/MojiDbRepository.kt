package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.table.ComponentKanjiTable
import ru.jr2.edit.data.db.table.MojiTable
import ru.jr2.edit.domain.entity.MojiEntity
import ru.jr2.edit.domain.model.Moji

class MojiDbRepository(
    private val db: Database = EditApp.instance.db
) {
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
}