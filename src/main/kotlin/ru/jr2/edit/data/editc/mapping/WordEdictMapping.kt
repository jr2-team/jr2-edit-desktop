package ru.jr2.edit.data.editc.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

@JacksonXmlRootElement(localName = "JMdict")
class WordDictionary(
    @JacksonXmlProperty(localName = "entry")
    @JacksonXmlElementWrapper(useWrapping = false) var entries: List<WordEdictEntry>
)

@JacksonXmlRootElement(localName = "entry")
class WordEdictEntry(
    @JacksonXmlProperty(localName = "ent_seq") val id: Int = 0,
    @JacksonXmlProperty(localName = "k_ele")
    @JacksonXmlElementWrapper(useWrapping = false) val kanjiElements: List<KanjiElement>?,
    @JacksonXmlProperty(localName = "r_ele")
    @JacksonXmlElementWrapper(useWrapping = false) val readingElements: List<KanaElement>?,
    @JacksonXmlProperty(localName = "sense")
    @JacksonXmlElementWrapper(useWrapping = false) val senses: List<Sense>?
)

@JacksonXmlRootElement(localName = "k_ele")
class KanjiElement(
    @JacksonXmlProperty(localName = "keb") val reading: String?,
    @JacksonXmlProperty(localName = "ke_inf") val info: String?,
    @JacksonXmlProperty(localName = "ke_pri") val priority: String?
)

@JacksonXmlRootElement(localName = "r_ele")
class KanaElement(
    @JacksonXmlProperty(localName = "reb") var reading: String?,
    @JacksonXmlProperty(localName = "re_inf") val info: String?,
    @JacksonXmlProperty(localName = "re_pri") val priority: String?
)

@JacksonXmlRootElement(localName = "sense")
class Sense(
    @JacksonXmlProperty(localName = "pos")
    @JacksonXmlElementWrapper(useWrapping = false) val partsOfSpeach: List<String>?,
    @JacksonXmlProperty(localName = "gloss")
    @JacksonXmlElementWrapper(useWrapping = false) val glossaryEntries: List<GlossaryEntry>?,
    @JacksonXmlProperty(localName = "xref")
    @JacksonXmlElementWrapper(useWrapping = false) val references: List<String>?
)

@JacksonXmlRootElement(localName = "gloss")
class GlossaryEntry(
    @JacksonXmlProperty(isAttribute = true, localName = "g_type") val type: String?,
    @JacksonXmlProperty(isAttribute = true, localName = "lang", namespace = "xml") val language: String?
) {
    @JacksonXmlText val definition = String()
}