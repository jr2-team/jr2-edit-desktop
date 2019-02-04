package jr2.edit.presentation.feature.word

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import jr2.edit.EditApp
import jr2.edit.domain.entity.Word
import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.expandArgs
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import tornadofx.FXEvent
import tornadofx.ViewModel

class WordViewModel : ViewModel() {
    val sPropValue = SimpleStringProperty()
    val sPropFurigana = SimpleStringProperty()
    val sPropBasicInterpretation = SimpleStringProperty()
    val iPropJlptLevel = SimpleIntegerProperty(5)

    fun onWordSave() {
        transaction(EditApp.instance.db) {
            addLogger(KotlinLoggingSqlLogger)
            val newWord = Word.new {
                value = sPropValue.value
                furigana = sPropFurigana.value
                basicInterpretation = sPropBasicInterpretation.value
                jlptLevel = iPropJlptLevel.value
            }

            fire(WordSavedEvent(newWord.toString()))
        }
    }

    object KotlinLoggingSqlLogger : SqlLogger {
        private val logger = LoggerFactory.getLogger(WordViewModel::class.java)
        override fun log(
            context: StatementContext,
            transaction: Transaction
        ) {
            logger.info("SQL: ${context.expandArgs(transaction)}")
        }
    }
}

class WordSavedEvent(val message: String) : FXEvent()