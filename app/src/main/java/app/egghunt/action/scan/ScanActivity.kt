package app.egghunt.action.scan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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
                    val intent = Intent()

                    intent.putExtra(Intent.EXTRA_TEXT, result.text)

                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

            errorCallback = ErrorCallback.SUPPRESS
            formats = listOf(BarcodeFormat.QR_CODE)
            isAutoFocusEnabled = true
            isFlashEnabled = false
            scanMode = ScanMode.SINGLE
        }

        // @remo Remove me!

        findViewById<Button>(R.id.c).setOnClickListener {
            intent.putExtra(Intent.EXTRA_TEXT, "{" +
                    "\"cd\":\"The Competition\"," +
                    "\"ct\":\"CCCCCC\"}")

            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        findViewById<Button>(R.id.e).setOnClickListener {
            intent.putExtra(Intent.EXTRA_TEXT, "{" +
                    "\"cd\":\"The Competition\"," +
                    "\"ct\":\"CCCCCC\"," +
                    "\"ed\":\"The Egg\"," +
                    "\"et\":\"EEEEEE\"}")

            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        findViewById<Button>(R.id.h).setOnClickListener {
            intent.putExtra(Intent.EXTRA_TEXT, "{" +
                    "\"cd\":\"The Competition\"," +
                    "\"ct\":\"CCCCCC\"," +
                    "\"hd\":\"The Hunter\"," +
                    "\"ht\":\"HHHHHH\"}")

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
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