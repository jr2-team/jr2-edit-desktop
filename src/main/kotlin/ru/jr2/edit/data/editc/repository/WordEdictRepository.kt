package ru.jr2.edit.data.editc.repository

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.jr2.edit.EditApp
import ru.jr2.edit.data.editc.mapping.Dictionary
import ru.jr2.edit.data.editc.mapping.WordEdictEntry
import java.io.File
import javax.xml.stream.XMLInputFactory

class WordEdictRepository(
    private val xmlMapper: XmlMapper = EditApp.instance.xmlMapper
) {
    suspend fun getWordEntriesFromFile(edictFile: File): List<WordEdictEntry> =
        withContext(Dispatchers.IO) {
            val xmlStreamReader = XMLInputFactory
                .newInstance()
                .createXMLStreamReader(edictFile.inputStream())
            xmlMapper.readValue(xmlStreamReader, Dictionary::class.java).entries
        }
}