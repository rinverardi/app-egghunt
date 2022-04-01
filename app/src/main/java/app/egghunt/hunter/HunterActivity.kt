package app.egghunt.hunter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.egghunt.R
import app.egghunt.action.scan.ScanActivity
import app.egghunt.competition.CompetitionRepo
import app.egghunt.egg.EggAdapter
import app.egghunt.egg.EggRepo
import app.egghunt.lib.Code
import app.egghunt.lib.CodeParser
import app.egghunt.lib.LocalData
import app.egghunt.lib.Popup
import app.egghunt.welcome.WelcomeActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference

class HunterActivity : AppCompatActivity() {
    private lateinit var competition: DatabaseReference
    private lateinit var competitionDescription: String
    private lateinit var competitionTag: String
    private var eggAdapter: EggAdapter? = null
    private lateinit var hunterDescription: String
    private lateinit var hunterTag: String

    private val scanLauncher =
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

    private fun doFind() {
        val intent = Intent(this, ScanActivity::class.java)

        intent.putExtra(ScanActivity.EXTRA_TITLE, R.string.activity_scan_egg)

        scanLauncher.launch(intent)
    }

    private fun doLogout() {
        supportFragmentManager.setFragmentResultListener(Popup.KEY_LOGOUT, this) { _, _ ->
            LocalData.forgetCurrentHunter(this)

            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }

        val dialog = Popup(
            Popup.KEY_LOGOUT,
            R.string.warning_logout,
            R.string.exclamation_whoa
        )

        dialog.show(supportFragmentManager, null)
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        setContentView(R.layout.activity_hunter)

        // Remember the hunter.

        val extras = intent.extras!!

        competitionDescription = extras.getString(EXTRA_COMPETITION_DESCRIPTION)!!
        competitionTag = extras.getString(EXTRA_COMPETITION_TAG)!!
        hunterDescription = extras.getString(EXTRA_HUNTER_DESCRIPTION)!!
        hunterTag = extras.getString(EXTRA_HUNTER_TAG)!!

        LocalData.setCurrentHunter(
            this,
            competitionDescription,
            competitionTag,
            hunterDescription,
            hunterTag
        )

        // Sync the competition.

        competition = CompetitionRepo.sync(competitionDescription, competitionTag)

        // Initialize the pager.

        val pager: ViewPager2 = findViewById(R.id.pager)

        pager.adapter = HunterTabAdapter(this)

        // Initialize the tabs.

        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, pager) { tab, tabPosition ->
            tab.text = resources.getStringArray(R.array.tabs_hunter)[tabPosition]
        }.attach()

        // Initialize the toolbar.

        val toolbar = findViewById<Toolbar>(R.id.toolbar)!!

        toolbar.navigationIcon = AppCompatResources.getDrawable(this, R.drawable.ic_action_close)

        if (!TextUtils.isEmpty(hunterDescription)) {
            toolbar.title = hunterDescription
        }

        toolbar.setNavigationOnClickListener {
            doLogout()
        }
    }

    private fun onScanCompetition() {
        val dialog = Popup(
            Popup.KEY_INFO,
            R.string.error_unexpected_competition,
            R.string.exclamation_oops
        )

        dialog.show(supportFragmentManager, null)
    }

    private fun onScanEgg(code: Code) {
        if (competitionTag != code.ct) {
            val dialog = Popup(
                Popup.KEY_INFO,
                R.string.error_different_competition,
                R.string.exclamation_psst
            )

            dialog.show(supportFragmentManager, null)
            return
        }

        // @remo Implement me!

    }

    private fun onScanHunter() {
        val dialog = Popup(
            Popup.KEY_INFO,
            R.string.error_unexpected_hunter,
            R.string.exclamation_oops
        )

        dialog.show(supportFragmentManager, null)
    }

    // @remo Fix me!

    /*
    override fun onStart() {
        super.onStart()

        eggAdapter?.startListening()
    }
    */

    // @remo Fix me!

    /*
    override fun onStop() {
        eggAdapter?.stopListening()

        super.onStop()
    }
    */

    fun rebind(tab: View) {

        // Bind the eggs.

        val eggRecycler: RecyclerView? = tab.findViewById(R.id.eggs)

        if (eggRecycler != null) {
            eggAdapter?.stopListening()
            eggAdapter = EggRepo.bind(competition, eggRecycler)
        }
    }

    fun reinitialize(tab: View) {

        // Initialize the 'find' button.

        val findButton: Button? = tab.findViewById(R.id.button_find)

        findButton?.setOnClickListener {
            doFind()
        }

        // Initialize the 'hide' button.

        val hideButton: Button? = tab.findViewById(R.id.button_hide)

        hideButton?.visibility = View.GONE
    }

    companion object {
        const val EXTRA_COMPETITION_DESCRIPTION = "competition_description"
        const val EXTRA_COMPETITION_TAG = "competition_tag"
        const val EXTRA_HUNTER_DESCRIPTION = "hunter_description"
        const val EXTRA_HUNTER_TAG = "hunter_tag"
    }
}
