package womenproject.com.mybury.ui

import android.widget.ArrayAdapter
import android.R
import android.content.Context
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.TextView
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import java.util.jar.Attributes


/**
 * Created by HanAYeon on 2019. 4. 26..
 */

class MyPageNickNameBehavior(context : Context, attributesSet: AttributeSet) : CoordinatorLayout.Behavior<View>(context, attributesSet) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is ImageView
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {

        if (child is TextView && dependency is ListView) {

            val tv = child as TextView
            val lv = dependency as ListView
            val vp = lv.getFirstVisiblePosition()

            if (lv.getFirstVisiblePosition() >= 3) {
                tv.textSize = 20f
            } else if (lv.getFirstVisiblePosition() < 3) {
                tv.textSize = 80f

            }
        }

        return false
    }
}
/*
class MyPageNickNameBehavior : CoordinatorLayout.Behavior<>() {



    fun layoutDependsOn(parent: CoordinatorLayout,
                        child: View,
                        dependency: View): Boolean {
        return dependency is ListView
    }

    fun onDependentViewChanged(parent: CoordinatorLayout,
                               child: View,
                               dependency: View): Boolean {
        if (child is TextView && dependency is ListView) {

            val tv = child as TextView
            val lv = dependency as ListView
            val vp = lv.getFirstVisiblePosition()

            if (lv.getFirstVisiblePosition() >= 3) {
                tv.textSize = 20f
            } else if (lv.getFirstVisiblePosition() < 3) {
                tv.textSize = 80f

            }
        }

        return false
    }

    companion object {
        private val TEXT_SIZE = 20f
    }
}
*/
