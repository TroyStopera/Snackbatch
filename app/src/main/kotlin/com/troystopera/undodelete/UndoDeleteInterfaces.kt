package com.troystopera.undodelete

interface UndoDeleteCallback<T>
    : ItemAddedCallback<T>, ItemShownCallback<T>, UndoActionCallback<T>, DeleteFinalizedCallback<T> {
    override fun onItemAdded(item: T) = Unit
    override fun onItemShown(item: T) = Unit
    override fun onUndoAction(item: T) = Unit
    override fun onDeleteFinalized(item: T) = Unit
}

interface ItemAddedCallback<T> {
    fun onItemAdded(item: T)
}

interface ItemShownCallback<T> {
    fun onItemShown(item: T)
}

interface UndoActionCallback<T> {
    fun onUndoAction(item: T)
    fun onUndoAction(items: Collection<T>) = items.forEach { onUndoAction(it) }
}

interface DeleteFinalizedCallback<T> {
    fun onDeleteFinalized(item: T)
    fun onDeleteFinalized(items: Collection<T>) = items.forEach { onDeleteFinalized(it) }
}