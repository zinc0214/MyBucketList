package womenproject.com.mybury.presentation.mypage


import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.R
import womenproject.com.mybury.data.MyPageCategory
import womenproject.com.mybury.data.MyPageInfo
import womenproject.com.mybury.data.ShowFilter
import womenproject.com.mybury.databinding.FragmentMyPageBinding
import womenproject.com.mybury.presentation.NetworkFailDialog
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.mypage.categoryedit.MyPageCategoryListAdapter
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel
import kotlin.random.Random

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

class MyPageFragment : BaseFragment<FragmentMyPageBinding, MyPageViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_my_page

    override val viewModel: MyPageViewModel
        get() = MyPageViewModel()

    private val bucketInfoViewModel = BucketInfoViewModel()
    private var isAdsShow = false

    override fun initDataBinding() {

        arguments?.let {
            val args = MyPageFragmentArgs.fromBundle(it)
            val argIsAdsShow = args.isAdsShow
            this.isAdsShow = argIsAdsShow
        }

        setCategoryList()
    }

    private fun setCategoryList() {

        viewModel.getMyPageData(object : BaseViewModel.MoreCallBackAny {
            override fun restart() {
                setCategoryList()
            }

            override fun start() {
                startLoading()
            }

            override fun success(value: Any) {

                val info = value as MyPageInfo
                stopLoading()

                val layoutManager = LinearLayoutManager(context)

                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.layoutManager = layoutManager
                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.hasFixedSize()
                viewDataBinding.mypageScrollLayout.mypageCategoryRecyclerview.adapter = MyPageCategoryListAdapter(context, info.categoryList as MutableList<MyPageCategory>)

                setUpView(info)
            }


            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(requireActivity().supportFragmentManager, "tag")
            }
        })

    }

    private fun setUpView(_myPageInfo: MyPageInfo) {

        viewDataBinding.apply {

            headerLayout.moreClickListener = moreButtonOnClickListener
            headerLayout.doingBucketListClickListener = doingBucketListClickListener
            headerLayout.doneBucketListClickListener = doneBucketListerClickListener
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
            mypageMoreMenuLarge.supportClickListener = myBurySupportClickListener

            mypageMoreMenuLarge.isAlarmVisible = BuildConfig.DEBUG

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

            seyMyProfileImg(_myPageInfo.imageUrl)
        }

    }

    private fun seyMyProfileImg(imgUrl: String?) {
        if (imgUrl.isNullOrBlank()) {
            val num = Random.nextInt(2)
            if (num == 1) {
                viewDataBinding.headerLayout.profileLargeImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_my))
                viewDataBinding.headerLayout.profileImgView.setImageDrawable(resources.getDrawable(R.drawable.default_profile_my))
            } else {
                viewDataBinding.headerLayout.profileLargeImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_bury))
                viewDataBinding.headerLayout.profileImgView.setImageDrawable(resources.getDrawable(R.drawable.default_profile_bury))
            }
        } else {
            Glide.with(this).load(imgUrl).into(viewDataBinding.headerLayout.profileLargeImg)
            Glide.with(this).load(imgUrl).into(viewDataBinding.headerLayout.profileImgView)
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
        directions.isAdsShow = isAdsShow
        it.findNavController().navigate(directions)

    }

    private val createOnClickHomeListener = View.OnClickListener {
        popupClickListener()
        requireActivity().onBackPressed()
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

    private val myBurySupportClickListener = View.OnClickListener {
        val directions = MyPageFragmentDirections.actionMyPageToMyburySupport()
        it.findNavController().navigate(directions)
        popupClickListener()
    }
    private val doingBucketListClickListener = View.OnClickListener {
        val directions = MyPageFragmentDirections.actionMyPageToBucketItemByFilter()
        directions.filter = ShowFilter.started.toString()
        it.findNavController().navigate(directions)
    }

    private val doneBucketListerClickListener = View.OnClickListener {
        val directions = MyPageFragmentDirections.actionMyPageToBucketItemByFilter()
        directions.filter = ShowFilter.completed.toString()
        it.findNavController().navigate(directions)
    }

    private val moreButtonOnClickListener = View.OnClickListener {

        if (viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility == View.GONE) {
            viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility = View.VISIBLE
        } else {
            viewDataBinding.mypageMoreMenuLarge.moreMenuLayout.visibility = View.GONE
        }
    }

}
