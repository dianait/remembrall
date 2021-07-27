package com.dianait.myapplication

import Reminder
import Location as firebaseLocation
import android.location.Location
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.GeoPoint
import services.firestore.FirestoreService


class FormReminder : AppCompatActivity() {
    private lateinit var db: FirestoreService
    // private var lat by Delegates.notNull<Double>()
    // private var lng by Delegates.notNull<Double>()
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var PERMISSION_ID = 44
    private var chipSelected = "PERSONAL"

    override fun onCreate(savedInstanceState: Bundle?) {
        db = FirestoreService()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_reminder)
        val inputTextReminder = findViewById<TextInputEditText>(R.id.input_reminder)
        val inputLatitude = findViewById<TextInputEditText>(R.id.input_latitude_text)
        val inputLongitude = findViewById<TextInputEditText>(R.id.input_longitude_text)
        val inputNameLocation = findViewById<TextInputEditText>(R.id.input_name_location_text)


        var btnCurrentLocation = findViewById<Button>(R.id.btn_current_location)
        val categoryChips = findViewById<ChipGroup>(R.id.chipGroup)
        categoryChips.layoutDirection = View.LAYOUT_DIRECTION_LOCALE
        categoryChips.isSingleSelection = true
        categoryChips.setOnCheckedChangeListener { group, checkedId ->

            var chipText: String = ""
            chipText = if (checkedId == -1) {
                "No chip selected"
            } else {
                val chip: Chip = group.findViewById(checkedId)
                chip.text as String
            }
            Log.d("CHIPS", chipText)
            chipSelected = chipText
        }

        btnCurrentLocation.setOnClickListener {

            var geoPoint: GeoPoint = getLastLocation()
            inputLatitude.setText(geoPoint.latitude.toString())
            inputLongitude.setText(geoPoint.longitude.toString())

        }

        val btnAddReminder = findViewById<FloatingActionButton>(R.id.btn_set_reminder)
        btnAddReminder.setOnClickListener {

            var reminder = Reminder(id = "",
                text = inputTextReminder.text.toString(),
                emoji = chipSelected,
                location = GeoPoint(inputLatitude.text.toString().toDouble(),
                    inputLongitude.text.toString().toDouble()),
                name = inputNameLocation.text.toString())

            db.addLocation(firebaseLocation("", reminder.name, reminder.location.latitude, reminder.location.longitude))
            db.addReminder(reminder)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(): GeoPoint {

        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        lat = location.latitude
                        lng = location.longitude
                        Log.d("debugInit", "$lat - $lng")

                    }
                }
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }

        return GeoPoint(lat, lng)
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient?.requestLocationUpdates(mLocationRequest,
            mLocationCallback,
            Looper.myLooper())
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            var lat = mLastLocation.latitude.toString()
            var lng = mLastLocation.longitude.toString()
            Log.d("debugInit", "$lat - $lng")
        }
    }

    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
    }

    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            getLastLocation()
        }
    }
}




