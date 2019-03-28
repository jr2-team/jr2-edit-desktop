package ru.jr2.edit.domain.dto

data class WordDto(
    var id: Int,
    var word: String,
    var furigana: String,
    var interps: StringBuilder
)