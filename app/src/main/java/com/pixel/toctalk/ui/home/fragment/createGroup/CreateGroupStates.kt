package com.pixel.toctalk.ui.home.fragment.createGroup

import com.pixel.toctalk.data.model.Group

sealed class CreateGroupStates {
    data class GroupCreated(val group: Group) : CreateGroupStates()
}
