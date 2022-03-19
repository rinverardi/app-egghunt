package app.egghunt.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.egghunt.R
import app.egghunt.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var mapBinding: ActivityMapBinding

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        mapBinding = ActivityMapBinding.inflate(layoutInflater)

        setContentView(mapBinding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map

        val ffhs = LatLng(46.318457, 7.9923558)

        this.map.addMarker(MarkerOptions().position(ffhs))
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(ffhs, 15f))
    }

    companion object {
        const val EXTRA_COMPETITION_DESCRIPTION = "competition_description"
        const val EXTRA_COMPETITION_TAG = "competition_tag"
        const val EXTRA_HUNTER_DESCRIPTION = "hunter_description"
        const val EXTRA_HUNTER_TAG = "hunter_tag"
    }
}
