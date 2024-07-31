package com.pixel.toctalk.ui.home.fragment.home.joinGroup

import com.pixel.toctalk.data.model.Group

sealed class JoinGroupState {
    data class NavigateToGroupChat(val group: Group) : JoinGroupState()
}
