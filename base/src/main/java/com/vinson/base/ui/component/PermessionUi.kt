package com.vinson.base.ui.component

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@Composable
fun RequiresWriteExternalStoragePermission() {
    RequiresPermission(Permission.WRITE_EXTERNAL)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequiresPermission(
    permission: Permission
) {
    val permissionState = rememberPermissionState(permission.code)
    val dialogState = rememberDialogState(permissionState.shouldShowRationale ||
            !permissionState.permissionRequested)

    when {
        // If the camera permission is granted, then show screen with the feature enabled
        permissionState.hasPermission -> {
            // Do Nothing
        }
        // If the user denied the permission but a rationale should be shown, or the user sees
        // the permission for the first time, explain why the feature is needed by the app and allow
        // the user to be presented with the permission again or to not see the rationale any more.
        permissionState.shouldShowRationale ||
                !permissionState.permissionRequested -> {
            Dialog(
                title = "Request permission",
                description = "Request ${permission.title} Permission",
                positiveText = "OK",
                positiveAction = {
                    permissionState.launchPermissionRequest()
                    dialogState.isOpen = false
                },
                state = dialogState
            )
        }
        // If the criteria above hasn't been met, the user denied the permission. Let's present
        // the user with a FAQ in case they want to know more and send them to the Settings screen
        // to enable it the future there if they want to.
        else -> {
            // Do Nothing
        }
    }
}

enum class Permission(val title: String, val code: String) {
    WRITE_EXTERNAL("write storage", android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
}