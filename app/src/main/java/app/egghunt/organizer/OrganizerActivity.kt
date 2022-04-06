package app.egghunt.organizer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.egghunt.R
import app.egghunt.action.scan.ScanActivity
import app.egghunt.competition.CompetitionActivity
import app.egghunt.competition.CompetitionManager
import app.egghunt.egg.EggAdapter
import app.egghunt.egg.EggManager
import app.egghunt.egg.EggRepo
import app.egghunt.hint.HintAdapter
import app.egghunt.hint.HintManager
import app.egghunt.hint.HintRepo
import app.egghunt.lib.Code
import app.egghunt.lib.Extras
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrganizerActivity : CompetitionActivity(R.layout.activity_organizer) {
    private var eggAdapter: EggAdapter? = null
    private var hintAdapter: HintAdapter? = null

    private fun doHide() {
        val intent = Intent(this, ScanActivity::class.java).apply {
            putExtra(Extras.TITLE, R.string.activity_scan_egg)
        }

        scanLauncher.launch(intent)
    }

    private fun doPost() {
        val edit: EditText = findViewById(R.id.hint)

        if (edit.text.isNotEmpty()) {
            HintManager.post(competition, edit.text.toString())

            edit.text.clear()
        }
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        // Remember the organizer.

        CompetitionManager.enterAsOrganizer(this, competitionDescription, competitionTag)

        // Initialize the pager.

        val pager: ViewPager2 = findViewById(R.id.pager)

        pager.adapter = OrganizerTabAdapter(this)

        // Initialize the tabs.

        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, pager) { tab, tabPosition ->
            tab.text = resources.getStringArray(R.array.tabs_organizer)[tabPosition]
        }.attach()
    }

    override fun onScanEgg(code: Code): Boolean {
        val result = super.onScanEgg(code)

        if (result) {
            EggManager.hide(code, competition)
        }

        return result
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
            eggAdapter = EggRepo.bind(competition, eggRecycler)
        }

        // Bind the hints.

        val hintRecycler: RecyclerView? = tab.findViewById(R.id.hints)

        if (hintRecycler != null) {
            hintAdapter?.stopListening()
            hintAdapter = HintRepo.bind(competition, hintRecycler)
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
}
