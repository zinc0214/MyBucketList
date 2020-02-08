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
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.img_wide_layout.view.*
import kotlinx.android.synthetic.main.write_img_layout.view.*
import womenproject.com.mybury.R


@SuppressLint("ViewConstructor")
class WriteImgLayout internal constructor(context: Context,
                                          private var id : String,
                                          private var imgRemoveListener: (String) -> Unit,
                                          private var imgClickListener: (Any) -> Unit): RelativeLayout(context) {

    private lateinit var cardViewLayout : RelativeLayout

    fun setUI(uri: Uri): View {
        val view = LayoutInflater.from(context).inflate(R.layout.write_img_layout, this, false)

        val imgView = view.findViewById<ImageView>(R.id.write_img)
        imgView.setImageURI(uri)

        cardViewLayout = view.findViewById<RelativeLayout>(R.id.img_all_layout)
        cardViewLayout.setOnClickListener {
            imgClickListener.invoke(uri)
        }

        val imgRemove = view.findViewById<RelativeLayout>(R.id.img_remove_btn)
        imgRemove.setOnClickListener {
            run {
                imgRemoveListener.invoke(id)
            }
        }

        imgRemove.setOnTouchListener(imgRemoveBtnOnTouchListener())
        return view
    }


    fun setAleadyUI(url : String): View {
        val view = LayoutInflater.from(context).inflate(R.layout.write_img_layout, this, false)

        val imgView = view.findViewById<ImageView>(R.id.write_img)
        Glide.with(view).load(url).centerCrop().placeholder(R.drawable.gradient_background).into(imgView)

        cardViewLayout = view.findViewById(R.id.img_all_layout)
        cardViewLayout.setOnClickListener {
            imgClickListener.invoke(url)
        }

        val imgRemove = view.findViewById<RelativeLayout>(R.id.img_remove_btn)
        imgRemove.setOnClickListener {
            run {
                imgRemoveListener.invoke(id)
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