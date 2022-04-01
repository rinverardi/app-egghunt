package app.egghunt.lib

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.egghunt.R

class PopupDialog(action: String, body: Int, title: Int) : DialogFragment() {
    init {
        val arguments = Bundle()

        arguments.putString(ARGUMENT_ACTION, action)
        arguments.putInt(ARGUMENT_BODY, body)
        arguments.putInt(ARGUMENT_TITLE, title)

        setArguments(arguments)
    }

    override fun onCreateDialog(state: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
            .setMessage(requireArguments().getInt(ARGUMENT_BODY))
            .setTitle(requireArguments().getInt(ARGUMENT_TITLE))

        val action = requireArguments().getString(ARGUMENT_ACTION)!!

        if (action == ACTION_LOGOUT) {
            builder
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.button_logout) { dialog, _ ->
                    parentFragmentManager.setFragmentResult(action, Bundle())
                    dialog.dismiss()
                }
        } else {
            builder.setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
        }

        return builder.create()
    }

    companion object {
        const val ACTION_INFO = "info"
        const val ACTION_LOGOUT = "logout"

        const val ARGUMENT_ACTION = "action"
        const val ARGUMENT_BODY = "body"
        const val ARGUMENT_TITLE = "title"
    }
}
