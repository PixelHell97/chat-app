package com.pixel.toctalk.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pixel.toctalk.ui.extensions.model.MessageDialogModel

open class BaseViewModel : ViewModel() {
    protected val _messageDialogModelDialog = MutableLiveData<MessageDialogModel>()
    val messageDialogModelDialog: LiveData<MessageDialogModel> = _messageDialogModelDialog
}
