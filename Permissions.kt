package com.entech.simple_player.common.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermission(
    permission: String,
    lifecycleOwner: LifecycleOwner,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionFullyDenied: () -> Unit
) {
    val permissionState = rememberPermissionState(permission = permission)


    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    permissionState.launchPermissionRequest()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    when {
        permissionState.status.isGranted -> {
            onPermissionGranted()
        }
        permissionState.status.shouldShowRationale -> {
            onPermissionDenied()
        }
        !permissionState.status.isGranted && !permissionState.status.shouldShowRationale -> {
            onPermissionFullyDenied()
        }
    }
}