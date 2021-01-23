package womenproject.com.mybury.ui.snackbar

import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.ContentViewCallback
import womenproject.com.mybury.R

class CustomSnackBarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {

    lateinit var titleView: TextView
    lateinit var countView: TextView
    lateinit var cancelView: TextView
    lateinit var cardView: CardView

    init {
        View.inflate(context, R.layout.widget_main_snack_bar_view, this)
        clipToPadding = false
        this.titleView = findViewById(R.id.bucketTitle)
        this.countView = findViewById(R.id.bucketCount)
        this.cancelView = findViewById(R.id.cancelButton)
        this.cardView = findViewById(R.id.cardView)
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        val animatorSet = AnimatorSet().apply {
            interpolator = OvershootInterpolator()
            setDuration(1500)

        }
        animatorSet.start()
    }

    override fun animateContentOut(delay: Int, duration: Int) {
    }
}