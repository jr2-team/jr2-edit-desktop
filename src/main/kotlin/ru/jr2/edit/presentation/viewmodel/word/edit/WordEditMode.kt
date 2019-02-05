package ru.jr2.edit.presentation.viewmodel.word.edit

enum class WordEditMode(val code: Int) {
    UPDATE(0),
    CREATE(1);

    companion object {
        fun fromCode(code: Int): WordEditMode = when (code) {
            UPDATE.code -> UPDATE
            CREATE.code -> CREATE
            else -> throw IllegalArgumentException()
        }
    }
}