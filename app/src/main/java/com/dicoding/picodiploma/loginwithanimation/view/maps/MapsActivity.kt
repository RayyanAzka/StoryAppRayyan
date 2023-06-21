package com.dicoding.picodiploma.loginwithanimation.view.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.domain.Result
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.Story
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMapsBinding
import com.dicoding.picodiploma.loginwithanimation.viewmodel.ViewModelFactory
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapsViewModel = ViewModelProvider(this, ViewModelFactory(this))[MapsViewModel::class.java]
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMapStory()
    }
    private fun getMapStory() {
        mapsViewModel.getStoryWithLocation().observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {}
                is Result.Success -> showMarker(result.data)
            }
        }
    }
    private val boundsBuilder = LatLngBounds.Builder()

    private fun showMarker(stories: List<ListStoryItem>) {
        stories.forEach { story ->
            val latLng = LatLng((story.lat) as Double, (story.lon) as Double)

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(story.name)
            )
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                30
            )
        )
    }
}