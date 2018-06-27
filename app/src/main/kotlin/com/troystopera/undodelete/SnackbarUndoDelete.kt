package com.troystopera.undodelete

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager

class SnackbarUndoDelete<T> @JvmOverloads constructor(
        val view: View,
        private val duration: Int,
        private val messageRes: Int = R.plurals.default_message,
        callback: UndoDeleteCallback<T>? = null
) : Snackbar.Callback() {

    var itemAddedCallback: ItemAddedCallback<T>? = callback
    var itemShownCallback: ItemShownCallback<T>? = callback
    var undoCallback: UndoActionCallback<T>? = callback
    var finalizedCallback: DeleteFinalizedCallback<T>? = callback
    var snackbar: Snackbar? = null
        private set

    val itemCount: Int
        get() = items.size

    private val items = LinkedHashSet<ItemWrapper<T>>()

    fun add(
            item: T,
            itemAddedCallback: ItemAddedCallback<T>? = null,
            itemShownCallback: ItemShownCallback<T>? = null,
            undoCallback: UndoActionCallback<T>? = null,
            finalizedCallback: DeleteFinalizedCallback<T>? = null
    ): Int {
        snackbar?.removeCallback(this)
        snackbar?.dismiss()

        snackbar = Snackbar.make(view, view.context.resources.getQuantityString(messageRes, itemCount, itemCount), duration)
        snackbar?.addCallback(this)
        snackbar?.setAction(R.string.undo, {
            snackbar?.removeCallback(this)
            items.groupBy { it.undoCallback }.forEach { it.key?.onUndoAction(it.value.map { it.item }) }
            items.clear()
        })

        items.add(
                ItemWrapper(
                        item,
                        itemShownCallback ?: this.itemShownCallback,
                        undoCallback ?: this.undoCallback,
                        finalizedCallback ?: this.finalizedCallback
                )
        )
        itemAddedCallback?.onItemAdded(item) ?: this.itemAddedCallback?.onItemAdded(item)

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
        items.groupBy { it.finalizedCallback }.forEach { it.key?.onDeleteFinalized(it.value.map { it.item }) }
        items.clear()
    }

}

private class ItemWrapper<T>(
        val item: T,
        val itemShownCallback: ItemShownCallback<T>?,
        val undoCallback: UndoActionCallback<T>?,
        val finalizedCallback: DeleteFinalizedCallback<T>?
)