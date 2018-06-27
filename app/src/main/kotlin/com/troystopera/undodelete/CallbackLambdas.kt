package com.troystopera.undodelete

fun <T> SnackbarUndoDelete<T>.setItemAddededCallback(callback: (T) -> Unit) {
    itemAddedCallback = object : ItemAddedCallback<T> {
        override fun onItemAdded(item: T) = callback(item)
    }
}

fun <T> SnackbarUndoDelete<T>.setItemShownCallback(callback: (T) -> Unit) {
    itemShownCallback = object : ItemShownCallback<T> {
        override fun onItemShown(item: T) = callback(item)
    }
}

fun <T> SnackbarUndoDelete<T>.setUndoCallback(callback: (T) -> Unit) {
    undoCallback = object : UndoActionCallback<T> {
        override fun onUndoAction(item: T) = callback(item)
    }
}

fun <T> SnackbarUndoDelete<T>.setFinalizedCallback(callback: (T) -> Unit) {
    finalizedCallback = object : DeleteFinalizedCallback<T> {
        override fun onDeleteFinalized(item: T) = callback(item)
    }
}