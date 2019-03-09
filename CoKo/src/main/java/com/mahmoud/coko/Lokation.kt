package com.mahmoud.coko

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*


//HOW TO USE
//DEPENDENCY
//com.google.android.gms:play-services-location:$VERSION
//Lokation(Context).getLocationOnce(OnLokationResultCallback)
//Lokation(Context).getLocationUpdates(OnLokationResultCallback)

class Lokation constructor(private val mcontext: Context) {

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mcontext)
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var locationUpdateState = false

    val LOCATION_SERVICE_DISABLED=-1
 val LOCATION_PERMISSION_NOT_GRANTED=-2

    @SuppressLint("MissingPermission")
    fun getLocationOnce(onLokationResultCallback: OnLokationResultCallback) {
        var result=LokationResult()

        if (!isLocationEnabled()){
            result.isSuccessfull=false
            result.errorCode=LOCATION_SERVICE_DISABLED
            onLokationResultCallback.onLokationResult(result)
            return
        }

        if (!isLocationPermissionEnabled()){
            result.isSuccessfull=false
            result.errorCode=LOCATION_PERMISSION_NOT_GRANTED
            onLokationResultCallback.onLokationResult(result)
            return
        }

        //first try with fused location if not use updates
        fusedLocationClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                result.isSuccessfull=true
                result.location=it.result
                onLokationResultCallback.onLokationResult(result)

            } else {
                //null so try from updates
                _getLocationUpdates(onLokationResultCallback, false)
            }
        }
    }

    fun getLocationUpdates(onLokationResultCallback: OnLokationResultCallback){
        _getLocationUpdates(onLokationResultCallback, true)
    }


    @SuppressLint("MissingPermission")
  private  fun _getLocationUpdates(onLokationResultCallback: OnLokationResultCallback, continious: Boolean = true) {
        var result=LokationResult()


        locationUpdateState = true
        createLocationRequest()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                for (location: Location in p0.locations) {
                    if (location!=null) {
                        result.isSuccessfull = true
                        result.location = location
                        onLokationResultCallback.onLokationResult(result)
                    }
                    if (!continious) {
                        stopUpdates()
                    }
                }
                //onLocationFixCallBack.onLocationFix(p0.lastLocation)
            }
        }
        //null so try from updates
        startLocationUpdates()
    }

    fun stopUpdates() {
        if (locationUpdateState && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            locationUpdateState = false
        }
    }

    private fun createLocationRequest() {
        // 1
        locationRequest = LocationRequest()
        // 2
        locationRequest?.interval = 5000
        // 3
        locationRequest?.fastestInterval = 2000
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
/*        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        // 4
        val client = LocationServices.getSettingsClient(mcontext)
        val task = client.checkLocationSettings(builder.build())
        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                  //  e.startResolutionForResult(mcontext as Activity,
                   //     REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }*/
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

   private fun isLocationEnabled(): Boolean {
        val mLocationManager: LocationManager = mcontext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

  private  fun isLocationPermissionEnabled(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        } else {
            return ContextCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }


    interface OnLokationResultCallback {
        fun onLokationResult(result: LokationResult)
    }

  class LokationResult{
    var isSuccessfull:Boolean=false
     var errorCode:Int?=null
     var location:Location?=null
  }



}