package womenproject.com.mybury.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import womenproject.com.mybury.util.Converter.Companion.dpToPx


/* set spacing for grid view */
class SupportItemDecoration : ItemDecoration() {

    private val spanCount = 2
    private val spacingLeft = dpToPx(12)
    private val spacingTop = dpToPx(12)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item phases_position
        val column = position % 2 // item column
       // outRect.left = spacingLeft - column * spacingLeft / spanCount // spacing - column * ((1f / spanCount) * spacing)
       // outRect.right = (column + 1) * spacingLeft / spanCount // (column + 1) * ((1f / spanCount) * spacing)
        if (position < spanCount) { // top edge
            outRect.top = spacingTop
        }
        outRect.bottom = spacingTop // item bottom
    }

}