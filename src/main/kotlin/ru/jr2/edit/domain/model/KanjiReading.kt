package ru.jr2.edit.domain.model

class KanjiReading(
    val reading: String,
    val readingType: Int,
    val priority: Int,
    val isAnachronism: Boolean
)