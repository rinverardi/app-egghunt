package app.egghunt.competition

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import app.egghunt.R
import app.egghunt.lib.Actions
import app.egghunt.lib.Code
import app.egghunt.lib.CodeParser
import app.egghunt.lib.Extras
import app.egghunt.lib.Messaging
import app.egghunt.lib.PopupDialog
import app.egghunt.lib.PopupNotification
import app.egghunt.score.Rank
import app.egghunt.score.ScoreAdapter
import app.egghunt.welcome.WelcomeActivity
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson

abstract class CompetitionActivity(private val layout: Int) : AppCompatActivity() {
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Actions.NEW_MESSAGE -> onNewMessage(intent)
                Actions.NEW_SCORES -> onNewScores(intent)
            }
        }
    }

    protected lateinit var competition: DatabaseReference
    protected lateinit var competitionDescription: String
    protected lateinit var competitionTag: String
    protected val scoreAdapter = ScoreAdapter()

    protected val scanLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val codeString = result.data!!.getStringExtra(Intent.EXTRA_TEXT)!!

                val code = CodeParser.parse(codeString)

                when {
                    code == null -> onCrappyCode()
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

    private fun onCrappyCode() {
        val dialog = PopupDialog(
            null,
            R.string.error_crappy_code,
            R.string.exclamation_no
        )

        dialog.show(supportFragmentManager, null)
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        setContentView(layout)

        // Listen for broadcasts.

        val intentFilter = IntentFilter().apply {
            addAction(Actions.NEW_MESSAGE)
            addAction(Actions.NEW_SCORES)
        }

        registerReceiver(broadcastReceiver, intentFilter)

        // Attach the database.

        val extras = intent.extras!!

        competitionDescription = extras.getString(Extras.COMPETITION_DESCRIPTION)!!
        competitionTag = extras.getString(Extras.COMPETITION_TAG)!!

        competition = CompetitionRepo.sync(competitionDescription, competitionTag)

        // Attach the messaging.

        Messaging.register(this)

        // Initialize the toolbar.

        findViewById<Toolbar>(R.id.toolbar)!!.setNavigationOnClickListener {
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

    private fun onNewScores(intent: Intent) {
        val list = ArrayList<Any>()

        Gson().fromJson(
            intent.getStringExtra(Extras.RANKS)!!,
            Array<Rank>::class.java
        ).forEach {
            list.add(it)
            list.addAll(it.scores)
        }

        scoreAdapter.submitList(list)
    }

    private fun onScanCompetition() {
        val dialog = PopupDialog(
            null,
            R.string.error_unexpected_competition,
            R.string.exclamation_oops
        )

        dialog.show(supportFragmentManager, null)
    }

    protected open fun onScanEgg(code: Code): Boolean {
        val result = competitionTag == code.ct

        if (!result) {
            val dialog = PopupDialog(
                null,
                R.string.error_different_competition,
                R.string.exclamation_psst
            )

            dialog.show(supportFragmentManager, null)
        }

        return result
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
