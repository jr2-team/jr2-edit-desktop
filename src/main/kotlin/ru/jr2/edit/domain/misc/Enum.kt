package ru.jr2.edit.domain.misc

enum class JlptLevel(val code: Int, val str: String) {
    JLPT5(5, "JLPT 5"),
    JLPT4(4, "JLPT 4"),
    JLPT3(3, "JLPT 3"),
    JLPT2(2, "JLPT 2"),
    JLPT1(1, "JLPT 1"),
    NO_LEVEL(0, "Без JLPT уровня");

    companion object {
        fun fromCode(code: Int?): JlptLevel = when (code) {
            JLPT5.code -> JLPT5
            JLPT4.code -> JLPT4
            JLPT3.code -> JLPT3
            JLPT2.code -> JLPT2
            JLPT1.code -> JLPT1
            NO_LEVEL.code, null -> NO_LEVEL
            else -> throw IllegalArgumentException()
        }

        fun fromStr(str: String?) = when (str) {
            JLPT5.str -> JLPT5
            JLPT4.str -> JLPT4
            JLPT3.str -> JLPT3
            JLPT2.str -> JLPT2
            JLPT1.str -> JLPT1
            NO_LEVEL.str, null -> NO_LEVEL
            else -> throw IllegalArgumentException()
        }

        fun getNames() = JlptLevel.values().map { it.str }
    }
}

enum class GroupType(val code: Int, val str: String) {
    KANJI_GROUP(0, "Группа канджи"),
    WORD_GROUP(1, "Группа слов");

    companion object {
        fun fromCode(code: Int): GroupType = when (code) {
            KANJI_GROUP.code -> KANJI_GROUP
            WORD_GROUP.code -> WORD_GROUP
            else -> throw IllegalArgumentException()
        }

        fun fromStr(str: String): GroupType = when (str) {
            KANJI_GROUP.str -> KANJI_GROUP
            WORD_GROUP.str -> WORD_GROUP
            else -> throw IllegalArgumentException()
        }

        fun getNames() = GroupType.values().map { it.str }
    }
}

enum class KanjiReadingType(val code: Int, val str: String) {
    ON_READING(0, "Онное-чтение"),
    KUN_READING(1, "Кунное-чтение");

    companion object {
        fun fromCode(code: Int): KanjiReadingType = when (code) {
            ON_READING.code -> ON_READING
            KUN_READING.code -> KUN_READING
            else -> throw IllegalArgumentException()
        }

        fun fromStr(str: String): KanjiReadingType = when (str) {
            ON_READING.str -> ON_READING
            KUN_READING.str -> KUN_READING
            else -> throw IllegalArgumentException()
        }

        fun getNames() = KanjiReadingType.values().map { it.str }
    }
}