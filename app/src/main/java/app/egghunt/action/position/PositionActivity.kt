package app.egghunt.action.position

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.egghunt.R
import app.egghunt.lib.Extras
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PositionActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback {
    private lateinit var buttonAccept: FloatingActionButton
    private lateinit var buttonLock: FloatingActionButton
    private var location: Location? = null
    private lateinit var locationClient: FusedLocationProviderClient
    private var locked: Boolean = false
    private var map: GoogleMap? = null

    private fun accept() {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(Extras.EGG_DESCRIPTION, intent.getStringExtra(Extras.EGG_DESCRIPTION))
            putExtra(Extras.EGG_TAG, intent.getStringExtra(Extras.EGG_TAG))

            Extras.setPosition(this, map?.cameraPosition?.target)
        })

        finish()
    }

    private fun autoLock() {
        val position = Extras.getPosition(intent)

        if (position == null) {
            lock()
        } else {
            unlock()
        }
    }

    private fun lock() {
        with(buttonLock) {
            setImageResource(R.drawable.ic_location_locked)

            visibility = View.VISIBLE
        }

        location?.let { location ->
            val position = LatLng(location.latitude, location.longitude)

            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM))
        }

        locked = true
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        setContentView(R.layout.activity_position)

        // Initialize the action bar.

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Initialize the buttons.

        buttonAccept = findViewById<FloatingActionButton>(R.id.button_accept).apply {
            setOnClickListener {
                accept()
            }
        }

        buttonLock = findViewById<FloatingActionButton>(R.id.button_lock).apply {
            setOnClickListener {
                lock()
            }
        }

        // Initialize the map.

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        // Set up the location.

        locationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissions()
    }

    override fun onLocationChanged(location: Location) {
        if (locked) {
            val position = LatLng(location.latitude, location.longitude)

            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM))
        }

        this.location = location
    }

    override fun onMapReady(map: GoogleMap) {

        // Update the button.

        buttonAccept.visibility = View.VISIBLE

        // Update the map.

        map.mapType = GoogleMap.MAP_TYPE_SATELLITE

        val position = Extras.getPosition(intent) ?: LatLng(
            location?.latitude ?: FALLBACK_LATITUDE,
            location?.longitude ?: FALLBACK_LONGITUDE
        )

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM))

        map.setOnCameraMoveStartedListener { reason ->
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                unlock()
            }
        }

        this.map = map
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
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
                    .makeText(this, R.string.toast_location_access_granted, Toast.LENGTH_SHORT)
                    .show()

                autoLock()
                requestLocation()
            } else {
                Toast
                    .makeText(this, R.string.toast_location_access_denied, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        try {
            locationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    onLocationChanged(it)
                }

                requestLocationUpdates()
            }
        } catch (exception: SecurityException) {
            Log.wtf(javaClass.simpleName, exception)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        try {
            val locationRequest = LocationRequest.create().apply {
                fastestInterval = 2 * 1000
                interval = 4 * 1000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    onLocationChanged(result.lastLocation)
                }
            }

            locationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (exception: SecurityException) {
            Log.wtf(javaClass.simpleName, exception)
        }
    }

    private fun requestPermissions() {
        val status = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (status == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_PERMISSION
            )
        } else {
            autoLock()
            requestLocation()
        }
    }

    private fun unlock() {
        with(buttonLock) {
            setImageResource(R.drawable.ic_location_unlocked)

            visibility = View.VISIBLE
        }

        locked = false
    }

    companion object {
        private const val FALLBACK_LATITUDE = 47.37984
        private const val FALLBACK_LONGITUDE = 8.5351137

        private const val REQUEST_PERMISSION = 1

        private const val ZOOM = 20f
    }
}
