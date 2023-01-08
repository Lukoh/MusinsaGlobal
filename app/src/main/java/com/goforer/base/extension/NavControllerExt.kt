package com.goforer.base.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.safeNavigate(@IdRes resId: Int, direction: NavDirections) {
    val action = currentDestination?.getAction(direction.actionId) ?: graph.getAction(resId)

    if (currentDestination == null)
        navigateUp()

    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(direction)
    }
}

fun NavController.safeNavigate(
    @IdRes currentDestinationId: Int,
    @IdRes resId: Int,
    args: Bundle? = null
) {
    val action = currentDestination?.getAction(currentDestinationId) ?: graph.getAction(resId)

    if (currentDestination == null)
        navigateUp()

    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(resId, args)
    }
}