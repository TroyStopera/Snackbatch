package com.troystopera.undodelete

import android.view.View

class SnackbarUndoDelete<T> @JvmOverloads constructor(
        val view: View,
        callback: UndoDeleteCallback<T>? = null
) {

    var itemAddedCallback: ItemAddedCallback<T>? = callback
    var itemShownCallback: ItemShownCallback<T>? = callback
    var undoCallback: UndoActionCallback<T>? = callback
    var finalizedCallback: DeleteFinalizedCallback<T>? = callback

}