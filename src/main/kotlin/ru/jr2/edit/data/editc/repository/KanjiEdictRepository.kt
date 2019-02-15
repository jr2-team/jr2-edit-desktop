package ru.jr2.edit.data.editc.repository

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.editc.mapping.KanjiDictionary
import ru.jr2.edit.data.editc.mapping.KanjiEdictEntry
import java.io.File
import javax.xml.stream.XMLInputFactory

class KanjiEdictRepository(
    private val xmlMapper: XmlMapper = EditApp.instance.xmlMapper
) {
    suspend fun getKanjiEntriesFromFile(edictFile: File): List<KanjiEdictEntry> =
        withContext(Dispatchers.IO) {
            val xmlStreamReader = XMLInputFactory
                .newInstance()
                .createXMLStreamReader(edictFile.inputStream())
            xmlMapper.readValue(xmlStreamReader, KanjiDictionary::class.java).entries
        }
}