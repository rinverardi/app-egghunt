package app.egghunt.hunter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.egghunt.R
import app.egghunt.action.scan.ScanActivity
import app.egghunt.action.switch.SwitchDialog
import app.egghunt.competition.CompetitionActivity
import app.egghunt.competition.CompetitionManager
import app.egghunt.egg.EggAdapter
import app.egghunt.egg.EggManager
import app.egghunt.egg.EggRepo
import app.egghunt.hint.HintAdapter
import app.egghunt.hint.HintRepo
import app.egghunt.lib.Actions
import app.egghunt.lib.Code
import app.egghunt.lib.Extras
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HunterActivity : CompetitionActivity(R.layout.activity_hunter) {
    private var eggAdapter: EggAdapter? = null
    private var hintAdapter: HintAdapter? = null
    private lateinit var hunterDescription: String
    private lateinit var hunterTag: String

    private fun doFind() {
        val intent = Intent(this, ScanActivity::class.java).apply {
            putExtra(Extras.TITLE, R.string.activity_scan_egg)
        }

        scanLauncher.launch(intent)
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        // Remember the hunter.

        hunterDescription = intent.getStringExtra(Extras.HUNTER_DESCRIPTION)!!
        hunterTag = intent.getStringExtra(Extras.HUNTER_TAG)!!

        CompetitionManager.enterAsHunter(
            this,
            competitionDescription,
            competitionTag,
            hunterDescription,
            hunterTag
        )

        // Initialize the pager.

        val pager: ViewPager2 = findViewById(R.id.pager)

        pager.adapter = HunterTabAdapter(this)

        // Initialize the tabs.

        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, pager) { tab, tabPosition ->
            tab.text = resources.getStringArray(R.array.tabs)[tabPosition]
        }.attach()

        // Initialize the toolbar.

        findViewById<Toolbar>(R.id.toolbar)!!.title = hunterDescription

        findViewById<Toolbar>(R.id.toolbar)!!.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_switch) {
                switch()
                true
            } else {
                false
            }
        }
    }

    override fun onScanEgg(code: Code): Boolean {
        val result = super.onScanEgg(code)

        if (result) {
            EggManager.find(competition, code.ed!!, code.et!!, hunterDescription, hunterTag)
        }

        return result
    }

    fun rebind(tab: View) {

        // Bind the eggs.

        val eggRecycler: RecyclerView? = tab.findViewById(R.id.eggs)

        if (eggRecycler != null) {
            eggAdapter?.stopListening()
            eggAdapter = EggRepo.bind(competition, null, eggRecycler)
        }

        // Bind the hints.

        val hintRecycler: RecyclerView? = tab.findViewById(R.id.hints)

        if (hintRecycler != null) {
            hintAdapter?.stopListening()
            hintAdapter = HintRepo.bind(competition, hintRecycler)
        }

        // Bind the scores.

        val scoreRecycler: RecyclerView? = tab.findViewById(R.id.scores)

        scoreRecycler?.apply {
            adapter = scoreAdapter
            layoutManager = LinearLayoutManager(scoreRecycler.context)
        }
    }

    fun reinitialize(tab: View) {
        tab.findViewById<Button>(R.id.button_find)?.setOnClickListener {
            doFind()
        }

        tab.findViewById<View>(R.id.button_hide)?.visibility = View.GONE
        tab.findViewById<View>(R.id.button_post)?.visibility = View.GONE
        tab.findViewById<View>(R.id.edit_post)?.visibility = View.GONE
    }

    private fun switch() {
        supportFragmentManager.setFragmentResultListener(Actions.SWITCH, this) { _, result ->
            hunterDescription = result.getString(Extras.HUNTER_DESCRIPTION)!!
            hunterTag = result.getString(Extras.HUNTER_TAG)!!

            findViewById<Toolbar>(R.id.toolbar)!!.title = hunterDescription
        }

        HunterRepo.list(competition, {}, { hunters ->
            val dialog = SwitchDialog(
                hunters.map { it.description }.toTypedArray(),
                hunters.map { it.tag }.toTypedArray()
            )

            dialog.show(supportFragmentManager, null)
        })
    }
}
