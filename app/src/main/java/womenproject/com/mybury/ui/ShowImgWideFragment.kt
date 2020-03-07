package womenproject.com.mybury.ui

import android.app.ActionBar
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import womenproject.com.mybury.R
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.databinding.ImgWideLayoutBinding
import womenproject.com.mybury.util.ScreenUtils.Companion.getScreenWidth

/**
 * Created by HanAYeon on 2019-05-14.
 */

class ShowImgWideFragment() : BaseDialogFragment<ImgWideLayoutBinding>() {

    private var uri : Uri ?= null
    private var url : String ?= null

    constructor(uri: Uri) : this() {
        this.uri = uri
    }
    constructor(url : String) : this() {
        this.url = url
    }


    override fun onResume() {
        super.onResume()

        val dialogWidth = getScreenWidth(context)
        val dialogHeight = getScreenWidth(context)
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }


    override val layoutResourceId: Int
        get() = R.layout.img_wide_layout


    override fun initDataBinding() {
        if(uri != null) {
            viewDataBinding.imgView.setImageURI(uri)
        } else {
            Glide.with(context!!).load(url).centerCrop().into(viewDataBinding.imgView)
        }


        viewDataBinding.swipeLayout.setOnSwipeBackListener(object : SwipeBackLayout.SwipeBackListener {
            override fun onViewPositionChanged(fractionAnchor: Float, fractionScreen: Float) {
                if(fractionAnchor.toInt()==1 && fractionScreen.toInt()==1) {
                    dismiss()
                }
            }

        })

    }
}
