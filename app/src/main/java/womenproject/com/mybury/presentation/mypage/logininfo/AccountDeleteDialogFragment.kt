package womenproject.com.mybury.presentation.mypage.logininfo

import android.animation.ValueAnimator
import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import womenproject.com.mybury.MyBuryApplication
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.AccountDelectDialogBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.ui.BucketItemView
import womenproject.com.mybury.ui.loadingbutton.animatedDrawables.ProgressType
import womenproject.com.mybury.ui.loadingbutton.customView.ProgressButton

/**
 * Created by HanAYeon on 2019. 1. 15..
 */

class AccountDeleteDialogFragment : BaseDialogFragment<AccountDelectDialogBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.account_delect_dialog


    //val itemView = BaseBucketItemView(viewDataBinding)

    val animFadeOut = AnimationUtils.loadAnimation(MyBuryApplication.context, R.anim.fade_out)

    override fun initDataBinding() {

        dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        viewDataBinding.activity = this
        viewDataBinding.lastBucketItem.bucketClickListener = onClickListenr
        viewDataBinding.lastBucketItem.bucketSuccessClickListener = onClickListenr
        viewDataBinding.lastBucketItem.bucketTitleText = "마이버리와 작별하기"
        viewDataBinding.endClickListener = onClickListenr

    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = ActionBar.LayoutParams.MATCH_PARENT
        val dialogHeight = ActionBar.LayoutParams.MATCH_PARENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }


    private fun dialogDismiss() {
        this.dismiss()
    }

    private val onClickListenr = View.OnClickListener {
        viewDataBinding.lastBucketItem.successButtonLayout.circularProgressBar.run {
            progressType = ProgressType.INDETERMINATE
            startAnimation()
            progressAnimator(this).start()
            Handler().run {
                viewDataBinding.lastBucketItem.bucketItemLayout.isClickable = false
                postDelayed({
                    setFinalSuccessUIButton()
                }, 800)
                postDelayed({
                    revertAnimation()
                    setDoneSuccessUIButton()
                }, 2000)
                postDelayed({
                    animFadeOut.duration = 1800
                    viewDataBinding.endContentText.visibility=View.GONE
                }, 2000)

            }
        }
        viewDataBinding.executePendingBindings()
    }


    open fun progressAnimator(progressButton: ProgressButton) = ValueAnimator.ofFloat(0F, 100F).apply {
        duration = 800
        startDelay = 0
        addUpdateListener { animation ->
            progressButton.setProgress(animation.animatedValue as Float)
        }
    }

    open fun setFinalSuccessUIButton() {
        viewDataBinding.lastBucketItem.successButtonLayout.successImg.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color._a6c6ff)
    }


    fun setDoneSuccessUIButton() {
        // viewDataBinding.lastBucketItem.successButtonLayout.successImg.backgroundTintList = MyBuryApplication.context.getColorStateList(R.color._e8e8e8)
        viewDataBinding.lastBucketItem.bucketItemImage.background = MyBuryApplication.context.getDrawable(R.drawable.bucket_item_base_background)
    }
}