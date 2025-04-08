package com.example.practicageolocalizacion

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.practicageolocalizacion.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var polyline: Polyline? = null
    private val loadedLocations: ArrayList<LatLng> = arrayListOf()
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000
    )
        .setMinUpdateIntervalMillis(5000)
        .build()
    private var currentMarkerCount: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupEventListeners()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                onLocationCallbackResult(locationResult)
            }
        }
    }

    private fun onLocationCallbackResult(locationResult: LocationResult) {
        for (location in locationResult.locations) {
            val theLocation = LatLng(location.latitude, location.longitude)
            mMap.addMarker(
                MarkerOptions().position(
                    theLocation
                ).title("Marcador $currentMarkerCount")
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLng(theLocation))
            currentMarkerCount++
            loadedLocations.add(theLocation)
            refreshPolyline()
        }
    }

    private fun refreshPolyline() {
        if (polyline != null) {
            polyline!!.remove()
        }
        polyline = mMap.addPolyline(PolylineOptions().apply {
            color(Color.RED)
            width(2f)
            add(
                *loadedLocations.toTypedArray()
            )
        })

    }

    private fun setupEventListeners() {
        binding.btnShowLocation.setOnClickListener {
            callLastLocation()
        }
        binding.btnShowCurrentLocation.setOnClickListener {
            callCurrentLocation()
        }
        binding.btnRequestLocationUpdates.setOnClickListener {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CONTINUOUS_LOCATION
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun callCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CURRENT_LOCATION
            )

            return
        }
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location: Location? ->
            val theLocation = LatLng(location!!.latitude, location.longitude)
            mMap.addMarker(
                MarkerOptions().position(
                    theLocation
                ).title("Tu ubicación")
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLng(theLocation))
        }.addOnFailureListener {
            Toast.makeText(this, "Error al obtener la ubicación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun callLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_BUTTON_LOCATION
            )

            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val theLocation = LatLng(location!!.latitude, location.longitude)
                mMap.addMarker(
                    MarkerOptions().position(
                        theLocation
                    ).title("Tu ubicación")
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLng(theLocation))
            }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_MAP_LOCATION
            )

            return
        }
        mMap.isMyLocationEnabled = true
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_MAP_LOCATION && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else if (requestCode == REQUEST_BUTTON_LOCATION && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            callLastLocation()
        } else if (
            requestCode == REQUEST_CURRENT_LOCATION && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            callCurrentLocation()
        } else if (
            requestCode == REQUEST_CONTINUOUS_LOCATION && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
        } else {
            Toast.makeText(
                this,
                "La app necesita permisos de ubicación para funcionar",
                Toast.LENGTH_SHORT
            ).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val REQUEST_MAP_LOCATION = 1
        private const val REQUEST_BUTTON_LOCATION = 2
        private const val REQUEST_CURRENT_LOCATION = 3
        private const val REQUEST_CONTINUOUS_LOCATION = 4
    }
}