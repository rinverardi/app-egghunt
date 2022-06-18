package app.egghunt.organizer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.egghunt.R
import app.egghunt.action.position.PositionActivity
import app.egghunt.action.scan.ScanActivity
import app.egghunt.competition.CompetitionActivity
import app.egghunt.competition.CompetitionManager
import app.egghunt.egg.Egg
import app.egghunt.egg.EggAdapter
import app.egghunt.egg.EggListener
import app.egghunt.egg.EggManager
import app.egghunt.egg.EggRepo
import app.egghunt.hint.HintAdapter
import app.egghunt.hint.HintManager
import app.egghunt.hint.HintRepo
import app.egghunt.lib.Code
import app.egghunt.lib.Extras
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * An activity for participating in a competition as the organizer.
 *
 * _Start Parameters_
 *
 * * Extras.COMPETITION_DESCRIPTION (required) -- the competition description
 * * Extras.COMPETITION_TAG (required) -- the competition tag
 */

class OrganizerActivity : CompetitionActivity(R.layout.activity_organizer) {
    private var eggAdapter: EggAdapter? = null

    private val eggListener = object : EggListener {
        override fun onClick(egg: Egg) {
            val context = this@OrganizerActivity

            val intent = Intent(context, PositionActivity::class.java).apply {
                putExtra(Extras.EGG_TAG, egg.tag)
                putExtra(Extras.POSITION_LATITUDE, egg.positionLatitude)
                putExtra(Extras.POSITION_LONGITUDE, egg.positionLongitude)
            }

            positionLauncher.launch(intent)
        }
    }

    private var hintAdapter: HintAdapter? = null

    private val positionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                EggManager.position(
                    competition,
                    result.data!!.getStringExtra(Extras.EGG_TAG)!!,
                    Extras.getPositionLatitude(result.data),
                    Extras.getPositionLongitude(result.data)
                )
            }
        }

    private fun doHide() {
        val intent = Intent(this, ScanActivity::class.java).apply {
            putExtra(Extras.TITLE, R.string.activity_scan_egg)
        }

        scanLauncher.launch(intent)
    }

    private fun doPost() {
        val edit: EditText = findViewById(R.id.edit_post)

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
            tab.text = resources.getStringArray(R.array.tabs)[tabPosition]
        }.attach()
    }

    override fun onScanEgg(code: Code): Boolean {
        val result = super.onScanEgg(code)

        if (result) {
            EggManager.hide(competition, code.ed!!, code.et!!)

            val intent = Intent(this, PositionActivity::class.java).apply {
                putExtra(Extras.EGG_TAG, code.et)
            }

            positionLauncher.launch(intent)
        }

        return result
    }

    fun rebind(tab: View) {

        // Bind the eggs.

        val eggRecycler: RecyclerView? = tab.findViewById(R.id.eggs)

        if (eggRecycler != null) {
            eggAdapter?.stopListening()
            eggAdapter = EggRepo.bind(competition, eggListener, eggRecycler)
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
        tab.findViewById<View>(R.id.button_find)?.visibility = View.GONE

        tab.findViewById<Button>(R.id.button_hide)?.setOnClickListener {
            doHide()
        }

        tab.findViewById<Button>(R.id.button_post)?.setOnClickListener {
            doPost()
        }
    }
}
