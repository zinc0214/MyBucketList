package womenproject.com.mybury.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import womenproject.com.mybury.R


@SuppressLint("ViewConstructor")
class WriteImgLayout internal constructor(context: Context, private var imgRemoveListener: (View) -> Unit): RelativeLayout(context) {

    private lateinit var cardViewLayout : RelativeLayout
    fun setUI(uri: Uri): View {
        val view = LayoutInflater.from(context).inflate(R.layout.write_img_layout, this, false)

        val imgView = view.findViewById<ImageView>(R.id.write_img)
        imgView.setImageURI(uri)

        cardViewLayout = view.findViewById<RelativeLayout>(R.id.img_all_layout)

        val imgRemove = view.findViewById<RelativeLayout>(R.id.img_remove_btn)
        imgRemove.setOnClickListener {
            run {
                imgRemoveListener.invoke(this)
            }
        }

        imgRemove.setOnTouchListener(imgRemoveBtnOnTouchListener())
        return view
    }


    private fun imgRemoveBtnOnTouchListener(): View.OnTouchListener {

        return View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    cardViewLayout.foreground = context.getDrawable(R.drawable.img_remove_btn_press_background)

                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    cardViewLayout.foreground = context.getDrawable(R.drawable.tranperant_background)
                }
            }
            false
        }
    }
}