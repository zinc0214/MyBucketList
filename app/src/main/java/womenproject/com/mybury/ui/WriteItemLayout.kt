package womenproject.com.mybury.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import womenproject.com.mybury.R


@SuppressLint("ViewConstructor")
class WriteItemLayout internal constructor(context: Context, private var categorySelectListener: (String) -> Unit) : RelativeLayout(context) {

    fun setUI(title: String): View {

        val view = LayoutInflater.from(context).inflate(R.layout.write_dialog_item, this, false)

        val textView = view.findViewById<TextView>(R.id.write_item_text)
        textView.setText(title)

        val writeLayout = view.findViewById<LinearLayout>(R.id.write_item_layout)


        writeLayout.setOnClickListener {
            categorySelectListener.invoke(textView.text.toString())

        }

        return view
    }


}