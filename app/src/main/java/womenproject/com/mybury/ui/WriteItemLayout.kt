package womenproject.com.mybury.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Category


class WriteItemLayout internal constructor(context: Context, private var categorySelectListener: (Category) -> Unit) : RelativeLayout(context) {

    fun setUI(category: Category): View {

        val view = LayoutInflater.from(context).inflate(R.layout.widget_write_fragment_add_item, this, false)

        val textView = view.findViewById<TextView>(R.id.write_item_text)
        textView.text = category.name

        val writeLayout = view.findViewById<LinearLayout>(R.id.write_item_layout)

        writeLayout.setOnClickListener {
            categorySelectListener.invoke(category)
        }

        return view
    }

}