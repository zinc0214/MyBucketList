package womenproject.com.mybury.presentation.detail

import android.annotation.SuppressLint
import android.app.ActionBar
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.FragmentBucketRetryGuideBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment

class BucketRetryGuideDialogFragment : BaseDialogFragment<FragmentBucketRetryGuideBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_retry_guide

    override fun onResume() {
        super.onResume()

        val dialogWidth = ActionBar.LayoutParams.MATCH_PARENT
        val dialogHeight = ActionBar.LayoutParams.MATCH_PARENT
        dialog?.window?.setDimAmount(0.6F)
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initDataBinding() {
        binding.contentLayout.setOnClickListener { dismiss() }
    }
}
