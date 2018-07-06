package com.troystopera.snackbatch

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager

class SnackbarBatch<T> @JvmOverloads constructor(
        val view: View,
        private val duration: Int,
        private val actionRes: Int,
        private val messageRes: Int = R.plurals.default_message,
        callback: SnackbarBatchCallback<T>? = null
) : Snackbar.Callback() {

    var addedCallback: ItemAddedCallback<T>? = callback
    var shownCallback: ItemShownCallback<T>? = callback
    var actionCallback: ItemActionCallback<T>? = callback
    var finalizedCallback: ItemFinalizedCallback<T>? = callback
    var snackbar: Snackbar? = null
        private set

    val itemCount: Int
        get() = items.size

    private val items = LinkedHashSet<ItemWrapper<T>>()

    fun add(
            item: T,
            itemAddedCallback: ItemAddedCallback<T>? = null,
            itemShownCallback: ItemShownCallback<T>? = null,
            actionCallback: ItemActionCallback<T>? = null,
            finalizedCallback: ItemFinalizedCallback<T>? = null
    ): Int {
        snackbar?.removeCallback(this)
        snackbar?.dismiss()

        snackbar = Snackbar.make(view, view.context.resources.getQuantityString(messageRes, itemCount, itemCount), duration)
        snackbar?.addCallback(this)
        snackbar?.setAction(actionRes, {
            snackbar?.removeCallback(this)
            items.groupBy { it.actionCallback }.forEach { it.key?.onItemAction(it.value.map { it.item }) }
            items.clear()
        })

        items.add(
                ItemWrapper(
                        item,
                        itemShownCallback ?: this.shownCallback,
                        actionCallback ?: this.actionCallback,
                        finalizedCallback ?: this.finalizedCallback
                )
        )
        itemAddedCallback?.onItemAdded(item) ?: this.addedCallback?.onItemAdded(item)

        return itemCount
    }

    fun show(): Boolean {
        val show = snackbar?.show()
        items.forEach { it.itemShownCallback?.onItemShown(it.item) }
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
        return show != null
    }

    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        items.groupBy { it.finalizedCallback }.forEach { it.key?.onItemFinalized(it.value.map { it.item }) }
        items.clear()
    }

}

private class ItemWrapper<T>(
        val item: T,
        val itemShownCallback: ItemShownCallback<T>?,
        val actionCallback: ItemActionCallback<T>?,
        val finalizedCallback: ItemFinalizedCallback<T>?
)