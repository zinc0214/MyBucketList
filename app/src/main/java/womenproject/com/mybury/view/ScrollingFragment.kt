package womenproject.com.mybury.view

import android.view.View
import android.view.animation.AlphaAnimation
import com.google.android.material.appbar.AppBarLayout
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.databinding.ScrollBinding
import womenproject.com.mybury.viewmodels.MyPageViewModel
import womenproject.com.mybury.R


class ScrollingFragment : BaseFragment<ScrollBinding, MyPageViewModel>(), AppBarLayout.OnOffsetChangedListener {

    override val layoutResourceId: Int
        get() = R.layout.scroll
    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()


    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

    override fun initStartView() {
        viewDataBinding.viewModel = viewModel
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

        viewDataBinding.appBarLayout.addOnOffsetChangedListener(this)
        startAlphaAnimation(viewDataBinding.scrollDoneLayout, 0, View.INVISIBLE)

    }


    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(viewDataBinding.scrollDoneLayout, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                mIsTheTitleVisible = true
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(viewDataBinding.scrollDoneLayout, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(viewDataBinding.myPageInfoLayout.root, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                mIsTheTitleContainerVisible = false
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(viewDataBinding.myPageInfoLayout.root, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                mIsTheTitleContainerVisible = true
            }
        }
    }

    companion object {

        private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.3f
        private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
        private val ALPHA_ANIMATIONS_DURATION = 200

        fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
            val alphaAnimation = if (visibility == View.VISIBLE)
                AlphaAnimation(0f, 1f)
            else
                AlphaAnimation(1f, 0f)

            alphaAnimation.duration = duration
            alphaAnimation.fillAfter = true
            v.startAnimation(alphaAnimation)
        }
    }
}