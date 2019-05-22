package ru.jr2.edit.data.editc.repository

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.jr2.edit.data.editc.mapping.Edict
import ru.jr2.edit.util.Injectable
import java.io.File

class EdictParserRepository(
    val xmlMapper: XmlMapper = Injectable.xmlMapper
) {
    suspend inline fun <reified TEdict : Edict<TEntry>, TEntry> getEdictEntries(
        edictFile: File
    ): List<TEntry> = withContext(Dispatchers.IO) {
        xmlMapper.readValue(edictFile, TEdict::class.java).entries
    }
}