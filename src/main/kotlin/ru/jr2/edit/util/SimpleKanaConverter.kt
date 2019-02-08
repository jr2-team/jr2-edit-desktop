package ru.jr2.edit.util

data class KanaChar(
    val katakana: String,
    val hirogana: String,
    val romajiEng: String,
    val romajiRus: String,
    val hasDakuten: Boolean = false,
    val hasHandakuten: Boolean = false
)

val sokuonChars = listOf("っ", "ッ")
val suteganaChars = listOf("ぁ", "ぃ", "ぅ", "ぇ", "ぉ")
var bulletChar = '•'
// TODO: в json?
fun getKanaChars(): List<KanaChar> = listOf(
    "あ", "ア", "a", "а",
    "い", "イ", "e", "и",
    "う", "ウ", "", "",
    "え", "エ", "", "",
    "お", "オ", "", "",

    "か", "カ", "", "",
    "き", "キ", "", "",
    "く", "ク", "", "",
    "け", "ケ", "", "",
    "こ", "コ", "", ""
).windowed(4, 4).map {
    KanaChar(it[0], it[1], it[2], it[3])
}

fun getHiroganaMap(): HashMap<String, KanaChar> =
    HashMap(getKanaChars().map {
        it.katakana to it
    }.toMap())

fun kanaToRomaji(kana: String, convertToRus: Boolean = true): String {
    return ""
}

fun romajiToKana(romaji: String, convertToHirogana: Boolean = true): String {
    return ""
}

fun furiganaToRomaji(furigana: String, convertToRus: Boolean = true): String {
    return ""
}

fun furiganaToKana(furigana: String): String {
    return ""
}