package app.egghunt.welcome

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
import app.egghunt.action.scan.ScanActivity
import app.egghunt.hunter.HunterActivity
import app.egghunt.lib.Code
import app.egghunt.lib.CodeParser
import app.egghunt.lib.Extras
import app.egghunt.lib.LocalData
import app.egghunt.lib.PopupDialog
import app.egghunt.organizer.OrganizerActivity
import kotlin.math.roundToLong

class WelcomeActivity : AppCompatActivity() {
    private val scanLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val codeString = result.data!!.getStringExtra(Intent.EXTRA_TEXT)!!

                val code = CodeParser.parse(codeString)

                when {
                    code.isEgg() -> onScanEgg()
                    code.isHunter() -> onScanHunter(code)
                    else -> onScanCompetition(code)
                }
            }
        }

    private fun autoLoginAsHunter(): Boolean {
        val hunter = LocalData.loadCurrentHunter(this)

        return if (hunter == null) {
            false
        } else {
            val intent = Intent(this, HunterActivity::class.java).apply {
                putExtra(Extras.COMPETITION_DESCRIPTION, hunter[0])
                putExtra(Extras.COMPETITION_TAG, hunter[1])
                putExtra(Extras.HUNTER_DESCRIPTION, hunter[2])
                putExtra(Extras.HUNTER_TAG, hunter[3])
            }

            startActivity(intent)
            true
        }
    }

    private fun autoLoginAsOrganizer(): Boolean {
        val organizer = LocalData.loadCurrentOrganizer(this)

        return if (organizer == null) {
            false
        } else {
            val intent = Intent(this, OrganizerActivity::class.java).apply {
                putExtra(Extras.COMPETITION_DESCRIPTION, organizer[0])
                putExtra(Extras.COMPETITION_TAG, organizer[1])
            }

            startActivity(intent)
            true
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

        if (autoLoginAsHunter() || autoLoginAsOrganizer()) {
            finish()
            return
        }

        // Initialize the button.

        findViewById<View>(R.id.button).setOnClickListener(this::onClickButton)

        // Initialize the eggs.

        val eggs = arrayOf<ImageView>(
            findViewById(R.id.egg0),
            findViewById(R.id.egg1),
            findViewById(R.id.egg2),
            findViewById(R.id.egg3)
        )

        eggs.forEach {
            startRotation(it)
            startTranslation(it)
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
        val intent = Intent(this, OrganizerActivity::class.java).apply {
            putExtra(Extras.COMPETITION_DESCRIPTION, code.cd)
            putExtra(Extras.COMPETITION_TAG, code.ct)
        }

        startActivity(intent)
        finish()
    }

    private fun onScanEgg() {
        val dialog = PopupDialog(
            null,
            R.string.error_unexpected_egg,
            R.string.exclamation_oops
        )

        dialog.show(supportFragmentManager, null)
    }

    private fun onScanHunter(code: Code) {
        val intent = Intent(this, HunterActivity::class.java).apply {
            putExtra(Extras.COMPETITION_DESCRIPTION, code.cd)
            putExtra(Extras.COMPETITION_TAG, code.ct)
            putExtra(Extras.HUNTER_DESCRIPTION, code.hd)
            putExtra(Extras.HUNTER_TAG, code.ht)
        }

        startActivity(intent)
        finish()
    }

    private fun scan() {
        val intent = Intent(this, ScanActivity::class.java)

        scanLauncher.launch(intent)
    }

    private fun startRotation(egg: View) =
        with(ObjectAnimator.ofFloat(egg, View.ROTATION, -20f, 20f)) {
            duration = (5000 + Math.random() * 1000).roundToLong()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE

            start()
        }

    private fun startTranslation(egg: View) =
        with(ObjectAnimator.ofFloat(egg, View.TRANSLATION_Y, -300f, 0f)) {
            duration = (2000 + Math.random() * 1000).roundToLong()
            interpolator = BounceInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE

            start()
        }

    companion object {
        private const val REQUEST_PERMISSION = 1
    }
}