# CoKO

Small lib of **Co**mmon **Ko**tlin utilities for handling Location , Permissions ,Net Connectivity ,Toasts and some handy functions .

##  Motivation

CoKo was built as learning purposes and engaging in open source .

## Usage
- Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
- Add the dependency
```
dependencies {
	        implementation 'com.github.mahmoud-assaf:CoKo:0.2'
	}
```

### Lokation
```
val lokation = Lokation(this)  
lokation.getLocationOnce(object : Lokation.OnLokationResultCallback {  
    override fun onLokationResult(result: Lokation.LokationResult) {  
        if (result.isSuccessfull) {  
            Log.e("Location", result.location.toString())  
        } else {  
            Log.e("error",result.errorCode.toString())  // LOCATION_SERVICE_DISABLED=-1  ,LOCATION_PERMISSION_NOT_GRANTED=-2  
  }  
    }  
})
// same for getting updates
lokation.getLocationUpdates(object:Lokation.OnLokationResultCallback {  
    override fun onLokationResult(result: Lokation.LokationResult) {  
        if (result.isSuccessfull) {  
            Log.e("Location", result.location.toString())  
        } else {  
            Log.e("error",result.errorCode.toString())  // LOCATION_SERVICE_DISABLED=-1  ,LOCATION_PERMISSION_NOT_GRANTED=-2  
  }  
    }  
})
//dont forget
lokation.stopUpdates()
```

### KPermission
```
KPermission(this).askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,object : KPermission.OnPermissionResultCallback{  
    override fun onPermissionResult(isGranted: Boolean) {  
        if (isGranted){  
            //granted  
            }else{  
            //denied  
            }  
    }  
})
```
### NetState
```
//better initialize in Application class
//must be called first
NetState.initialize(this)
NetState.isOnline():Boolean
NetState.getLiveConnectivity():LiveData<Boolean>
```
### Toasty
- implemented as extensions to Context
```
toast("message")  
toastError("error")  
toastWarning("warning")  
toastInfo("info ")  
toastSuccess("success")
```

![toasts](https://raw.githubusercontent.com/mahmoud-assaf/CoKo/master/Untitled.png)

### Utils & Extentions
-Utils
```
getRandomString(length:Int): String
convertDpToPixel(dp: Float): Float

```
- Extentions
```
Context.isLocationEnabled(): Boolean
Context.isPermissionEnabled(mainfestPermission: String): Boolean
Context.getScaledBitmap(resId: Int,width:Float,height:Float): Bitmap
```
##  License
[Apache 2](https://www.apache.org/licenses/LICENSE-2.0)


