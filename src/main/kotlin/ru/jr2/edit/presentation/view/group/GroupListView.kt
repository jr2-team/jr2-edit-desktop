package ru.jr2.edit.presentation.view.group

import javafx.scene.control.Button
import ru.jr2.edit.Style
import ru.jr2.edit.Style.Companion.mediumButton
import ru.jr2.edit.presentation.model.GroupModel
import ru.jr2.edit.presentation.viewmodel.group.GroupListViewModel
import tornadofx.*

class GroupListView : View() {
    private val viewModel: GroupListViewModel by inject()

    override fun onTabSelected() {
        super.onTabSelected()
        viewModel.loadContent()
    }

    private var btnSwitchToWord: Button = button("Слова") {
        action {
            viewModel.onSwitchToWordGroupClick()
            this.replaceWith(btnSwitchToKanji)
        }
        addClass(mediumButton)
    }

    private var btnSwitchToKanji: Button = button("Канджи") {
        action {
            viewModel.onSwitchToKanjiGroupClick()
            this.replaceWith(btnSwitchToWord)
        }
        addClass(mediumButton)
    }

    override val root = borderpane {
        top = hbox {
            paddingAll = 5.0
            add(btnSwitchToWord)
        }
        center = tableview(viewModel.groups) {
            column("Имя группы", GroupModel::pName)
        }
        bottom = renderContentControlBorderPane()
    }

    private fun renderContentControlBorderPane() = borderpane {
        left = buttonbar {
            button("Добавить") {
                action { }
            }
            button("Редактировать") {
                action { }
                isDisable = true
            }
            button("Удалить") {
                action { }
                isDisable = true
            }
        }
        addClass(Style.bottomBorderPaneStyle)
    }
}