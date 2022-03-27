package app.egghunt.organizer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.egghunt.R
import app.egghunt.action.scan.ScanActivity
import app.egghunt.hunter.HunterActivity
import app.egghunt.lib.Code
import app.egghunt.lib.CodeParser
import app.egghunt.lib.Data
import app.egghunt.lib.DataBinding
import app.egghunt.lib.EggAdapter
import app.egghunt.lib.HintAdapter
import app.egghunt.lib.Popup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference

class OrganizerActivity : AppCompatActivity() {
    private lateinit var competitionReference: DatabaseReference
    private var eggAdapter: EggAdapter? = null
    private var hintAdapter: HintAdapter? = null

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

    private fun doHide() {
        val intent = Intent(this, ScanActivity::class.java)

        intent.putExtra(ScanActivity.EXTRA_TITLE, R.string.activity_scan_egg)

        scanLauncher.launch(intent)
    }

    private fun doPost() {
        val edit: EditText = findViewById(R.id.hint)

        val hint = competitionReference.child("hint").child(Data.tag())

        hint.child("order").setValue(-System.currentTimeMillis())
        hint.child("postedAt").setValue(System.currentTimeMillis())
        hint.child("text").setValue(edit.text.toString())

        edit.text.clear()
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        setContentView(R.layout.activity_organizer)

        // Sync the competition.

        competitionReference = Data.syncCompetition(
            intent.extras!!.getString(EXTRA_COMPETITION_DESCRIPTION)!!,
            intent.extras!!.getString(EXTRA_COMPETITION_TAG)!!
        )

        // Initialize the pager.

        val pager: ViewPager2 = findViewById(R.id.pager)

        pager.adapter = OrganizerTabAdapter(this)

        // Initialize the tabs.

        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, pager) { tab, tabPosition ->
            tab.text = resources.getStringArray(R.array.tabs_organizer)[tabPosition]
        }.attach()
    }

    private fun onScanCompetition() {
        val dialog = Popup(
            R.string.error_message_unexpected_competition,
            R.string.error_title_oops
        )

        dialog.show(supportFragmentManager, null)
    }

    private fun onScanEgg(code: Code) {
        val competitionTag = intent.getStringExtra(HunterActivity.EXTRA_COMPETITION_TAG)!!

        if (competitionTag != code.ct) {
            val dialog = Popup(
                R.string.error_message_different_competition,
                R.string.error_title_psst
            )

            dialog.show(supportFragmentManager, null)
            return
        }

        val egg = competitionReference.child("egg").child(code.et!!)

        egg.child("description").setValue(code.ed)
        egg.child("hidden_at").setValue(System.currentTimeMillis())
    }

    private fun onScanHunter() {
        val dialog = Popup(
            R.string.error_message_unexpected_hunter,
            R.string.error_title_oops
        )

        dialog.show(supportFragmentManager, null)
    }

    // @remo Fix me!

    /*
    override fun onStart() {
        super.onStart()

        eggAdapter?.startListening()
        hintAdapter?.startListening()
    }
    */

    // @remo Fix me!

    /*
    override fun onStop() {
        eggAdapter?.stopListening()
        hintAdapter?.stopListening()

        super.onStop()
    }
    */

    fun rebind(tab: View) {

        // Bind the eggs.

        val eggRecycler: RecyclerView? = tab.findViewById(R.id.eggs)

        if (eggRecycler != null) {
            eggAdapter?.stopListening()
            eggAdapter = DataBinding.bindEggs(competitionReference, eggRecycler)
        }

        // Bind the hints.

        val hintRecycler: RecyclerView? = tab.findViewById(R.id.hints)

        if (hintRecycler != null) {
            hintAdapter?.stopListening()
            hintAdapter = DataBinding.bindHints(competitionReference, hintRecycler)
        }
    }

    fun reinitialize(tab: View) {

        // Initialize the 'find' button.

        val findButton: Button? = tab.findViewById(R.id.button_find)

        findButton?.visibility = View.GONE

        // Initialize the 'hide' button.

        val hintButton: Button? = tab.findViewById(R.id.button_hide)

        hintButton?.setOnClickListener {
            doHide()
        }

        // Initialize the 'post' button.

        val postButton: Button? = tab.findViewById(R.id.button_post)

        postButton?.setOnClickListener {
            doPost()
        }
    }

    companion object {
        const val EXTRA_COMPETITION_DESCRIPTION = "competition_description"
        const val EXTRA_COMPETITION_TAG = "competition_tag"
    }
}
