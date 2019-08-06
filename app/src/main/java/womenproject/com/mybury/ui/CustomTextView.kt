package womenproject.com.mybury.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_base_dialog.view.*
import womenproject.com.mybury.R


class CustomTextView : TextView {
    private var mAvailableWidth = 0
    private var mPaint: Paint? = null
    private val mCutStr = ArrayList<String>()

    private val scale = resources.displayMetrics.scaledDensity
    val lineSpacingExtraSize = resources.getDimensionPixelSize(R.dimen.lineSpacingExtra) / scale

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    private fun setTextInfo(text: String, textWidth: Int, textHeight: Int): Int {
        var text = text
        // 그릴 페인트 세팅
        mPaint = paint

        var mTextHeight = textHeight

        if (textWidth > 0) {
            // 값 세팅
            mAvailableWidth = textWidth - this.paddingLeft - this.paddingRight

            mCutStr.clear()
            var end = 0
            do {
                // 글자가 width 보다 넘어가는지 체크
                end = mPaint!!.breakText(text, true, mAvailableWidth.toFloat(), null)
                if (end > 0) {
                    // 자른 문자열을 문자열 배열에 담아 놓는다.
                    mCutStr.add(text.substring(0, end))
                    // 넘어간 글자 모두 잘라 다음에 사용하도록 세팅
                    text = text.substring(end)
                    // 다음라인 높이 지정
                    if (textHeight == 0) mTextHeight += (lineHeight + lineSpacingExtraSize).toInt()
                }
            } while (end > 0)
        }
        return mTextHeight
    }

    protected override fun onDraw(canvas: Canvas) {
        // 글자 높이 지정
        var height = (paddingTop + lineHeight).toFloat()
        for (text in mCutStr) {
            // 캔버스에 라인 높이 만큰 글자 그리기
            canvas.drawText(text, paddingLeft.toFloat(), height, mPaint)
            height += lineHeight.toFloat()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        var parentHeight = 0
        val height = setTextInfo(this.text.toString(), parentWidth, parentHeight)
        Log.e("ayhan", "width : $parentWidth, height : $parentHeight")
        // 부모 높이가 0인경우 실제 그려줄 높이만큼 사이즈를 놀려줌...
        if (parentHeight == 0)
            parentHeight = height
        this.setMeasuredDimension(parentWidth, parentHeight)
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, after: Int) {
        // 글자가 변경되었을때 다시 세팅
        setTextInfo(text.toString(), this.width, this.height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // 사이즈가 변경되었을때 다시 세팅(가로 사이즈만...)
        if (w != oldw) {
            setTextInfo(this.text.toString(), w, h)
        }
    }
}