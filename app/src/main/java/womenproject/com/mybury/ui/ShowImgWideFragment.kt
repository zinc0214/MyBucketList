package womenproject.com.mybury.ui

import android.app.ActionBar
import android.net.Uri
import android.util.Log
import womenproject.com.mybury.R
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.databinding.ImgWideLayoutBinding

/**
 * Created by HanAYeon on 2019-05-14.
 */

class ShowImgWideFragment(private val uri: Uri) : BaseDialogFragment<ImgWideLayoutBinding>() {

    override fun onResume() {
        super.onResume()

        val dialogWidth = ActionBar.LayoutParams.MATCH_PARENT
        val dialogHeight = ActionBar.LayoutParams.MATCH_PARENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }


    override val layoutResourceId: Int
        get() = R.layout.img_wide_layout


    override fun initDataBinding() {
        viewDataBinding.imgView.setImageURI(uri)
        viewDataBinding.swipeLayout.setOnSwipeBackListener(object : SwipeBackLayout.SwipeBackListener {
            override fun onViewPositionChanged(fractionAnchor: Float, fractionScreen: Float) {
                Log.e("ayhan", "fractionAnchor : ${fractionAnchor} / fractionScreen : ${fractionScreen}")
                if(fractionAnchor.toInt()==1 && fractionScreen.toInt()==1) {
                    dismiss()
                }
            }

        })

    }
}
