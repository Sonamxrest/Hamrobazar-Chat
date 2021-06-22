package com.xrest.hamrobazar

import android.content.pm.PackageManager

import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority

import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode



class MainActivity2 : AppCompatActivity(),OnMapReadyCallback,PermissionsListener,LocationEngineListener,MapboxMap.OnMapClickListener {
    private var mapView: MapView? = null
    private var map: MapboxMap? = null
    var locationEngine: LocationEngine? = null
    var locationLayerPlugin: LocationLayerPlugin? = null

    var permissionsManager: PermissionsManager? = null
    var originLayout: Location? = null
    private lateinit var originPoint: Point
    private lateinit var destinationPoint :Point
    lateinit var btn: Button



    private var dMarker: Marker?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        Mapbox.getInstance(this, getString(R.string.access_token));
        mapView = findViewById(R.id.mapView);
        mapView!!.onCreate(savedInstanceState);
        btn = findViewById(R.id.btn)
        btn.isEnabled =false
        mapView!!.getMapAsync(this);
    }



    private fun locationEnable() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            intialLocationEngine();
            intializLocationLayer();
        } else {
            permissionsManager =  PermissionsManager(this);
            permissionsManager!!.requestLocationPermissions(this);
        }
    }

    private fun intializLocationLayer() {
        locationLayerPlugin = LocationLayerPlugin(mapView!!, map!!, locationEngine)
        locationLayerPlugin!!.isLocationLayerEnabled = true
        locationLayerPlugin!!.cameraMode = CameraMode.TRACKING
        locationLayerPlugin!!.renderMode = RenderMode.NORMAL

    }

    @SuppressWarnings("MissingPermission")
    private fun intialLocationEngine() {
        locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()
        locationEngine!!.priority = LocationEnginePriority.HIGH_ACCURACY
        locationEngine!!.activate()

        val lastLocation = locationEngine!!.lastLocation
        if (lastLocation != null) {
            originLayout = lastLocation
            setCamerpostion(lastLocation)
        } else {
            locationEngine!!.addLocationEngineListener(this)
        }
    }

    private fun setCamerpostion(lastLocation: Location) {
        map!!.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    lastLocation.latitude,
                    lastLocation.longitude
                ), 13.0
            ),4000,null
        )

    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {

    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            locationEnable();
        }
    }

    override fun onConnected() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationEngine!!.requestLocationUpdates();
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            originLayout = location;
            setCamerpostion(location);
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        if (locationEngine != null)
            locationEngine!!.requestLocationUpdates();
        mapView!!.onStart();
    }

    override fun onRestart() {
        super.onRestart()
        mapView!!.onResume()
    }


    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }
    override fun onDestroy() {
        super.onDestroy()
        if(locationEngine != null) {
            locationEngine!!.deactivate()
        }
        else mapView!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onMapClick(point: LatLng) {
        if(dMarker!=null)
        {
            dMarker.let {
                map!!.removeMarker(it!!)
            }
        }
        dMarker = map!!.addMarker(MarkerOptions().position(point))
        destinationPoint = Point.fromLngLat(point.longitude,point.latitude)
        originPoint = Point.fromLngLat(originLayout!!.longitude,originLayout!!.longitude)
        btn.isEnabled =true
        btn.setBackgroundResource(R.color.black)

    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap;
        map?.addOnMapClickListener(this)

        locationEnable();
//        mapboxMap!!.getUiSettings().setZoomControlsEnabled(true);
//        mapboxMap!!.getUiSettings().setZoomGesturesEnabled(true);
//        mapboxMap!!.getUiSettings().setScrollGesturesEnabled(true);
//        mapboxMap!!.getUiSettings().setAllGesturesEnabled(true);

    }


}