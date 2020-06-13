package womenproject.com.mybury.ui.snackbar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import womenproject.com.mybury.R
import womenproject.com.mybury.util.Converter

class MainSnackBarWidget(
    parent: ViewGroup,
    content: CustomSnackBarView
) : BaseTransientBottomBar<MainSnackBarWidget>(parent, content, content) {

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {

        fun make(
            view: View, title: String, count: String, listener: View.OnClickListener
        ): MainSnackBarWidget? {

            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            try {
                val customView = LayoutInflater.from(view.context).inflate(
                    R.layout.main_snack_view_widget, parent, false) as CustomSnackBarView
                customView.apply {
                    titleView.text = "\"${title}\" "
                    countView.text = "${count}회완료"
                    cancelView.setOnClickListener {
                        listener.onClick(view)
                        cancelView.isEnabled = false
                    }

                    val layoutParams = customView.layoutParams
                    if (layoutParams is MarginLayoutParams) {
                        layoutParams.bottomMargin = Converter.dpToPx(95)
                    }
                }

                return MainSnackBarWidget(parent, customView).setDuration(10000)
            } catch (e: Exception) {
                Log.e("exception", e.message)
            }
            return null
        }


        private fun View?.findSuitableParent(): ViewGroup? {
            var view = this
            var fallback: ViewGroup? = null
            do {
                if (view is CoordinatorLayout) {
                    // We've found a CoordinatorLayout, use it
                    return view
                } else if (view is FrameLayout) {
                    if (view.id == android.R.id.content) {
                        // If we've hit the decor content view, then we didn't find a CoL in the
                        // hierarchy, so use it.
                        return view
                    } else {
                        // It's not the content view but we'll use it as our fallback
                        fallback = view
                    }
                }

                if (view != null) {
                    // Else, we will loop and crawl up the view hierarchy and try to find a parent
                    val parent = view.parent
                    view = if (parent is View) parent else null
                }
            } while (view != null)

            // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
            return fallback
        }
    }

}