package app.egghunt

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

class ScanActivity : AppCompatActivity() {
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private lateinit var surfaceView: SurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scan)

        surfaceView = findViewById(R.id.surfaceView)
    }

    override fun onPause() {
        super.onPause()

        cameraSource.release()
    }

    override fun onResume() {
        super.onResume()

        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(1920, 1080)
            .build()

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceCreated(holder: SurfaceHolder) {
                val status = ActivityCompat.checkSelfPermission(this@ScanActivity, Manifest.permission.CAMERA)

                if (status == PackageManager.PERMISSION_GRANTED) {
                    cameraSource.start(surfaceView.holder)
                } else {
                    ActivityCompat.requestPermissions(
                        this@ScanActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_CAMERA_PERMISSION)
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode?> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode?>) {
                val barcodes = detections.detectedItems

                if (barcodes.size() != 0) {
                    surfaceView.post {
                        val result = Intent()

                        result.putExtra("code", barcodes.valueAt(0)!!.rawValue)

                        setResult(Activity.RESULT_OK, result)
                        finish()
                    }
                }
            }
        })
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 201
    }
}
