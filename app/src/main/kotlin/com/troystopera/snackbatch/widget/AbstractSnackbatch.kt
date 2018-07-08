package com.troystopera.snackbatch.widget

import android.content.res.ColorStateList
import android.support.annotation.PluralsRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import com.troystopera.snackbatch.R

abstract class AbstractSnackbatch<T>(
        view: View,
        var duration: Int
) : Snackbar.Callback() {

    private lateinit var messageFactory: (Int) -> CharSequence

    protected val items = LinkedHashSet<T>()
    val itemCount: Int
        get() = items.size

    protected val snackbar: Snackbar by lazy { Snackbar.make(view, "", duration) }
    var action: CharSequence? = null
        set(value) {
            field = value
            snackbar.setAction(value, {
                onItemsAction(items)
                items.clear()
            })
        }

    init {
        setMessagePlurals(R.plurals.default_message)
    }

    fun add(item: T): Int {
        items.add(item)
        onItemAdded(item)

        val wasShown = isShown()
        snackbar.removeCallback(this)
        snackbar.dismiss()
        if (wasShown) show()

        return items.size
    }

    fun show(): Boolean {
        if (itemCount == 0) return false
        else if (isShown()) return true

        snackbar.addCallback(this)
        snackbar.setText(messageFactory(itemCount))
        snackbar.duration = duration
        snackbar.show()

        items.forEach(::onItemShown)

        return true
    }

    fun isShown() = snackbar.isShown

    fun dismiss() = snackbar.dismiss()

    fun dismissWithoutFinalize(keepItems: Boolean) {
        snackbar.removeCallback(this)
        snackbar.dismiss()
        if (!keepItems) items.clear()
    }

    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        onItemsFinalized(items)
        items.clear()
    }

    fun setActionTextColor(color: Int) = snackbar.setActionTextColor(color)

    fun setActionTextColor(colors: ColorStateList) = snackbar.setActionTextColor(colors)

    fun setAction(resId: Int) {
        action = snackbar.context.getString(resId)
    }

    fun setMessage(message: CharSequence) {
        messageFactory = { String.format(message.toString(), itemCount) }
    }

    fun setMessagePlurals(@PluralsRes resId: Int) {
        messageFactory = { snackbar.context.resources.getQuantityString(resId, it, it) }
    }

    fun setMessageString(@StringRes resId: Int) {
        messageFactory = { snackbar.context.getString(resId, it) }
    }

    fun setMessageFactory(factory: (Int) -> CharSequence) {
        messageFactory = factory
    }

    protected abstract fun onItemsAction(items: Set<T>)

    protected abstract fun onItemsFinalized(items: Set<T>)

    protected open fun onItemAdded(item: T) = Unit

    protected open fun onItemShown(item: T) = Unit

}