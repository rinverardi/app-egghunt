package app.egghunt.action.hide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.egghunt.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HideActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        setContentView(R.layout.activity_hide)

        // Initialize the map.

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    // @remo Fix me!

    override fun onMapReady(map: GoogleMap) {
        this.map = map

        val position = LatLng(46.318457, 7.9923558)

        this.map.addMarker(MarkerOptions().position(position))
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
    }
}
