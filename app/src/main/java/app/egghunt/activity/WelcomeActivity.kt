package app.egghunt.activity

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.egghunt.R
import app.egghunt.code.Code
import app.egghunt.dialog.ErrorDialog
import com.google.gson.Gson
import kotlin.math.roundToLong

class WelcomeActivity : AppCompatActivity() {
    private val scanLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val codeString = result.data!!.getStringExtra(Intent.EXTRA_TEXT)

                val code = Gson().fromJson(codeString, Code::class.java)

                when {
                    code.et != null -> onScanEgg()
                    code.ht != null -> onScanHunter(code)
                    else -> onScanCompetition(code)
                }
            }
        }

    @Suppress("UNUSED_PARAMETER")
    fun onClickButton(view: View) {
        val status = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if (status == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        } else {
            scan()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_welcome)

        // Initialize the button.

        findViewById<View>(R.id.button).setOnClickListener(this::onClickButton)

        // Initialize the eggs.

        val eggs = arrayOf<ImageView>(
            findViewById(R.id.egg0),
            findViewById(R.id.egg1),
            findViewById(R.id.egg2),
            findViewById(R.id.egg3)
        )

        eggs.forEach { egg ->
            run {
                startRotation(egg)
                startTranslation(egg)
            }
        }
    }

    override fun onRequestPermissionsResult(
        code: Int,
        permissions: Array<String?>,
        results: IntArray
    ) {

        super.onRequestPermissionsResult(code, permissions, results)

        if (code == REQUEST_PERMISSION) {
            if (results[0] == PackageManager.PERMISSION_GRANTED) {
                Toast
                    .makeText(this, R.string.toast_camera_access_granted, Toast.LENGTH_SHORT)
                    .show()

                scan()
            } else {
                Toast
                    .makeText(this, R.string.toast_camera_access_denied, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun onScanCompetition(code: Code) {
        val intent = Intent(this, MapActivity::class.java)

        intent.putExtra(MapActivity.EXTRA_COMPETITION_DESCRIPTION, code.cd)
        intent.putExtra(MapActivity.EXTRA_COMPETITION_TAG, code.ct)

        startActivity(intent)
    }

    private fun onScanEgg() {
        val dialog = ErrorDialog(
            R.string.error_message_unexpected_egg_code,
            R.string.error_title
        )

        dialog.show(supportFragmentManager, null)
    }

    private fun onScanHunter(code: Code) {
        val intent = Intent(this, MapActivity::class.java)

        intent.putExtra(MapActivity.EXTRA_COMPETITION_DESCRIPTION, code.cd)
        intent.putExtra(MapActivity.EXTRA_COMPETITION_TAG, code.ct)
        intent.putExtra(MapActivity.EXTRA_HUNTER_DESCRIPTION, code.hd)
        intent.putExtra(MapActivity.EXTRA_HUNTER_TAG, code.ht)

        startActivity(intent)
    }

    private fun scan() {
        val intent = Intent(this, ScanActivity::class.java)

        scanLauncher.launch(intent)
    }

    private fun startRotation(egg: View) {
        val rotate = ObjectAnimator.ofFloat(egg, View.ROTATION, -20f, 20f)

        rotate.duration = (5000 + Math.random() * 1000).roundToLong()
        rotate.repeatCount = ValueAnimator.INFINITE
        rotate.repeatMode = ValueAnimator.REVERSE

        rotate.start()
    }

    private fun startTranslation(egg: View) {
        val translate = ObjectAnimator.ofFloat(egg, View.TRANSLATION_Y, -300f, 0f)

        translate.interpolator = BounceInterpolator()
        translate.duration = (2000 + Math.random() * 1000).roundToLong()
        translate.repeatCount = ValueAnimator.INFINITE
        translate.repeatMode = ValueAnimator.REVERSE

        translate.start()
    }

    companion object {
        private const val REQUEST_PERMISSION = 1
    }
}