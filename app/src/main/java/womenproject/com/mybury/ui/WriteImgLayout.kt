package womenproject.com.mybury.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import womenproject.com.mybury.R


@SuppressLint("ViewConstructor")
class WriteImgLayout internal constructor(context: Context, private var imgRemoveListener: (View) -> Unit): RelativeLayout(context) {

    fun setUI(uri: Uri): View {
        val view = LayoutInflater.from(context).inflate(R.layout.write_img_layout, this, false)

        val imgView = view.findViewById<ImageView>(R.id.write_img)
        imgView.setImageURI(uri)

        val imgRemove = view.findViewById<RelativeLayout>(R.id.img_remove_btn)
        imgRemove.setOnClickListener {
            run {
                imgRemoveListener.invoke(this)
            }
        }
        return view
    }
}