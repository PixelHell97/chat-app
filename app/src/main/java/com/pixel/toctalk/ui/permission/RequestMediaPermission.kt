package com.pixel.toctalk.ui.permission

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.pixel.toctalk.Constants
import com.pixel.toctalk.ui.extensions.showDialog

object RequestMediaPermission {
    private fun permissionNeeded(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            arrayOf(
                READ_MEDIA_IMAGES,
                READ_MEDIA_VIDEO,
                READ_MEDIA_VISUAL_USER_SELECTED,
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO)
        } else {
            arrayOf(READ_EXTERNAL_STORAGE)
        }
    }

    private fun permission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            READ_MEDIA_VISUAL_USER_SELECTED
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            READ_MEDIA_IMAGES
        } else {
            READ_EXTERNAL_STORAGE
        }
    }

    fun requestGalleryPermission(
        activity: Activity,
        imagePicker: ActivityResultLauncher<PickVisualMediaRequest>,
    ) {
        when {
            ContextCompat.checkSelfPermission(
                activity.baseContext,
                permission(),
            ) == PackageManager.PERMISSION_GRANTED -> {
                imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                permission(),
            ) -> {
                Fragment().showDialog(
                    "This app requires Storage permission for particular feature to work as expected.",
                    "Ok",
                    posAction = {
                        ActivityCompat.requestPermissions(
                            activity,
                            permissionNeeded(),
                            Constants.PERMISSION_REQ_CODE,
                        )
                    },
                )
            }

            else -> {
                activity.requestPermissions(
                    permissionNeeded(),
                    Constants.PERMISSION_REQ_CODE,
                )
            }
        }
    }
}
