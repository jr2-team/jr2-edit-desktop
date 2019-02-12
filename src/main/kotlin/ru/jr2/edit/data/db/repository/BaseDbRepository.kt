package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.Database
import ru.jr2.edit.EditApp
import ru.jr2.edit.domain.model.BaseModel

abstract class BaseDbRepository<T : BaseModel>(
    internal open val db: Database = EditApp.instance.db
) {
    abstract fun getById(id: Int): T

    abstract fun getById(vararg id: Int): List<T>

    abstract fun getAll(): List<T>

    abstract fun insert(o: T): T

    abstract fun insertUpdate(o: T): T

    abstract fun delete(o: T)
}