package ru.jr2.edit.data.editc.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper  as Wrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty as Prop
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement as Root
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText as Text

@Root(localName = "kanjidic2")
class KanjiDictionary(
    @Prop(localName = "character")
    @Wrapper(useWrapping = false)
    override var entries: List<KanjiEdictEntry>
) : Edict<KanjiEdictEntry>

@Root(localName = "character")
class KanjiEdictEntry(
    @Prop val literal: String,
    @Prop val codepoint: CodePoint,
    @Prop val radical: Radical,
    @Prop val misc: Misc,
    @Prop(localName = "reading_meaning") val readingMeaning: ReadingMeaning?
)

@Root(localName = "misc")
class Misc(
    @Prop(localName = "stroke_count") val strokeCount: Int,
    @Prop val grade: Int,
    @Prop(localName = "freq") val frequency: Int,
    @Prop val jlpt: Int
)

@Root(localName = "codepoint")
class CodePoint(
    @Prop(localName = "cp_value")
    @Wrapper(useWrapping = false) val values: List<CodePointValue>
)

@Root(localName = "cp_value")
class CodePointValue(
    @Prop(isAttribute = true, localName = "cp_type") val type: String
) {
    @Text
    val value: String = String()
}

@Root(localName = "radical")
class Radical(
    @Prop(localName = "rad_value")
    @Wrapper(useWrapping = false) val values: List<RadicalValue>
)

@Root(localName = "rad_value")
class RadicalValue(
    @Prop(isAttribute = true, localName = "rad_type") val type: String
) {
    @Text
    val value: Int = 0
}

@Root(localName = "reading_meaning")
class ReadingMeaning(
    @Prop val rmgroup: RmGroup?,
    @Prop
    @Wrapper(useWrapping = false) val nanori: List<String>?
)

@Root(localName = "rmgroup")
class RmGroup(
    @Prop(localName = "reading")
    @Wrapper(useWrapping = false) val readings: List<Reading>?,
    @Prop(localName = "meaning")
    @Wrapper(useWrapping = false) val meanings: List<Meaning>?
)

@Root(localName = "reading")
class Reading(
    @Prop(isAttribute = true, localName = "r_type") val type: String
) {
    @Text
    val value: String = String()
}

@Root(localName = "meaning")
class Meaning(
    @Prop(isAttribute = true, localName = "m_lang") val language: String = String()
) {
    @Text
    val value: String = String()
}

