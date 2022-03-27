package app.egghunt.lib

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class Popup : DialogFragment {
    constructor() : super()

    constructor(message: Int, title: Int) : super() {
        val arguments = Bundle()

        arguments.putInt(ARGUMENT_MESSAGE, message)
        arguments.putInt(ARGUMENT_TITLE, title)

        setArguments(arguments)
    }

    override fun onCreateDialog(state: Bundle?): Dialog = AlertDialog.Builder(activity)
        .setMessage(requireArguments().getInt(ARGUMENT_MESSAGE))
        .setTitle(requireArguments().getInt(ARGUMENT_TITLE))
        .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
        .create()

    companion object {
        const val ARGUMENT_MESSAGE = "message"
        const val ARGUMENT_TITLE = "title"
    }
}
