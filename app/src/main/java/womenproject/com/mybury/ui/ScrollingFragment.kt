package womenproject.com.mybury.ui


import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import womenproject.com.mybury.presentation.mypage.categoryedit.MyPageCategoryListAdapter
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel
import womenproject.com.mybury.R
import womenproject.com.mybury.databinding.ScrollBinding
import womenproject.com.mybury.presentation.mypage.MyPageFragmentDirections

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

class ScrollingFragment : BaseFragment<ScrollBinding, MyPageViewModel>(), AppBarLayout.OnOffsetChangedListener {


    override val layoutResourceId: Int
        get() = R.layout.scroll

    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()

    override fun initDataBinding() {
        viewDataBinding.viewModel = viewModel
        viewDataBinding.headerLayout.viewModel = viewModel

        viewDataBinding.mypageBottomSheet.homeClickListener = createOnClickHomeListener()
        viewDataBinding.mypageBottomSheet.writeClickListener = createOnClickWriteListener()
        viewDataBinding.mypageScrollLayout.ddayListClickListener = createOnClickDdayListListener()
        viewDataBinding.mypageScrollLayout.categoryEditClickListener = createOnClickCategoryEditListener()

        viewDataBinding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
                Log.e("ayhan", "$p1")
                if(p1 == -384) {
                    viewDataBinding.mypageScrollLayout.ddayLayout.visibility = View.GONE
                } else if(p1 == 0) {
                    viewDataBinding.mypageScrollLayout.ddayLayout.visibility = View.VISIBLE
                }
            }

        })

        viewDataBinding.headerLayout.constraintToolbar.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            Log.e("ayhan", "$left, $top")
        }

        setCategoryList()

    }

    private fun setCategoryList() {


        val categoryList = mutableListOf<String>()
        categoryList.add("없음")
        categoryList.add("여행")
        categoryList.add("서울 맛집")
        categoryList.add("다이어트")
        categoryList.add("스터디")
        categoryList.add("도라에몽 주머니")


        val layoutManager = LinearLayoutManager(context)

        viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.layoutManager = layoutManager
        viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.hasFixedSize()
        viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.adapter = MyPageCategoryListAdapter(context, categoryList)


        /*
         viewModel.getCategory(object : BucketWriteViewModel.GetBucketListCallBackListener {
             override fun start() {

             }

             override fun success(bucketCategory: BucketCategory) {
                 for(i in 0 until bucketCategory.category.size) {
                     category.add(bucketCategory.category[i].name)
                 }

                // viewDataBinding.mainScrollAppbar.addOnOffsetChangedListener(this@ScrollingFragment)
                // startAlphaAnimation(viewDataBinding.mainTextviewTitle, 0, View.INVISIBLE)


                 val layoutManager = LinearLayoutManager(context)

                 viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.layoutManager = layoutManager
                 viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.hasFixedSize()
                 viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.adapter = MyPageCategoryListAdapter(context, category)

                 viewDataBinding.executePendingBindings()
             }

             override fun fail() {

             }

         })
 */
    }


    private fun createOnClickWriteListener(): View.OnClickListener {
        return View.OnClickListener {
            val directions = MyPageFragmentDirections.actionMyPageToWrite()

            it.findNavController().navigate(directions)
        }
    }

    private fun createOnClickHomeListener(): View.OnClickListener {
        return View.OnClickListener {
            activity!!.onBackPressed()
        }
    }

    private fun createOnClickDdayListListener(): View.OnClickListener {
        return View.OnClickListener {
            val directions = MyPageFragmentDirections.actionMyPageToDday()

            it.findNavController().navigate(directions)
        }
    }

    private fun createOnClickCategoryEditListener(): View.OnClickListener {
        return View.OnClickListener {
            val directions = MyPageFragmentDirections.actionMyPageToCategoryEdit()

            it.findNavController().navigate(directions)
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()

        //  handleAlphaOnTitle(percentage)
        //  handleToolbarTitleVisibility(percentage)

        Log.e("ayhan", "${maxScroll} ,, $percentage")
    }

/*    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(viewDataBinding.mainTextviewTitle, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)

                mIsTheTitleVisible = true
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(viewDataBinding.mainTextviewTitle, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                mIsTheTitleVisible = false
            }
        }
    }*/

    /* private fun handleAlphaOnTitle(percentage: Float) {
         if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
             if (mIsTheTitleContainerVisible) {
                 startAlphaAnimation(viewDataBinding.mainLinearlayoutTitle.rootView, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                 startAlphaAnimation(viewDataBinding.blueLayout, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)

                 val layout = viewDataBinding.scrollLayout.content_scroll_layout
                 val params = layout.layoutParams as FrameLayout.LayoutParams
                 params.topMargin = 128
                 layout.setLayoutParams(params)

                 mIsTheTitleContainerVisible = false

                 Log.e("ayhan", "handleAlphaOnTitle1")
             }

         } else {

             if (!mIsTheTitleContainerVisible) {
                 startAlphaAnimation(viewDataBinding.mainLinearlayoutTitle.rootView, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                 startAlphaAnimation(viewDataBinding.blueLayout, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)

                 val layout = viewDataBinding.scrollLayout.content_scroll_layout
                 val params = layout.layoutParams as FrameLayout.LayoutParams
                 params.topMargin = 0
                 layout.setLayoutParams(params)

                 mIsTheTitleContainerVisible = true

                 Log.e("ayhan", "handleAlphaOnTitle2")
             }
         }
     }*/

    companion object {

        private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.3f
        private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.5f
        private val ALPHA_ANIMATIONS_DURATION = 100

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
