package womenproject.com.mybury.ui

import android.net.Uri
import com.bumptech.glide.Glide
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.LayoutImgWideBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.util.ScreenUtils.Companion.getScreenWidth

/**
 * Created by HanAYeon on 2019-05-14.
 */

class ShowImgWideFragment() : BaseDialogFragment<LayoutImgWideBinding>() {

    private var uri: Uri? = null
    private var url: String? = null

    constructor(uri: Uri) : this() {
        this.uri = uri
    }

    constructor(url: String) : this() {
        this.url = url
    }


    override fun onResume() {
        super.onResume()

        val dialogWidth = getScreenWidth(context)
        val dialogHeight = getScreenWidth(context)
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }


    override val layoutResourceId: Int
        get() = R.layout.layout_img_wide


    override fun initDataBinding() {
        if(uri != null) {
            binding.imgView.setImageURI(uri)
        } else {
            Glide.with(requireContext()).load(url).centerCrop().into(binding.imgView)
        }


        binding.swipeLayout.setOnSwipeBackListener(object : SwipeBackLayout.SwipeBackListener {
            override fun onViewPositionChanged(fractionAnchor: Float, fractionScreen: Float) {
                if(fractionAnchor.toInt()==1 && fractionScreen.toInt()==1) {
                    dismiss()
                }
            }

        })

    }
}
