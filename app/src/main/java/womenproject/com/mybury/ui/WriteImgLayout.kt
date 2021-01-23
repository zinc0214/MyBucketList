package womenproject.com.mybury.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import womenproject.com.mybury.R


@SuppressLint("ViewConstructor")
class WriteImgLayout internal constructor(context: Context,
                                          private var id : String,
                                          private var imgRemoveListener: (String) -> Unit,
                                          private var imgClickListener: (Any) -> Unit): RelativeLayout(context) {

    private lateinit var cardViewLayout : RelativeLayout

    fun setUI(uri: Uri): View {
        val view = LayoutInflater.from(context).inflate(R.layout.widget_write_add_img, this, false)

        val imgView = view.findViewById<ImageView>(R.id.write_img)
      //  imgView.setImageURI(uri)

        Glide.with(context)
                .load(uri)
                .override(200, 200)
                .into(imgView)

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
        val view = LayoutInflater.from(context).inflate(R.layout.widget_write_add_img, this, false)

        val imgView = view.findViewById<ImageView>(R.id.write_img)
        Glide.with(view).load(url).centerCrop().placeholder(R.drawable.shape_gradient).into(imgView)

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

    @SuppressLint("ClickableViewAccessibility")
    private fun imgRemoveBtnOnTouchListener() = OnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                cardViewLayout.foreground = context.getDrawable(R.drawable.shape_33ffffff_r4)

            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                cardViewLayout.foreground = context.getDrawable(R.drawable.shape_00ffffff_r4)
            }
            else -> {
                // Do Nothing
            }
        }
            false
        }
}