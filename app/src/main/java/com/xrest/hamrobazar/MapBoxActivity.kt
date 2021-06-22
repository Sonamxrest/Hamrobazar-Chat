//package com.xrest.hamrobazar
//
//import android.content.pm.PackageManager
//import android.location.Location
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import com.mapbox.android.core.location.LocationEngine
//import com.mapbox.android.core.location.LocationEngineListener
//import com.mapbox.android.core.location.LocationEnginePriority
//import com.mapbox.android.core.location.LocationEngineProvider
//import com.mapbox.android.core.permissions.PermissionsListener
//import com.mapbox.mapboxsdk.Mapbox
//import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
//import com.mapbox.mapboxsdk.geometry.LatLng
//
//import com.mapbox.mapboxsdk.maps.MapView
//import com.mapbox.mapboxsdk.maps.MapboxMap
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
//import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
//import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
//import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
//
//
//class MapBoxActivity : AppCompatActivity(),PermissionsListener,LocationEngineListener,OnMapReadyCallback {
//    private lateinit var mapBox:MapView
//    private lateinit var map:MapboxMap
//
//    private lateinit var clocation: Location
//    private  var locationEngine: LocationEngine?=null
//    private var locationLayerPlugin:LocationLayerPlugin?=null
//
//
//
//    val permission = arrayOf(
//        android.Manifest.permission.READ_PHONE_STATE,
//        android.Manifest.permission.ACCESS_FINE_LOCATION
//    )
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_map_box)
//        Mapbox.getInstance(applicationContext, getString(R.string.access_token))
//        for(p in permission)
//        {
//            if(ActivityCompat.checkSelfPermission(this, p)!=PackageManager.PERMISSION_GRANTED)
//            {
//                ActivityCompat.requestPermissions(this, permission, 1)
//            }
//        }
//        mapBox = findViewById(R.id.mapbox)
//        mapBox.onCreate(savedInstanceState)
//        mapBox.getMapAsync(){ mapBox->
//            map = mapBox
//            enableLocation()
//
//        }
//
//    }
//
//
//    override fun  onStart(){
//        super.onStart()
//        mapBox.onStart()
//    }
//    override fun onResume(){
//        super.onResume()
//mapBox.onResume()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mapBox.onDestroy()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        mapBox.onPause()
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        if(outState!=null)
//        {
//            mapBox.onSaveInstanceState(outState)
//        }
//    }
//
//    fun enableLocation(){
//
//    inititalizeLocationEngiene()
//    inititalizeLocationLayer()
//    }
//
//    @SuppressWarnings("MissingPermission")
//    private fun inititalizeLocationLayer() {
//        locationLayerPlugin = LocationLayerPlugin(mapBox, map, locationEngine)
//        // it is responsible for tracking the location
//        locationLayerPlugin!!.setLocationLayerEnabled(true)
//        locationLayerPlugin!!.cameraMode =CameraMode.TRACKING
//        locationLayerPlugin!!.renderMode =RenderMode.NORMAL
//
//    }
//
//    fun setCameraLocation(location: Location){
//
//
//        map.animateCamera(
//            CameraUpdateFactory.newLatLngZoom(
//                LatLng(
//                    location!!.latitude,
//                    location.longitude
//                ), 14.0
//            ), 2000,null
//        )
//
//
//    }
//
//    @SuppressWarnings("MissingPermission")
//    private fun inititalizeLocationEngiene() {
//
//        locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()
//        locationEngine!!.priority =LocationEnginePriority.HIGH_ACCURACY
//        locationEngine!!.activate()
//        if(locationEngine!!.lastLocation!=null)
//        {
//            clocation =locationEngine!!.lastLocation
//            setCameraLocation(clocation)
//        }
//
//    }
//
//    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
//
//    }
//
//
//    override fun onPermissionResult(granted: Boolean) {
//if(granted)
//{
//    enableLocation()
//}
//
//    }
//
//
//    @SuppressWarnings("MissingPermission")
//    override fun onConnected() {
//locationEngine!!.requestLocationUpdates()
//    }
//
//    override fun onLocationChanged(location: Location?) {
//
//        location!!.let {
//            clocation =location!!
//            setCameraLocation(location)
//        }
//
//    }
//
//    override fun onMapReady(mapboxMap: MapboxMap?) {
//
//        map = mapboxMap!!
//
//
//
//    }
//}