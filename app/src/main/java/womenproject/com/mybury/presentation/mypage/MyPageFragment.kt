package womenproject.com.mybury.presentation.mypage


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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

            headerLayout.apply {
                myPageInfo = _myPageInfo

                moreClickListener = moreButtonOnClickListener
                doingBucketListClickListener = doingBucketListClick
                doneBucketListClickListener = doneBucketListClick
                startedCountTextView.text = _myPageInfo.startedCount.toString()
                completedCountTextView.text = _myPageInfo.completedCount.toString()

                mypageScrollLayout.dDayCountText.text = _myPageInfo.dDayCount.toString()

                constraintLayout.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

                    }

                    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

                    }

                    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                        if (p0?.targetPosition!! == 0.0f) {
                            //      mypageScrollLayout.ddayLayout.visibility = View.VISIBLE
                        } else {
                            //       mypageScrollLayout.ddayLayout.visibility = View.GONE
                        }
                    }

                    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                        val targetPosition: Float = p0?.targetPosition ?: 0.0f
                        if (targetPosition == 1.0f) {
                            mypageScrollLayout.ddayLayout.visibility = View.GONE
                        } else if (targetPosition == 0.0f) {
                            mypageScrollLayout.ddayLayout.visibility = View.VISIBLE
                        }
                    }
                })

            }


            mypageBottomSheet.homeClickListener = createOnClickHomeListener
            mypageBottomSheet.writeClickListener = createOnClickWriteListener

            mypageScrollLayout.ddayListClickListener = createOnClickDdayListListener
            mypageScrollLayout.categoryEditClickListener = createOnClickCategoryEditListener

            mypageMoreMenuLarge.appInfoClickListener = appInfoOnClickListener
            mypageMoreMenuLarge.profileEditClickListener = profileEditOnClickListener
            mypageMoreMenuLarge.loginInfoClickListener = loginInfoClickListener
            mypageMoreMenuLarge.alarmClickListener = alarmSettingClickListener
            mypageMoreMenuLarge.supportClickListener = myBurySupportClickListener
            mypageMoreMenuLarge.contactClickListener = contactToMyBuryClickListener


            mypageMoreMenuLarge.isAlarmVisible = BuildConfig.DEBUG


            mypageMoreMenuLarge.myBurySupport.visibility = if (BuildConfig.DEBUG) View.VISIBLE else View.GONE

            seyMyProfileImg(_myPageInfo.imageUrl)
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun seyMyProfileImg(imgUrl: String?) {
        if (imgUrl.isNullOrBlank()) {
            val num = Random.nextInt(2)
            if (num == 1) {
                viewDataBinding.headerLayout.profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_my, null))
            } else {
                viewDataBinding.headerLayout.profileImg.setImageDrawable(resources.getDrawable(R.drawable.default_profile_bury, null))
            }
        } else {
            Glide.with(this).load(imgUrl).into(viewDataBinding.headerLayout.profileImg)
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

    private val contactToMyBuryClickListener = View.OnClickListener {
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode("mybury.info@gmail.com") +
                "?subject=" + Uri.encode("< 마이버리 문의 >")
        val uri = Uri.parse(uriText)

        send.data = uri
        startActivity(Intent.createChooser(send, "마이버리 문의하기"))
    }

    private val doingBucketListClick = View.OnClickListener {
        val directions = MyPageFragmentDirections.actionMyPageToBucketItemByFilter()
        directions.filter = ShowFilter.started.toString()
        it.findNavController().navigate(directions)
    }

    private val doneBucketListClick = View.OnClickListener {
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
