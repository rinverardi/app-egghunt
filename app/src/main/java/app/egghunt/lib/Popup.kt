package app.egghunt.lib

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.egghunt.R

class Popup : DialogFragment {
    constructor() : super()

    constructor(key: String, message: Int, title: Int) : super() {
        val arguments = Bundle()

        arguments.putString(ARGUMENT_KEY, key)
        arguments.putInt(ARGUMENT_MESSAGE, message)
        arguments.putInt(ARGUMENT_TITLE, title)

        setArguments(arguments)
    }

    override fun onCreateDialog(state: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
            .setMessage(requireArguments().getInt(ARGUMENT_MESSAGE))
            .setTitle(requireArguments().getInt(ARGUMENT_TITLE))

        val key = requireArguments().getString(ARGUMENT_KEY)!!

        if (key == KEY_LOGOUT) {
            builder
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.button_logout) { dialog, _ ->
                    parentFragmentManager.setFragmentResult(key, Bundle())
                    dialog.dismiss()
                }
        } else {
            builder.setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
        }

        return builder.create()
    }

    companion object {
        const val ARGUMENT_KEY = "key"
        const val ARGUMENT_MESSAGE = "message"
        const val ARGUMENT_TITLE = "title"

        const val KEY_INFO = "info"
        const val KEY_LOGOUT = "logout"
    }
}
