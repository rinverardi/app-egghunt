package app.egghunt.action.switch

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.egghunt.R
import app.egghunt.lib.Actions
import app.egghunt.lib.Arguments
import app.egghunt.lib.Extras

/**
 * A dialog to switch the current hunter.
 *
 * _Start Parameters_
 *
 * * Arguments.HUNTER_DESCRIPTIONS -- the list of hunter descriptions
 * * Arguments.HUNTER_TAGS -- the list of hunter tags
 *
 * _Result Data_
 *
 * * Extras.HUNTER_DESCRIPTION -- the hunter description
 * * Extras.HUNTER_TAG -- the hunter tag
 */

class SwitchDialog(hunterDescriptions: Array<String>, hunterTags: Array<String>) :
    DialogFragment() {

    init {
        val arguments = Bundle()

        arguments.putStringArray(Arguments.HUNTER_DESCRIPTIONS, hunterDescriptions)
        arguments.putStringArray(Arguments.HUNTER_TAGS, hunterTags)

        setArguments(arguments)
    }

    override fun onCreateDialog(state: Bundle?): Dialog {
        val hunterDescriptions = requireArguments().getStringArray(Arguments.HUNTER_DESCRIPTIONS)!!
        val hunterTags = requireArguments().getStringArray(Arguments.HUNTER_TAGS)!!

        val builder = AlertDialog.Builder(activity)
            .setItems(hunterDescriptions) { dialog, item ->
                parentFragmentManager.setFragmentResult(Actions.SWITCH, Bundle().apply {
                    putString(Extras.HUNTER_DESCRIPTION, hunterDescriptions[item])
                    putString(Extras.HUNTER_TAG, hunterTags[item])
                })

                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setTitle(R.string.action_switch)

        return builder.create()
    }
}
