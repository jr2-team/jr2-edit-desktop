package ru.jr2.edit

import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

class Style : Stylesheet() {
    companion object {
        val largeButton by cssclass()
        val mediumButton by cssclass()
        val miniButton by cssclass()

        val bottomButtonPane by cssclass()
        val utilityFragment by cssclass()
        val fragmentMiniButton by cssclass()
    }

    init {
        textArea {
            prefRowCount = 3
            maxWidth = 300.px
            wrapText = true
        }

        bottomButtonPane {
            padding = box(10.px)
            button {
                minWidth = 120.px
                minHeight = 28.px
            }
            buttonBar {
                button {
                    minWidth = 120.px
                    minHeight = 28.px
                }
            }
        }

        utilityFragment {
            minWidth = 400.px
            minHeight = 500.px
            maxWidth = 400.px
            maxHeight = 500.px
        }

        largeButton {
            minWidth = 120.px
            minHeight = 28.px
        }
        mediumButton {
            minWidth = 80.px
            minHeight = 28.px
        }
        fragmentMiniButton {
            minWidth = 20.px
            minHeight = 28.px
            maxWidth = 20.px
            maxHeight = 28.px
        }
    }
}