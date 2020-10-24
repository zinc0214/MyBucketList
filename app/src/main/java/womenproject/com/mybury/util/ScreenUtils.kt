package womenproject.com.mybury.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import womenproject.com.mybury.R

/**
 * Created by nbtk on 5/4/18.
 */
class ScreenUtils {
    companion object {

        fun getScreenWidth(context: Context?): Int {
            val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            return dm.widthPixels
        }

        fun dpToPx(context: Context?, value: Int): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), context?.resources?.displayMetrics).toInt()
        }

        fun setStatusBar(activity: Activity, colors: Int = R.color.colorPrimary) {

            val window = activity.window

            if (colors != android.R.color.transparent) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            if (colors == android.R.color.white)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = activity.resources.getColor(colors, null)
        }
    }


}