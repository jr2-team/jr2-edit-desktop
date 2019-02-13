package ru.jr2.edit.presentation.view.tool

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.db.repository.WordDbRepository
import ru.jr2.edit.domain.misc.Dictionary
import ru.jr2.edit.domain.misc.GlossaryEntry
import ru.jr2.edit.domain.model.Word
import tornadofx.*
import java.io.File
import java.nio.file.Paths
import javax.xml.stream.XMLInputFactory


class ToolView : View() {
    private val viewModel: ToolViewModel by inject()

    override val root = borderpane {
        center = button("Parse") {
            action { viewModel.onParseClick() }
        }
    }
}

class ToolViewModel(
    private val xmlMapper: XmlMapper = EditApp.instance.xmlMapper
) : ViewModel() {
    fun onParseClick() {
        val pathToJMdict = "${Paths.get("").toAbsolutePath()}\\JMdict"

        val xmlStreamReader = XMLInputFactory
            .newInstance()
            .createXMLStreamReader(File(pathToJMdict).inputStream())

        val dictionary = xmlMapper.readValue(xmlStreamReader, Dictionary::class.java)

        /*val words = dictionary.entries?.map { e ->
            Word().apply {
                e.kanjiElement?.first()?.reading?.let { value = it }
                e.readingElement?.first()?.reading?.let { furigana = it }
                val gloses = e.senses?.filter { it.glossaryEntries is List<GlossaryEntry> }?.flatMap {
                    it.glossaryEntries!!
                }
                interpretation = gloses?.filter { it.language == "rus" }.run {
                    this!!.joinToString { it.definition }
                }

            }
        }*/
        dictionary.entries?.let { WordDbRepository().insertAllEntries(it) }
    }
}