package com.pixel.toctalk.ui.extensions

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.pixel.toctalk.ui.extensions.model.MessageDialogModel

fun Fragment.showDialog(
    message: String,
    posActionName: String? = null,
    posAction: (() -> Unit)? = null,
    negActionName: String? = null,
    negAction: (() -> Unit)? = null,
    isCancelable: Boolean = true,
): AlertDialog {
    val alertDialog = AlertDialog.Builder(requireContext())

    alertDialog
        .setMessage(message)
        .setPositiveButton(posActionName) { dialog, _ ->
            dialog.dismiss()
            posAction?.invoke()
        }
        .setNegativeButton(negActionName) { dialog, _ ->
            dialog.dismiss()
            negAction?.invoke()
        }
        .setCancelable(isCancelable)

    return alertDialog.show()
}

fun Fragment.showDialog(
    messageDialogModel: MessageDialogModel,
): AlertDialog {
    return showDialog(
        message = messageDialogModel.message,
        posActionName = messageDialogModel.posActionName,
        posAction = messageDialogModel.posAction,
        negActionName = messageDialogModel.negActionName,
        negAction = messageDialogModel.negAction,
        isCancelable = messageDialogModel.isCancelable,
    )
}
