package app.egghunt.hunter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import app.egghunt.R
import app.egghunt.action.scan.ScanActivity
import app.egghunt.lib.code.Code
import app.egghunt.lib.code.CodeParser
import app.egghunt.lib.error.ErrorDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HunterActivity : AppCompatActivity() {
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

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        setContentView(R.layout.activity_hunter)

        // Initialize the pager.

        val pager: ViewPager2 = findViewById(R.id.pager)

        pager.adapter = HunterPageAdapter(this)

        // Initialize the tabs.

        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, pager) { tab, tabPosition ->
            tab.text = resources.getStringArray(R.array.tabs_hunter)[tabPosition]
        }.attach()
    }

    private fun onScanCompetition() {
        val dialog = ErrorDialog(
            R.string.error_message_unexpected_competition_code,
            R.string.error_title
        )

        dialog.show(supportFragmentManager, null)
    }

    private fun onScanEgg(code: Code) {

        // @remo Implement me!

    }

    private fun onScanHunter() {
        val dialog = ErrorDialog(
            R.string.error_message_unexpected_hunter_code,
            R.string.error_title
        )

        dialog.show(supportFragmentManager, null)
    }

    fun scan() {
        val intent = Intent(this, ScanActivity::class.java)

        intent.putExtra(ScanActivity.EXTRA_TITLE, R.string.activity_scan_egg)

        scanLauncher.launch(intent)
    }

    companion object {
        const val EXTRA_COMPETITION_DESCRIPTION = "competition_description"
        const val EXTRA_COMPETITION_TAG = "competition_tag"
        const val EXTRA_HUNTER_DESCRIPTION = "hunter_description"
        const val EXTRA_HUNTER_TAG = "hunter_tag"
    }
}
