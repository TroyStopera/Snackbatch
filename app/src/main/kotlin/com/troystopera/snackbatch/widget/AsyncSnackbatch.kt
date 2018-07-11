package com.troystopera.snackbatch.widget

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.view.View
import com.troystopera.snackbatch.ItemsFinalizedCallback

abstract class AsyncSnackbatch<T, P, R>(
        view: View,
        duration: Int
) : AbstractSnackbatch<T>(view, duration) {

    private var task: Task? = null

    protected abstract fun onFinalizedAsync(items: Set<T>): R

    protected open fun onPreExecute(task: Task) = Unit

    protected fun publishProgress(progress: P) = task?.update(progress)

    protected open fun onProgress(progress: P) = Unit

    protected open fun onPostExecute(result: R, items: Set<T>) = Unit

    final override fun onItemsFinalized(items: Set<T>) {
        task = Task()
        task?.execute(items.toHashSet())
    }

    @SuppressLint("StaticFieldLeak")
    inner class Task : AsyncTask<Set<T>, P, R>() {

        private lateinit var items: Set<T>

        internal fun update(progress: P) = publishProgress(progress)

        override fun onPreExecute() = onPreExecute(this)

        override fun onProgressUpdate(vararg values: P) = onProgress(values[0])

        override fun doInBackground(vararg params: Set<T>): R {
            items = params[0]
            return onFinalizedAsync(items)
        }

        override fun onPostExecute(result: R) {
            onPostExecute(result, items)
            task = null
        }

    }

    companion object {
        @JvmStatic
        fun <T> make(view: View, duration: Int, onFinalizedAsync: (Set<T>) -> Unit) =
                object : AsyncSnackbatch<T, Unit, Unit>(view, duration) {
                    override fun onItemsAction(items: Set<T>) = Unit
                    override fun onFinalizedAsync(items: Set<T>) = onFinalizedAsync(items)
                }

        @JvmStatic
        fun <T> make(view: View, duration: Int, onFinalizedAsync: ItemsFinalizedCallback<T>) =
                make(view, duration, onFinalizedAsync::onItemsFinalized)
    }

}