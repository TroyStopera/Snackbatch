package com.troystopera.snackbatch

interface SnackbatchCallback<T>
    : ItemAddedCallback<T>, ItemShownCallback<T>, ItemsActionCallback<T>, ItemsFinalizedCallback<T> {
    override fun onItemAdded(item: T) = Unit
    override fun onItemShown(item: T) = Unit
    override fun onItemsAction(items: Collection<T>) = Unit
    override fun onItemsFinalized(items: Collection<T>) = Unit
}

interface ItemAddedCallback<T> {
    fun onItemAdded(item: T)
}

interface ItemShownCallback<T> {
    fun onItemShown(item: T)
}

interface ItemsActionCallback<T> {
    fun onItemsAction(items: Collection<T>)
}

interface ItemsFinalizedCallback<T> {
    fun onItemsFinalized(items: Collection<T>)
}