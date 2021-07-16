package ru.test.testapplication.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import ru.test.testapplication.R
import ru.test.testapplication.dto.Repository

object Utils {
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun createDialog(activity: Activity, message: Int) {
        val dialog = activity.let { activity ->
            AlertDialog.Builder(activity)
        }
        dialog.setMessage(message)
            .setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    object RepositoryDiffCallback : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem == newItem
        }
    }
}