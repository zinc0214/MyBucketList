package womenproject.com.mybury.presentation.mypage


import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Category
import womenproject.com.mybury.data.MyPageInfo
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.data.Preference.Companion.getUserId
import womenproject.com.mybury.databinding.FragmentMyPageBinding
import womenproject.com.mybury.presentation.NetworkFailDialog
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.mypage.categoryedit.MyPageCategoryListAdapter
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_my_page

    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()

    private val bucketInfoViewModel = BucketInfoViewModel()
    override fun initDataBinding() {

        setCategoryList()

    }

    private fun setCategoryList() {

        viewModel.getMyPageData(object : MyPageViewModel.GetMyPageInfoListener {
            override fun success(myPageInfo: MyPageInfo) {
                stopLoading()
                Log.e("ayhan", "cate: ${myPageInfo.categoryMap.size}")

                val layoutManager = LinearLayoutManager(context)

                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.layoutManager = layoutManager
                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.hasFixedSize()
                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.adapter = MyPageCategoryListAdapter(context, myPageInfo.categoryMap as MutableList<Category>)

                setUpView(myPageInfo)
            }

            override fun start() {
                startLoading()
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(activity!!.supportFragmentManager, "tag")
            }
        }, getAccessToken(context!!), getUserId(context!!))


/*        bucketInfoViewModel.getCategoryList(object : BucketInfoViewModel.GetBucketListCallBackListener {
            override fun success(_categoryList: List<Category>) {
                stopLoading()
                Log.e("ayhan", "cate: ${_categoryList.size}")

                val layoutManager = LinearLayoutManager(context)

                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.layoutManager = layoutManager
                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.hasFixedSize()
                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.adapter = MyPageCategoryListAdapter(context, _categoryList as MutableList<Category>)

                setUpView()
            }

            override fun start() {
                startLoading()
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(activity!!.supportFragmentManager, "tag")
            }
        }, getUserId(context!!))*/


/*
        bucketInfoViewModel.getCategoryList(object : BucketInfoViewModel.GetBucketListCallBackListener{
            override fun start() {

            }

            override fun success(categoryList: List<Category>) {

            }

            override fun fail() {

            }

        }, getUserId(context!!))


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
*/


        /*viewModel.getCategory(object : BucketWriteViewModel.GetBucketListCallBackListener {
            override fun start() {

            }

            override fun success(bucketCategory: BucketCategory) {
                for(i in 0 until bucketCategory.category.size) {
                    category.add(bucketCategory.category[i].name)
                }
                viewDataBinding.mainScrollAppbar.addOnOffsetChangedListener(this@MyPageFragment)
                startAlphaAnimation(viewDataBinding.mainTextviewTitle, 0, View.INVISIBLE)


                val layoutManager = LinearLayoutManager(context)

                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.layoutManager = layoutManager
                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.hasFixedSize()
                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.adapter = MyPageCategoryListAdapter(context, category)

                viewDataBinding.executePendingBindings()
            }

            override fun fail() {

            }

        })*/

    }


    private fun setUpView(_myPageInfo: MyPageInfo) {

        viewDataBinding.apply {

            headerLayout.moreClickListener = moreButtonOnClickListener
            headerLayout.startCountText.text = _myPageInfo.startedCount.toString()
            headerLayout.completeCountText.text = _myPageInfo.completedCount.toString()
            headerLayout.name.text = _myPageInfo.name

            headerLayout.background

            mypageScrollLayout.dDayCountText.text = _myPageInfo.dDayCount.toString()


            mypageBottomSheet.homeClickListener = createOnClickHomeListener
            mypageBottomSheet.writeClickListener = createOnClickWriteListener

            mypageScrollLayout.ddayListClickListener = createOnClickDdayListListener
            mypageScrollLayout.categoryEditClickListener = createOnClickCategoryEditListener

            mypageMoreMenuLarge.appInfoClickListener = appInfoOnClickListener
            mypageMoreMenuLarge.profileEditClickListener = profileEditOnClickListener
            mypageMoreMenuLarge.loginInfoClickListener = loginInfoClickListener
            mypageMoreMenuLarge.alarmClickListener = alarmSettingClickListener


            appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, p1 ->
                if (p1 == -384) {
                    mypageScrollLayout.ddayLayout.visibility = View.GONE
                    headerLayout.moreBtn.visibility = View.GONE
                    popupClickListener()
                } else if (p1 == 0) {
                    mypageScrollLayout.ddayLayout.visibility = View.VISIBLE
                    headerLayout.moreBtn.visibility = View.VISIBLE
                }
            })

        }

    }

    private fun popupClickListener() {
        if (viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility == View.VISIBLE) {
            viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility = View.GONE
        }
    }

    private val createOnClickWriteListener = View.OnClickListener {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToWrite()
        it.findNavController().navigate(directions)

    }

    private val createOnClickHomeListener = View.OnClickListener {
        popupClickListener()
        activity!!.onBackPressed()
    }

    private val createOnClickDdayListListener = View.OnClickListener {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToDday()
        it.findNavController().navigate(directions)

    }

    private val createOnClickCategoryEditListener = View.OnClickListener {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToCategoryEdit()
        it.findNavController().navigate(directions)
    }


    private val profileEditOnClickListener = View.OnClickListener {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToProfileEdit()
        it.findNavController().navigate(directions)
    }

    private val appInfoOnClickListener = View.OnClickListener {
        val directions = MyPageFragmentDirections.actionMyPageToAppInfo()
        it.findNavController().navigate(directions)
        popupClickListener()
    }

    private val loginInfoClickListener = View.OnClickListener {
        val directions = MyPageFragmentDirections.actionMyPageToLoginInfo()
        it.findNavController().navigate(directions)
        popupClickListener()
    }

    private val alarmSettingClickListener = View.OnClickListener {
        val directions = MyPageFragmentDirections.actionMyPageToAlarmSetting()
        it.findNavController().navigate(directions)
        popupClickListener()
    }
    private val moreButtonOnClickListener = View.OnClickListener {

        if (viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility == View.GONE) {
            viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility = View.VISIBLE
        } else {
            viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility = View.GONE
        }
    }

}
