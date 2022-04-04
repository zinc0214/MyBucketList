package womenproject.com.mybury.presentation.mypage


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.BuildConfig
import womenproject.com.mybury.R
import womenproject.com.mybury.data.MyPageInfo
import womenproject.com.mybury.data.ShowFilter
import womenproject.com.mybury.data.WebViewType
import womenproject.com.mybury.databinding.FragmentMyPageBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.mypage.categoryedit.MyPageCategoryListAdapter
import womenproject.com.mybury.presentation.viewmodels.MyPageViewModel
import kotlin.random.Random

/**
 * Created by HanAYeon on 2019. 4. 23..
 */

@AndroidEntryPoint
class MyPageFragment : BaseFragment() {

    private lateinit var binding: FragmentMyPageBinding

    private val viewModel by viewModels<MyPageViewModel>()

    private var isAdsShow = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
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

                binding.mypageScrollLayout.mypageCategoryRecyclerview.layoutManager =
                    layoutManager
                binding.mypageScrollLayout.mypageCategoryRecyclerview.hasFixedSize()
                binding.mypageScrollLayout.mypageCategoryRecyclerview.adapter =
                    MyPageCategoryListAdapter(info.showableCategoryList())

                setUpView(info)
            }


            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(requireActivity().supportFragmentManager, "tag")
            }
        })

    }

    private fun setUpView(_myPageInfo: MyPageInfo) {

        binding.apply {

            view = this@MyPageFragment
            myPageInfo = _myPageInfo

            headerLayout.apply {
                constraintLayout.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionTrigger(
                        p0: MotionLayout?,
                        p1: Int,
                        p2: Boolean,
                        p3: Float
                    ) {

                    }

                    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

                    }

                    override fun onTransitionChange(
                        p0: MotionLayout?,
                        p1: Int,
                        p2: Int,
                        p3: Float
                    ) {

                    }

                    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                        val targetPosition: Float = p0?.targetPosition ?: 0.0f
                        if (targetPosition == 1.0f) {
                            mypageScrollLayout.ddayLayout.visibility = View.GONE
                            mypageScrollLayout.supportLayout.visibility = View.GONE
                        } else if (targetPosition == 0.0f) {
                            mypageScrollLayout.ddayLayout.visibility = View.VISIBLE
                            mypageScrollLayout.supportLayout.visibility = View.VISIBLE
                        }
                    }
                })
            }

            mypageMoreMenuLarge.isAlarmVisible = BuildConfig.DEBUG

            seyMyProfileImg(_myPageInfo.imageUrl)
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun seyMyProfileImg(imgUrl: String?) {
        if (imgUrl.isNullOrBlank()) {
            val num = Random.nextInt(2)
            if (num == 1) {
                binding.headerLayout.profileImg.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.default_profile_my,
                        null
                    )
                )
            } else {
                binding.headerLayout.profileImg.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.default_profile_bury,
                        null
                    )
                )
            }
        } else {
            Glide.with(this).load(imgUrl).into(binding.headerLayout.profileImg)
        }
    }

    private fun popupClickListener() {
        if (binding.mypageMoreMenuLarge.moreMenuLayout.visibility == View.VISIBLE) {
            binding.mypageMoreMenuLarge.moreMenuLayout.visibility = View.GONE
        }
    }

    fun writeClickListener(v : View) {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToWrite()
        directions.isAdsShow = isAdsShow
        v.findNavController().navigate(directions)
    }

    fun homeClickListener() {
        popupClickListener()
        requireActivity().onBackPressed()
    }

    fun goToDdayList(v : View) {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToDday()
        v.findNavController().navigate(directions)
    }

    fun goToCategoryEdit(v : View) {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToCategoryEdit()
        v.findNavController().navigate(directions)
    }

    fun goToProfileEdit(v : View) {
        popupClickListener()
        val directions = MyPageFragmentDirections.actionMyPageToProfileEdit()
        v.findNavController().navigate(directions)
    }

    fun goToAppInfoPage(v : View) {
        val directions = MyPageFragmentDirections.actionMyPageToAppInfo()
        v.findNavController().navigate(directions)
        popupClickListener()
    }

    fun goToLoginInfo(v : View) {
        val directions = MyPageFragmentDirections.actionMyPageToLoginInfo()
        v.findNavController().navigate(directions)
        popupClickListener()
    }

    fun goToAlarmSetting(v : View) {
        val directions = MyPageFragmentDirections.actionMyPageToAlarmSetting()
        v.findNavController().navigate(directions)
        popupClickListener()
    }

    fun goToMyBurySupport(v : View) {
        val directions = MyPageFragmentDirections.actionMyPageToMyburySupport()
        v.findNavController().navigate(directions)
        popupClickListener()
    }

    fun goToContactToMyBuryByEmail() {
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode("mybury.info@gmail.com") +
                "?subject=" + Uri.encode("< 마이버리 문의 >")
        val uri = Uri.parse(uriText)

        send.data = uri
        startActivity(Intent.createChooser(send, "마이버리 문의하기"))
    }

    fun goToDoingBucketList(v : View) {
        val directions = MyPageFragmentDirections.actionMyPageToBucketItemByFilter()
        directions.filter = ShowFilter.started.toString()
        v.findNavController().navigate(directions)
    }

    fun goToDoneBucketList(v : View) {
        val directions = MyPageFragmentDirections.actionMyPageToBucketItemByFilter()
        directions.filter = ShowFilter.completed.toString()
        v.findNavController().navigate(directions)
    }

    fun goToNoticeWebView(v : View) {
        val directions = MyPageFragmentDirections.actionMyPageToNotice()
        directions.type = WebViewType.notice.toString()
        v.findNavController().navigate(directions)
    }

    fun moreClickListener() {
        if (binding.mypageMoreMenuLarge.moreMenuLayout.visibility == View.GONE) {
            binding.mypageMoreMenuLarge.moreMenuLayout.visibility = View.VISIBLE
        } else {
            binding.mypageMoreMenuLarge.moreMenuLayout.visibility = View.GONE
        }
    }

}
