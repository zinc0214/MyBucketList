package womenproject.com.mybury.view

import android.app.ActionBar
import android.net.Uri
import android.util.Log
import womenproject.com.mybury.R
import womenproject.com.mybury.base.BaseDialogFragment
import womenproject.com.mybury.databinding.ImgWideLayoutBinding
import womenproject.com.mybury.ui.SwipeBackLayout

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


    override fun initStartView() {

        viewDataBinding.imgView.setImageURI(uri)


    }

    override fun initDataBinding() {


    }

    override fun initAfterBinding() {
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
