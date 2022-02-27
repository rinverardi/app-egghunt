package app.egghunt.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.egghunt.R
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

        // Initialize the scanner.

        scanner = CodeScanner(this, findViewById(R.id.preview))

        scanner.autoFocusMode = AutoFocusMode.SAFE
        scanner.camera = CodeScanner.CAMERA_BACK

        scanner.decodeCallback = DecodeCallback { result ->
            runOnUiThread {
                val intent = Intent()

                intent.putExtra(Intent.EXTRA_TEXT, result.text)

                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        scanner.errorCallback = ErrorCallback.SUPPRESS
        scanner.formats = listOf(BarcodeFormat.QR_CODE)
        scanner.isAutoFocusEnabled = true
        scanner.isFlashEnabled = false
        scanner.scanMode = ScanMode.SINGLE
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