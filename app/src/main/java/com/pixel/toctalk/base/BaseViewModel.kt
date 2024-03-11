package com.pixel.toctalk.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pixel.toctalk.auth.extensions.model.Message

open class BaseViewModel : ViewModel() {
    val messageDialog = MutableLiveData<Message>()
}
