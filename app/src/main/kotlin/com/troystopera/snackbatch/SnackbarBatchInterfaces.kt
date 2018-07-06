package com.troystopera.snackbatch

interface SnackbarBatchCallback<T>
    : ItemAddedCallback<T>, ItemShownCallback<T>, ItemActionCallback<T>, ItemFinalizedCallback<T> {
    override fun onItemAdded(item: T) = Unit
    override fun onItemShown(item: T) = Unit
    override fun onItemAction(item: T) = Unit
    override fun onItemFinalized(item: T) = Unit
}

interface ItemAddedCallback<T> {
    fun onItemAdded(item: T)
}

interface ItemShownCallback<T> {
    fun onItemShown(item: T)
}

interface ItemActionCallback<T> {
    fun onItemAction(item: T)
    fun onItemAction(items: Collection<T>) = items.forEach(::onItemAction)
}

interface ItemFinalizedCallback<T> {
    fun onItemFinalized(item: T)
    fun onItemFinalized(items: Collection<T>) = items.forEach(::onItemFinalized)
}