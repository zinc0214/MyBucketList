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
import womenproject.com.mybury.ui.loadingbutton.animatedDrawables.ProgressType
import womenproject.com.mybury.ui.loadingbutton.customView.ProgressButton

/**
 * Created by HanAYeon on 2019. 1. 15..
 */

class AccountDeleteDialogFragment(private val startDeleting : () -> Unit,
                                  private val isAnimEnd : () -> Unit ) : BaseDialogFragment<AccountDelectDialogBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.account_delect_dialog


    val animFadeOut = AnimationUtils.loadAnimation(MyBuryApplication.context, R.anim.fade_out)

    override fun initDataBinding() {

        dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        viewDataBinding.activity = this
        viewDataBinding.lastBucketItem.bucketClickListener = onClickListener
        viewDataBinding.lastBucketItem.bucketSuccessClickListener = onClickListener
        viewDataBinding.lastBucketItem.bucketTitleText = "마이버리와 작별하기"
        viewDataBinding.endClickListener = onClickListener

    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = ActionBar.LayoutParams.MATCH_PARENT
        val dialogHeight = ActionBar.LayoutParams.MATCH_PARENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    private val onClickListener = View.OnClickListener {
        viewDataBinding.lastBucketItem.successButtonLayout.circularProgressBar.run {
            progressType = ProgressType.INDETERMINATE
            startAnimation()
            progressAnimator(this).start()
            Handler().run {
                viewDataBinding.lastBucketItem.bucketItemLayout.isClickable = false
                postDelayed({
                    startDeleting.invoke()
                    setFinalSuccessUIButton()
                }, 800)
                postDelayed({
                    revertAnimation()
                    setDoneSuccessUIButton()
                }, 2000)
                postDelayed({
                    animFadeOut.duration = 1800
                    viewDataBinding.endContentText.visibility=View.GONE
                    isAnimEnd.invoke()
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


    private fun setDoneSuccessUIButton() {
        viewDataBinding.lastBucketItem.bucketItemImage.background = MyBuryApplication.context.getDrawable(R.drawable.bucket_item_base_background)
    }
}