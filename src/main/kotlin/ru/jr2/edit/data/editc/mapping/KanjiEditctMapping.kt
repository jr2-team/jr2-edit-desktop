package ru.jr2.edit.data.editc.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

@JacksonXmlRootElement(localName = "kanjidic2")
class KanjiDictionary(
    @JacksonXmlProperty(localName = "character")
    @JacksonXmlElementWrapper(useWrapping = false) var entries: List<KanjiEdictEntry>
)

@JacksonXmlRootElement(localName = "character")
class KanjiEdictEntry(
    @JacksonXmlProperty val literal: String,
    @JacksonXmlProperty val codepoint: CodePoint,
    @JacksonXmlProperty val radical: Radical,
    @JacksonXmlProperty val misc: Misc,
    @JacksonXmlProperty(localName = "reading_meaning") val readingMeaning: ReadingMeaning?
)

@JacksonXmlRootElement(localName = "misc")
class Misc(
    @JacksonXmlProperty(localName = "stroke_count") val strokeCount: Int,
    @JacksonXmlProperty val grade: Int,
    @JacksonXmlProperty(localName = "freq") val frequency: Int,
    @JacksonXmlProperty val jlpt: Int
)

@JacksonXmlRootElement(localName = "codepoint")
class CodePoint(
    @JacksonXmlProperty(localName = "cp_value")
    @JacksonXmlElementWrapper(useWrapping = false) val values: List<CodePointValue>
)

@JacksonXmlRootElement(localName = "cp_value")
class CodePointValue(
    @JacksonXmlProperty(isAttribute = true, localName = "cp_type") val type: String
) {
    @JacksonXmlText
    val value: String = String()
}

@JacksonXmlRootElement(localName = "radical")
class Radical(
    @JacksonXmlProperty(localName = "rad_value")
    @JacksonXmlElementWrapper(useWrapping = false) val values: List<RadicalValue>
)

@JacksonXmlRootElement(localName = "rad_value")
class RadicalValue(
    @JacksonXmlProperty(isAttribute = true, localName = "rad_type") val type: String
) {
    @JacksonXmlText
    val value: Int = 0
}

@JacksonXmlRootElement(localName = "reading_meaning")
class ReadingMeaning(
    @JacksonXmlProperty val rmgroup: RmGroup?,
    @JacksonXmlProperty
    @JacksonXmlElementWrapper(useWrapping = false) val nanori: List<String>?
)

@JacksonXmlRootElement(localName = "rmgroup")
class RmGroup(
    @JacksonXmlProperty(localName = "reading")
    @JacksonXmlElementWrapper(useWrapping = false) val readings: List<Reading>?,
    @JacksonXmlProperty(localName = "meaning")
    @JacksonXmlElementWrapper(useWrapping = false) val meanings: List<Meaning>?
)

@JacksonXmlRootElement(localName = "reading")
class Reading(
    @JacksonXmlProperty(isAttribute = true, localName = "r_type") val type: String
) {
    @JacksonXmlText
    val value: String = String()
}

@JacksonXmlRootElement(localName = "meaning")
class Meaning(
    @JacksonXmlProperty(isAttribute = true, localName = "m_lang") val language: String = String()
) {
    @JacksonXmlText
    val value: String = String()
}

