package com.pixel.toctalk.ui.home.fragment.home.searchContact

import com.pixel.toctalk.data.model.Contact

sealed class SearchContactState {
    data class NavToChat(val contact: Contact) : SearchContactState()
}
