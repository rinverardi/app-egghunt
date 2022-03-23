package app.egghunt.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.egghunt.R
import app.egghunt.databinding.ActivityOrganizerBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class OrganizerActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityOrganizerBinding
    private lateinit var map: GoogleMap

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        binding = ActivityOrganizerBinding.inflate(layoutInflater)

        setContentView(binding.root)

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
    }
}
