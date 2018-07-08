package com.troystopera.snackbatch.widget

import android.view.View
import com.troystopera.snackbatch.*

open class Snackbatch<T>(
        view: View,
        duration: Int
) : AbstractSnackbatch<T>(view, duration) {

    protected var itemAdded: ((T) -> Unit)? = null
    protected var itemShown: ((T) -> Unit)? = null
    protected var itemsAction: ((Set<T>) -> Unit)? = null
    protected var itemsFinalized: ((Set<T>) -> Unit)? = null

    override fun onItemAdded(item: T) {
        itemAdded?.invoke(item); show()
    }

    fun setItemAddedCallback(callback: ((T) -> Unit)?) {
        itemAdded = callback
    }

    fun setItemAddedCallback(callback: ItemAddedCallback<T>) {
        itemAdded = callback::onItemAdded
    }

    override fun onItemShown(item: T) = itemShown?.invoke(item) ?: Unit

    fun setItemShownCallback(callback: ((T) -> Unit)?) {
        itemShown = callback
    }

    fun setItemShownCallback(callback: ItemShownCallback<T>) {
        itemShown = callback::onItemShown
    }

    override fun onItemsAction(items: Set<T>) = itemsAction?.invoke(items) ?: Unit

    fun setItemsActionCallback(callback: ((Set<T>) -> Unit)?) {
        itemsAction = callback
    }

    fun setItemsActionCallback(callback: ItemsActionCallback<T>) {
        itemsAction = callback::onItemsAction
    }

    override fun onItemsFinalized(items: Set<T>) = itemsFinalized?.invoke(items) ?: Unit

    fun setItemsFinalizedCallback(callback: ((Set<T>) -> Unit)?) = { itemsFinalized = callback }

    fun setItemsFinalizedCallback(callback: ItemsFinalizedCallback<T>) {
        itemsFinalized = callback::onItemsFinalized
    }

    fun setSnackbatchCallback(callback: SnackbatchCallback<T>) {
        setItemAddedCallback(callback)
        setItemShownCallback(callback)
        setItemsActionCallback(callback)
        setItemsFinalizedCallback(callback)
    }

}