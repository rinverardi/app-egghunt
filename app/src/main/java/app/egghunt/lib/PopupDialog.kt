package app.egghunt.lib

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.egghunt.R

/**
 * A popup dialog.
 *
 * _Key Responsibilities_
 *
 * * Ask a user for confirmation; e.g. Actions.LOGOUT.
 * * Present feedback to a user.
 *
 * _Start Parameters_
 *
 * * Arguments.ACTION (optional) -- the action that caused the popup dialog
 * * Arguments.BODY (required) -- the text for the dialog body
 * * Arguments.TITLE (required) -- the text for the dialog title
 */

class PopupDialog(action: String?, body: Int, title: Int) : DialogFragment() {
    init {
        val arguments = Bundle()

        arguments.putString(Arguments.ACTION, action)
        arguments.putInt(Arguments.BODY, body)
        arguments.putInt(Arguments.TITLE, title)

        setArguments(arguments)
    }

    override fun onCreateDialog(state: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
            .setMessage(requireArguments().getInt(Arguments.BODY))
            .setTitle(requireArguments().getInt(Arguments.TITLE))

        val action = requireArguments().getString(Arguments.ACTION)

        if (action == Actions.LOGOUT) {
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
}
