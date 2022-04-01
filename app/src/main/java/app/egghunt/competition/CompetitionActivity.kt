package app.egghunt.competition

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import app.egghunt.R
import app.egghunt.lib.Actions
import app.egghunt.lib.Code
import app.egghunt.lib.CodeParser
import app.egghunt.lib.Extras
import app.egghunt.lib.Messaging
import app.egghunt.lib.PopupDialog
import app.egghunt.lib.PopupNotification
import app.egghunt.welcome.WelcomeActivity
import com.google.firebase.database.DatabaseReference

abstract class CompetitionActivity(private val layout: Int) : AppCompatActivity() {
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Actions.NEW_MESSAGE) {
                onNewMessage(intent)
            }
        }
    }

    protected lateinit var competition: DatabaseReference
    protected lateinit var competitionDescription: String
    protected lateinit var competitionTag: String

    protected val scanLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val codeString = result.data!!.getStringExtra(Intent.EXTRA_TEXT)!!

                val code = CodeParser.parse(codeString)

                when {
                    code.isEgg() -> onScanEgg(code)
                    code.isHunter() -> onScanHunter()
                    else -> onScanCompetition()
                }
            }
        }

    private fun doLogout() {
        supportFragmentManager.setFragmentResultListener(Actions.LOGOUT, this) { _, _ ->
            CompetitionManager.leave(this, competitionDescription, competitionTag)

            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }

        val dialog = PopupDialog(
            Actions.LOGOUT,
            R.string.warning_logout,
            R.string.exclamation_whoa
        )

        dialog.show(supportFragmentManager, null)
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        setContentView(layout)

        // Attach the database.

        val extras = intent.extras!!

        competitionDescription = extras.getString(Extras.COMPETITION_DESCRIPTION)!!
        competitionTag = extras.getString(Extras.COMPETITION_TAG)!!

        competition = CompetitionRepo.sync(competitionDescription, competitionTag)

        // Attach the messaging.

        registerReceiver(broadcastReceiver, IntentFilter(Actions.NEW_MESSAGE))

        Messaging.register(this)

        // Initialize the toolbar.

        val toolbar = findViewById<Toolbar>(R.id.toolbar)!!

        toolbar.navigationIcon = AppCompatResources.getDrawable(this, R.drawable.ic_action_close)

        toolbar.setNavigationOnClickListener {
            doLogout()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)

        super.onDestroy()
    }

    private fun onNewMessage(intent: Intent) = PopupNotification(this)
        .setBody(intent.getStringExtra(Extras.BODY)!!)
        .setTitle(intent.getStringExtra(Extras.TITLE)!!)
        .show()

    private fun onScanCompetition() {
        val dialog = PopupDialog(
            null,
            R.string.error_unexpected_competition,
            R.string.exclamation_oops
        )

        dialog.show(supportFragmentManager, null)
    }

    protected open fun onScanEgg(code: Code) {
        val competitionTag = intent.getStringExtra(Extras.COMPETITION_TAG)!!

        if (competitionTag != code.ct) {
            val dialog = PopupDialog(
                null,
                R.string.error_different_competition,
                R.string.exclamation_psst
            )

            dialog.show(supportFragmentManager, null)
            return
        }
    }

    private fun onScanHunter() {
        val dialog = PopupDialog(
            null,
            R.string.error_unexpected_hunter,
            R.string.exclamation_oops
        )

        dialog.show(supportFragmentManager, null)
    }
}
