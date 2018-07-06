package com.troystopera.snackbatch

fun <T> SnackbarBatch<T>.setAddededCallback(callback: (T) -> Unit) {
    addedCallback = object : ItemAddedCallback<T> {
        override fun onItemAdded(item: T) = callback(item)
    }
}

fun <T> SnackbarBatch<T>.setShownCallback(callback: (T) -> Unit) {
    shownCallback = object : ItemShownCallback<T> {
        override fun onItemShown(item: T) = callback(item)
    }
}

fun <T> SnackbarBatch<T>.setActionCallback(callback: (T) -> Unit) {
    actionCallback = object : ItemActionCallback<T> {
        override fun onItemAction(item: T) = callback(item)
    }
}

fun <T> SnackbarBatch<T>.setFinalizedCallback(callback: (T) -> Unit) {
    finalizedCallback = object : ItemFinalizedCallback<T> {
        override fun onItemFinalized(item: T) = callback(item)
    }
}