package app.egghunt.hunter

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.egghunt.R
import app.egghunt.action.scan.ScanActivity
import app.egghunt.competition.CompetitionActivity
import app.egghunt.competition.CompetitionManager
import app.egghunt.egg.EggAdapter
import app.egghunt.egg.EggManager
import app.egghunt.egg.EggRepo
import app.egghunt.lib.Code
import app.egghunt.lib.Extras
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HunterActivity : CompetitionActivity(R.layout.activity_hunter) {
    private var eggAdapter: EggAdapter? = null
    private lateinit var hunterDescription: String
    private lateinit var hunterTag: String

    private fun doFind() {
        val intent = Intent(this, ScanActivity::class.java)

        intent.putExtra(Extras.TITLE, R.string.activity_scan_egg)

        scanLauncher.launch(intent)
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        // Remember the hunter.

        val extras = intent.extras!!

        hunterDescription = extras.getString(Extras.HUNTER_DESCRIPTION)!!
        hunterTag = extras.getString(Extras.HUNTER_TAG)!!

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
            tab.text = resources.getStringArray(R.array.tabs_hunter)[tabPosition]
        }.attach()

        // Initialize the toolbar.

        val toolbar = findViewById<Toolbar>(R.id.toolbar)!!

        if (!TextUtils.isEmpty(hunterDescription)) {
            toolbar.title = hunterDescription
        }
    }

    override fun onScanEgg(code: Code) {
        super.onScanEgg(code)

        EggManager.find(competition, code, hunterDescription, hunterTag)
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
}
