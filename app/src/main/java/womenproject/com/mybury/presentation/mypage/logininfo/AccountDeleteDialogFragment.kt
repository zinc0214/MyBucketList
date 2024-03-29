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
import womenproject.com.mybury.databinding.DialogAccountDelectBinding
import womenproject.com.mybury.presentation.base.BaseDialogFragment
import womenproject.com.mybury.ui.loadingbutton.animatedDrawables.ProgressType
import womenproject.com.mybury.ui.loadingbutton.customView.ProgressButton

/**
 * Created by HanAYeon on 2019. 1. 15..
 */

open class AccountDeleteDialogFragment(private val startDeleting: () -> Unit,
                                  private val isAnimEnd: () -> Unit) : BaseDialogFragment<DialogAccountDelectBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.dialog_account_delect


    private val animFadeOut = AnimationUtils.loadAnimation(MyBuryApplication.context, R.anim.fade_out)

    override fun initDataBinding() {

        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        binding.activity = this
        binding.lastBucketItem.bucketSuccessClickListener = onClickListener
        binding.lastBucketItem.bucketTitleText = "마이버리와 작별하기"

    }

    override fun onResume() {
        super.onResume()

        val dialogWidth = ActionBar.LayoutParams.MATCH_PARENT
        val dialogHeight = ActionBar.LayoutParams.MATCH_PARENT
        dialog?.window!!.setLayout(dialogWidth, dialogHeight)
    }

    private val onClickListener = View.OnClickListener {
        binding.lastBucketItem.progressLayout.isEnabled = false
        binding.lastBucketItem.progressLayout.isClickable = false

        binding.lastBucketItem.successButtonLayout.circularProgressBar.run {
            progressType = ProgressType.INDETERMINATE
            startAnimation()
            progressAnimator(this).start()
            Handler().run {
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
                    binding.endContentText.visibility = View.GONE
                    isAnimEnd.invoke()
                    dismiss()
                }, 2000)
            }
        }
        binding.executePendingBindings()
    }


    private fun progressAnimator(progressButton: ProgressButton) =
        ValueAnimator.ofFloat(0F, 100F).apply {
            duration = 800
            startDelay = 0
            addUpdateListener { animation ->
                progressButton.setProgress(animation.animatedValue as Float)
            }
        }

    private fun setFinalSuccessUIButton() {
        binding.lastBucketItem.successButtonLayout.successImg.backgroundTintList =
            MyBuryApplication.context.getColorStateList(R.color._a6c6ff)
    }


    private fun setDoneSuccessUIButton() {
        binding.lastBucketItem.bucketItemImage.background =
            MyBuryApplication.context.getDrawable(R.drawable.shape_ffffff_r4_strk_06_e8e8e8)
    }
}