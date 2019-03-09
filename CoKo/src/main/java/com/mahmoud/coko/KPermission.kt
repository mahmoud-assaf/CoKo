package com.mahmoud.coko

import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log


//HOW TO USE
// call KPermission(Context).askForPermission(string,OnPermissionResultCallback)

class KPermission constructor(private val mActivity: AppCompatActivity) {
private var mHeadlessFrag:HeadlessFragment?=null

    init {
        Log.e("initialize",".......")
        val fm=mActivity.supportFragmentManager
        mHeadlessFrag = fm .findFragmentByTag(HeadlessFragment.TAG) as? HeadlessFragment

        if (mHeadlessFrag == null) {
            Log.e("null...","creating new one")
            mHeadlessFrag = HeadlessFragment.newInstance()
            fm.beginTransaction()
                .add(mHeadlessFrag!!, HeadlessFragment.TAG)
                .commitNow()
        }else{
            Log.e("Not null...","use existing")
        }
    }
    fun askForPermission(permission: String, onPermissionResult: OnPermissionResultCallback) {

        mHeadlessFrag?.askForPermission(permission, onPermissionResult)

    }


    class HeadlessFragment  constructor() : Fragment() {
        private var mContext: Context?=null
        companion object {
             val TAG="headlessFragment"
            fun newInstance(): HeadlessFragment {
                return HeadlessFragment()
            }
        }

        val REQUEST_PERMISSION_CODE = 233
        var onPermissionResultCallback: OnPermissionResultCallback? = null


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            retainInstance = true

        }



        override fun onAttach(mcontext: Context) {
            if(mcontext==null) Log.e("context","null") else  Log.e("context...","not null")
            super.onAttach(mcontext)
            mContext = mcontext
        }

        /*override fun onAttach(mactivity: Activity?) {
            if(mactivity==null) Log.e("context","null") else  Log.e("context...","not null")
            super.onAttach(activity)
            mContext = activity
        }*/
        /*companion object {

           fun newInstance(): HeadlessFragment {
               return HeadlessFragment()
           }
       }
   */


        fun askForPermission(permission: String, onPermissionResultCallback: OnPermissionResultCallback) {
            this.onPermissionResultCallback = onPermissionResultCallback
            //check for it first

            if (ActivityCompat.checkSelfPermission(mContext!!, permission) == PackageManager.PERMISSION_GRANTED) {
                //granted
                onPermissionResultCallback?.onPermissionResult(true)
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)) {
                //should show rationale
                //Show Information about why you need the permission
                val builder = AlertDialog.Builder(activity!!)
                builder.setTitle("Need Permission")
                builder.setMessage("This app needs  permission to function properly")
                builder.setPositiveButton("Grant", DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                    requestPermissions(arrayOf(permission), REQUEST_PERMISSION_CODE);

                })
                builder.setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                builder.show()
            } else {
                //never asked
                requestPermissions(arrayOf(permission), REQUEST_PERMISSION_CODE);
            }


        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            if (requestCode == REQUEST_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionResultCallback?.onPermissionResult(true)

            } else {
                onPermissionResultCallback?.onPermissionResult(false)
                // We were not granted permission this time, so don't try to show the contact picker
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }


    }


    interface OnPermissionResultCallback {
        fun onPermissionResult(isGranted: Boolean)
    }

}


