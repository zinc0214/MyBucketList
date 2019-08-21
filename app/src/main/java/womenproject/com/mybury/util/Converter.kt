package womenproject.com.mybury.util

import android.content.res.Resources


/**
 * Created by HanAYeon on 2018. 12. 4..
 */

class Converter {


    companion object {

        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().getDisplayMetrics().density).toInt()
        }


        fun stringFormat(origin : String, text1 :String): String {
            return String.format(origin, text1)
        }

        fun stringFormat(origin : String, text1 :String, text2 : String): String {
            return String.format(origin, text1, text2)
        }


    }
}