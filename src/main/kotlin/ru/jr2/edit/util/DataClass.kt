package ru.jr2.edit.util

data class TabType(
    val code: Int,
    val name: String
) {
    companion object {
        val KANJI_TAB = TabType(0, "Кандзи")
        val WORD_TAB = TabType(1, "Слова")
        val SENTENCE_TAB = TabType(2, "Предложения")
        val GROUP_TAB = TabType(3, "Группы")
    }
}