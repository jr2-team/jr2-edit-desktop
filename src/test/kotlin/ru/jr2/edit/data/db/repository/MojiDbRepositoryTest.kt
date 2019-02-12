package ru.jr2.edit.data.db.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import ru.jr2.edit.data.db.table.ComponentKanjiTable
import ru.jr2.edit.data.db.table.MojiTable
import ru.jr2.edit.domain.entity.MojiEntity
import ru.jr2.edit.domain.model.Moji
import java.sql.Connection
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class MojiDbRepositoryTest {
    private val testDb: Database = Database.connect(
        "jdbc:sqlite:file:test?mode=memory&cache=shared",
        "org.sqlite.JDBC"
    )
    private val repository = MojiDbRepository(testDb)

    init {
        TransactionManager.manager.defaultIsolationLevel =
                Connection.TRANSACTION_READ_UNCOMMITTED
    }

    @Test
    fun getById() = transaction(testDb) {
        SchemaUtils.create(MojiTable, ComponentKanjiTable)

        val testMoji = repository.insert(Moji(value = "Moji"))
        assertEquals(
            testMoji.value,
            repository.getById(testMoji.id).value
        )
    }

    @Test
    fun insertUpdate() = transaction(testDb) {
        SchemaUtils.create(MojiTable)

        val testMoji0 = repository.insertUpdate(Moji(value = "Moji0"))
        assertEquals(
            testMoji0.value,
            repository.getById(testMoji0.id).value
        )

        val testMoji1 = repository.insertUpdate(Moji(id = testMoji0.id, value = "Moji1"))
        assertEquals(
            testMoji1.value,
            repository.getById(testMoji1.id).value
        )
    }

    @Test
    fun createMojiComponent() = transaction(testDb) {
        SchemaUtils.create(MojiTable, ComponentKanjiTable)

        val testMoji0 = repository.insert(Moji(value = "Moji0"))
        val testComponents0 = (0..9).map {
            repository.insert(Moji(value = "Moji Comp 0 $it"))
        }
        repository.insertUpdateMojiComponent(testMoji0, testComponents0)

        val testMoji1 = repository.insert(Moji(value = "Moji1"))
        val testComponents1 = (0..9).map {
            repository.insert(Moji(value = "Moji Comp 1 $it"))
        }
        repository.insertUpdateMojiComponent(testMoji1, testComponents1)

        val testMoji2 = repository.insert(Moji(value = "Moji2"))
        val testComponents2 = repository.getById(
            *(1..MojiEntity.count() - 2).map { it }.toIntArray()
        )
        repository.insertUpdateMojiComponent(testMoji2, testComponents2)
        assertEquals(
            testComponents0.map { it.value },
            repository.getComponentsOfMoji(testMoji0.id).map { it.value }
        )
        assertEquals(
            testComponents1.map { it.value },
            repository.getComponentsOfMoji(testMoji1.id).map {
                it.value
            }
        )
        assertEquals(
            testComponents2.map { it.value },
            repository.getComponentsOfMoji(testMoji2.id).map { it.value }
        )
        assertNotEquals(
            repository.getComponentsOfMoji(testMoji0.id).map { it.value },
            repository.getComponentsOfMoji(testMoji1.id).map { it.value }
        )
    }
}