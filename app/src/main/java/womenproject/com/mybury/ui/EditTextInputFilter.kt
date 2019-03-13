package womenproject.com.mybury.ui

import android.text.Spanned
import android.text.InputFilter



/**
 * Created by HanAYeon on 2019. 3. 13..
 */


class EditTextInputFilter : InputFilter {

    private var mIntMin: Int = 0
    private var mIntMax: Int = 0

    constructor(minValue: Int, maxValue: Int) {
        this.mIntMin = minValue
        this.mIntMax = maxValue
    }

    constructor(minValue: String, maxValue: String) {
        this.mIntMin = Integer.parseInt(minValue)
        this.mIntMax = Integer.parseInt(maxValue)
    }

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        try {
            val input = Integer.parseInt(dest.toString() + source.toString())
            if (isInRange(mIntMin, mIntMax, input))
                return null
        } catch (nfe: NumberFormatException) {
        }

        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c >= a && c <= b else c >= b && c <= a
    }
}