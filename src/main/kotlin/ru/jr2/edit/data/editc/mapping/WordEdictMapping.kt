package ru.jr2.edit.data.editc.mapping

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper  as Wrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty as Prop
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement as Root
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText as Text

@Root(localName = "JMdict")
class WordDictionary(
    @Prop(localName = "entry")
    @Wrapper(useWrapping = false)
    override var entries: List<WordEdictEntry>
) : Edict<WordEdictEntry>

@Root(localName = "entry")
class WordEdictEntry(
    @Prop(localName = "ent_seq") val id: Int = 0,
    @Prop(localName = "k_ele")
    @Wrapper(useWrapping = false) val kanjiElements: List<KanjiElement>?,
    @Prop(localName = "r_ele")
    @Wrapper(useWrapping = false) val readingElements: List<KanaElement>?,
    @Prop(localName = "sense")
    @Wrapper(useWrapping = false) val senses: List<Sense>?
)

@Root(localName = "k_ele")
class KanjiElement(
    @Prop(localName = "keb") val reading: String?,
    @Prop(localName = "ke_inf") val info: String?,
    @Prop(localName = "ke_pri") val priority: String?
)

@Root(localName = "r_ele")
class KanaElement(
    @Prop(localName = "reb") var reading: String?,
    @Prop(localName = "re_inf") val info: String?,
    @Prop(localName = "re_pri") val priority: String?
)

@Root(localName = "sense")
class Sense(
    @Prop(localName = "pos")
    @Wrapper(useWrapping = false) val partsOfSpeach: List<String>?,
    @Prop(localName = "gloss")
    @Wrapper(useWrapping = false) val glossaryEntries: List<GlossaryEntry>?,
    @Prop(localName = "xref")
    @Wrapper(useWrapping = false) val references: List<String>?
)

@Root(localName = "gloss")
class GlossaryEntry(
    @Prop(isAttribute = true, localName = "g_type") val type: String?,
    @Prop(isAttribute = true, localName = "lang", namespace = "xml") val language: String?
) {
    @Text val definition = String()
}