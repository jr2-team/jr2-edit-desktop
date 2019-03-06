package ru.jr2.edit.presentation.viewmodel.group

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.jr2.edit.data.db.repository.GroupDbRepository
import ru.jr2.edit.domain.misc.GroupType
import ru.jr2.edit.domain.model.GroupModel
import tornadofx.ViewModel
import tornadofx.onChange

class GroupListViewModel(
    private val groupRepository: GroupDbRepository = GroupDbRepository()
) : ViewModel() {
    private val pSelectedType = SimpleObjectProperty<GroupType>(GroupType.KANJI_GROUP)

    val groups: ObservableList<GroupModel> = FXCollections.observableArrayList()

    init {
        pSelectedType.onChange { loadContent() }
    }

    fun loadContent() {
        groups.clear()
        groups.addAll(groupRepository.getByGroupType(pSelectedType.value))
    }

    fun onSwitchToKanjiGroupClick() {
        pSelectedType.value = GroupType.KANJI_GROUP
    }

    fun onSwitchToWordGroupClick() {
        pSelectedType.value = GroupType.WORD_GROUP
    }
}