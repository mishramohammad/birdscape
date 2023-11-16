package com.green.birdscapeopsc7312poe.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.gson.Gson
import com.green.birdscapeopsc7312poe.DashboardActivity
import com.green.birdscapeopsc7312poe.R
import com.green.birdscapeopsc7312poe.dataclass.BirdObservation
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class MapboxActivity : AppCompatActivity() {

    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private lateinit var mapView: MapView
    private var birdList: List<BirdObservation> = emptyList()

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapbox)
        mapView = findViewById(R.id.mapView)

        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }
        // Fetch bird observations from eBird API
        fetchBirdObservations()

        // Receive latitude and longitude from intent
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        // Display the received latitude and longitude on the map
        if (latitude != 0.0 && longitude != 0.0) {
            addMapIcon(latitude, longitude)
        }

        val zoomInButton = findViewById<Button>(R.id.zoomInButton)
        val zoomOutButton = findViewById<Button>(R.id.zoomOutButton)

        zoomInButton.setOnClickListener {
            // Zoom in when the "Zoom In" button is clicked
            zoomIn()
        }

        zoomOutButton.setOnClickListener {
            // Zoom out when the "Zoom Out" button is clicked
            zoomOut()
        }

        val btnDashBoard = findViewById<Button>(R.id.dashboardButton)

        btnDashBoard.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun zoomIn() {
        val currentZoom = mapView.getMapboxMap().cameraState.zoom
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(currentZoom + 1.0)
                .build()
        )
    }

    private fun zoomOut() {
        val currentZoom = mapView.getMapboxMap().cameraState.zoom
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(currentZoom - 1.0)
                .build()
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(0.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initLocationComponent()
            setupGesturesListener()
        }
    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    this@MapboxActivity,
                    R.drawable.mapbox_mylocation_icon_default,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }

    private fun onCameraTrackingDismissed() {
        Toast.makeText(this, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // BIRD API
    // Fetch bird observations from the eBird API
    private fun fetchBirdObservations() {
        Thread {
            try {
                val apiKey = "5ia6u8qs174j"
                val url = URL("https://api.ebird.org/v2/data/obs/ZA/recent")
                val connection = url.openConnection() as HttpURLConnection

                connection.setRequestProperty("x-ebirdapitoken", apiKey)

                if (connection.responseCode == 200) {
                    val inputSystem = connection.inputStream
                    val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                    val response = Gson().fromJson(inputStreamReader, Array<BirdObservation>::class.java)
                    birdList = response.toList()

                    runOnUiThread {
                        displayBirdsOnMap()
                        showToast("We fetched bird data from the server")
                    }

                    inputStreamReader.close()
                    inputSystem.close()
                } else {
                    runOnUiThread {
                        // Handle failed connection
                        showToast("Failed to fetch data from the server")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    // Handle other exceptions
                    showToast("An error occurred: ${e.message}")
                }
            }
        }.start()
    }

    // Display bird locations on the Mapbox map
    private fun displayBirdsOnMap() {
        for (bird in birdList) {
            addMapIcon(bird.lng, bird.lat)
        }
    }

    // This function is responsible for adding a map icon at a specific latitude and longitude position.
    private fun addMapIcon(latitude: Double, longitude: Double) {
        // Retrieving a Bitmap from a drawable resource to use as the map icon.
        // The red_marker is a drawable resource representing the map icon.
        bitmapFromDrawableRes(this@MapboxActivity, R.drawable.blue_marker)?.let {
            // Obtaining the annotation API from the MapView to manage annotations on the map.
            val annotationApi = mapView.annotations
            // Creating a point annotation manager for handling point annotations on the map.
            val pointAnnotationManager = annotationApi.createPointAnnotationManager(mapView)
            // Setting up options for the point annotation.
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(longitude, latitude)) // Setting the position of the annotation.
                .withIconImage(it) // Using the retrieved bitmap as the icon for the annotation.

            // Creating the point annotation using the options provided.
            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }

    // This function converts a drawable resource into a Bitmap.
    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int): Bitmap? {
        // Use the provided resourceId to get the Drawable
        val drawable = AppCompatResources.getDrawable(context, resourceId)
        return convertDrawableToBitmap(drawable)
    }

    // This function converts a Drawable into a Bitmap, allowing for the creation of a Bitmap from a drawable resource.
    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }

        if (sourceDrawable is BitmapDrawable) {
            return sourceDrawable.bitmap
        } else {
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }
}
