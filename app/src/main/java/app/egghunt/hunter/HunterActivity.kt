package app.egghunt.hunter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import app.egghunt.R
import app.egghunt.action.scan.ScanActivity
import app.egghunt.lib.Code
import app.egghunt.lib.CodeParser
import app.egghunt.lib.Data
import app.egghunt.lib.DataBinding
import app.egghunt.lib.EggAdapter
import app.egghunt.lib.Popup
import app.egghunt.organizer.OrganizerActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference

class HunterActivity : AppCompatActivity() {
    private lateinit var competitionReference: DatabaseReference
    private var eggAdapter: EggAdapter? = null

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

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        setContentView(R.layout.activity_hunter)

        // Sync the competition.

        competitionReference = Data.syncCompetition(
            intent.extras!!.getString(OrganizerActivity.EXTRA_COMPETITION_DESCRIPTION)!!,
            intent.extras!!.getString(OrganizerActivity.EXTRA_COMPETITION_TAG)!!
        )

        // Initialize the pager.

        val pager: ViewPager2 = findViewById(R.id.pager)

        pager.adapter = HunterTabAdapter(this)

        // Initialize the tabs.

        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, pager) { tab, tabPosition ->
            tab.text = resources.getStringArray(R.array.tabs_hunter)[tabPosition]
        }.attach()
    }

    private fun onScanCompetition() {
        val dialog = Popup(
            R.string.error_message_unexpected_competition_code,
            R.string.error_title
        )

        dialog.show(supportFragmentManager, null)
    }

    // @remo Implement me!

    private fun onScanEgg(code: Code) {}

    private fun onScanHunter() {
        val dialog = Popup(
            R.string.error_message_unexpected_hunter_code,
            R.string.error_title
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
            eggAdapter = DataBinding.bindEggs(competitionReference, eggRecycler)
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
