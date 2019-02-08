package ru.jr2.edit.presentation.viewmodel

enum class EditMode(val code: Int) {
    UPDATE(0),
    CREATE(1);

    companion object {
        fun fromCode(code: Int): EditMode = when (code) {
            UPDATE.code -> UPDATE
            CREATE.code -> CREATE
            else -> throw IllegalArgumentException()
        }
    }
}