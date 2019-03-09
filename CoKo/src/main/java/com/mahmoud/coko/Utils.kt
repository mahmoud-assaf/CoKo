package com.mahmoud.coko

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import java.util.*



class Toasty constructor(private val mcontext:Context){

    fun toast(message: String){  //default toast
            show(message,0)
    }

    fun info(message: String){  //blue info toast
        show(message,1)
    }

    fun warning(message: String){  //orange warning toast
        show(message,2)
    }


    fun success(message: String){  //green success toast
        show(message,3)
    }

    fun error(message: String){  //red error toast
        show(message,4)
    }





  private  fun show(message:String,resID:Int=0) {
        val li =LayoutInflater.from(mcontext) as LayoutInflater
        //Getting the View object as defined in the customtoast.xml file
      //  val viewGroup=(mcontext as AppCompatActivity).findViewById<ViewGroup>(R.id.custom_taost_container)
      val resId=when(resID){
          0->R.layout.custom_toast
          1->R.layout.custom_toast_info
          2->R.layout.custom_toast_warning
          3->R.layout.custom_toast_success
          4->R.layout.custom_toast_error
          else->R.layout.custom_toast
      }
        val layout = li.inflate (resId,null);
        val txt=layout.findViewById<TextView>(R.id.custom_toast_text)
        txt.text=message
        //Creating the Toast object
        val toast =  Toast(mcontext);
        toast.duration = Toast.LENGTH_LONG
        toast.setMargin(50f,0f)
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, convertDpToPixel(60f).toInt())

        toast.view = layout //setting the view of custom toast layout
        toast.show()



    }




}


//Extensions for using Toasty
fun Context.toast(message:String){
    Toasty(this).toast(message)
}

fun Context.toastInfo(message:String){
    Toasty(this).info(message)
}

fun Context.toastWarning(message:String){
    Toasty(this).warning(message)
}

fun Context.toastError(message:String){
    Toasty(this).error(message)
}

fun Context.toastSuccess(message:String){
    Toasty(this).success(message)
}

fun getRandomString(length:Int): String {
    val chars1 = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray()
    val sb1 = StringBuilder()
    val random1 = Random()
    for (i in 0..length) {
        val c1 = chars1[random1.nextInt(chars1.size)]
        sb1.append(c1)
    }
    return sb1.toString()
}


fun convertDpToPixel(dp: Float): Float {
    val metrics = Resources.getSystem().getDisplayMetrics()
    val px = dp * (metrics.densityDpi / 160f)
    return Math.round(px).toFloat()
}

