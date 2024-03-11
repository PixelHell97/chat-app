package com.pixel.toctalk.auth.extensions

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.pixel.toctalk.auth.extensions.model.Message

fun Fragment.showDialog(
    title: String,
    message: String,
    posActionName: String? = null,
    posAction: (() -> Unit)? = null,
    negActionName: String? = null,
    negAction: (() -> Unit)? = null,
    isCancelable: Boolean = true,
): AlertDialog {
    val alertDialog = AlertDialog.Builder(requireContext())

    alertDialog
        .setTitle(title)
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
    message: Message,
): AlertDialog {
    return showDialog(
        title = message.title,
        message = message.message,
        posActionName = message.posActionName,
        posAction = message.posAction,
        negActionName = message.negActionName,
        negAction = message.negAction,
        isCancelable = message.isCancelable,
    )
}
