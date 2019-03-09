package com.mahmoud.coko

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.LocationManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue

fun Context.isLocationEnabled(): Boolean {
    val mLocationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

fun Context.isPermissionEnabled(mainfestPermission: String): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return true
    } else {
        return ContextCompat.checkSelfPermission(this, mainfestPermission) == PackageManager.PERMISSION_GRANTED
    }
}

fun AppCompatActivity.requestPermission(mainfestPermissions: Array<String>, requestCode: Int, preInfo: String? = null) {
    if (preInfo.isNullOrEmpty()) {
        ActivityCompat.requestPermissions(this, mainfestPermissions, requestCode)
    } else {
        val builder = AlertDialog.Builder(this)

        builder.setMessage(preInfo)

        builder.setNeutralButton("Ok") { dialog, which ->
            ActivityCompat.requestPermissions(this@requestPermission, mainfestPermissions, requestCode)
        }
        builder.setCancelable(false)
        builder.show()
    }
}



fun Context.getScaledBitmap(resId: Int,width:Float,height:Float): Bitmap {
    val widthdp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, resources.displayMetrics)
    val heightdp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, resources.displayMetrics)
    var bitmapdraw = getDrawable(resId) as BitmapDrawable
    var b = bitmapdraw.getBitmap()
    var scaled = Bitmap.createScaledBitmap(b, widthdp.toInt(), heightdp.toInt(), false)
    return scaled
}
