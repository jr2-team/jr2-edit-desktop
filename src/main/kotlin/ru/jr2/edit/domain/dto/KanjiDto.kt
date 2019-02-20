package ru.jr2.edit.domain.dto

import java.lang.StringBuilder

data class KanjiDto(
    var id: Int,
    var kanji: String,
    var interpretation: String = String(),
    var onReadings: StringBuilder = StringBuilder(),
    var kunReadings: StringBuilder = StringBuilder(),
    var jlptLevel: String = String()
) {
    override fun toString() = kanji
}