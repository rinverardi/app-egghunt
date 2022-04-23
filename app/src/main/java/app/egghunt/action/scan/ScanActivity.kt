package app.egghunt.action.scan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import app.egghunt.BuildConfig
import app.egghunt.R
import app.egghunt.lib.Extras
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat

class ScanActivity : AppCompatActivity() {
    private lateinit var scanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scan)

        // Initialize the action bar.

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setTitle(intent.getIntExtra(Extras.TITLE, R.string.activity_scan))

        // Initialize the scanner.

        scanner = CodeScanner(this, findViewById(R.id.scanner)).apply {
            autoFocusMode = AutoFocusMode.SAFE
            camera = CodeScanner.CAMERA_BACK

            decodeCallback = DecodeCallback { result ->
                runOnUiThread {
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(Intent.EXTRA_TEXT, result.text)
                    })

                    finish()
                }
            }

            errorCallback = ErrorCallback.SUPPRESS
            formats = listOf(BarcodeFormat.QR_CODE)
            isAutoFocusEnabled = true
            isFlashEnabled = false
            scanMode = ScanMode.SINGLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return if (BuildConfig.DEBUG) {
            menuInflater.inflate(R.menu.menu_scan, menu)
            true
        } else {
            super.onCreateOptionsMenu(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.fake_competition -> {
                fakeCompetition()
                true
            }

            R.id.fake_egg -> {
                fakeEgg()
                true
            }

            R.id.fake_hunter_1 -> {
                fakeHunter("1")
                true
            }

            R.id.fake_hunter_2 -> {
                fakeHunter("2")
                true
            }

            R.id.test_hunter_3 -> {
                fakeHunter("3")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    private fun fakeCompetition() {
        intent.putExtra(
            Intent.EXTRA_TEXT, "{" +
                    "\"cd\":\"Fake Competition\"," +
                    "\"ct\":\"CCCCCC\"}"
        )

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun fakeEgg() {
        intent.putExtra(
            Intent.EXTRA_TEXT, "{" +
                    "\"cd\":\"Fake Competition\"," +
                    "\"ct\":\"CCCCCC\"," +
                    "\"ed\":\"Fake Egg\"," +
                    "\"et\":\"EEEEEE\"}"
        )

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun fakeHunter(suffix: String) {
        intent.putExtra(
            Intent.EXTRA_TEXT, "{" +
                    "\"cd\":\"Fake Competition\"," +
                    "\"ct\":\"CCCCCC\"," +
                    "\"hd\":\"Fake Hunter $suffix\"," +
                    "\"ht\":\"HHHHH$suffix\"}"
        )

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onResume() {
        super.onResume()

        scanner.startPreview()
    }

    override fun onPause() {
        scanner.releaseResources()

        super.onPause()
    }
}