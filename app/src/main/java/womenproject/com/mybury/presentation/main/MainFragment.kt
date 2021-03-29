package womenproject.com.mybury.presentation.main

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.Preference.Companion.getCloseAlarm3Days
import womenproject.com.mybury.data.Preference.Companion.getEnableShowAlarm
import womenproject.com.mybury.data.Preference.Companion.getFilterForShow
import womenproject.com.mybury.data.Preference.Companion.getFilterListUp
import womenproject.com.mybury.databinding.FragmentMainBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.main.bucketlist.MainBucketListAdapter
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.ui.snackbar.MainSnackBarWidget
import java.util.*


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class MainFragment : BaseFragment<FragmentMainBinding, BucketInfoViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_main

    override val viewModel: BucketInfoViewModel
        get() = BucketInfoViewModel()

    private var currentBucketSize = 0
    override fun initDataBinding() {
        viewDataBinding.mainToolbar.filterClickListener = createOnClickFilterListener()
        viewDataBinding.mainBottomSheet.writeClickListener = createOnClickWriteListener()
        viewDataBinding.mainBottomSheet.myPageClickListener = createOnClickMyPageListener()

        initBucketListUI()
    }

    private fun initBucketListUI() {
        val layoutManager = LinearLayoutManager(context)

        viewDataBinding.bucketList.layoutManager = layoutManager
        viewDataBinding.bucketList.hasFixedSize()

        getMainBucketList()

    }

    private fun getMainBucketList() {

        val filterForShow = getFilterForShow(requireContext())
        val filterListUp = getFilterListUp(requireContext())

        if (filterForShow == null || filterListUp == null) {
            return
        }

        viewModel.getMainBucketList(object : BaseViewModel.MoreCallBackAny {
            override fun restart() {
                getMainBucketList()
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(requireActivity().supportFragmentManager)
            }

            override fun start() {
                startLoading()
            }

            override fun success(value: Any) {
                val response = value as BucketList
                if (response.bucketlists.isEmpty()) {
                    viewDataBinding.blankImg.visibility = View.VISIBLE
                    viewDataBinding.bucketList.visibility = View.GONE
                    viewDataBinding.endImage.visibility = View.GONE
                } else {
                    viewDataBinding.blankImg.visibility = View.GONE
                    viewDataBinding.bucketList.visibility = View.VISIBLE
                    viewDataBinding.endImage.visibility = View.VISIBLE
                    viewDataBinding.bucketList.adapter = MainBucketListAdapter(response.bucketlists, showSnackBar)
                }
                stopLoading()
                if (response.popupYn && isOpenablePopup() && getEnableShowAlarm(requireActivity())) {
                    showDdayPopup()
                }
                currentBucketSize = response.bucketlists.size
            }
        }, filterForShow, filterListUp)

    }

    private fun setBucketCancel(bucketId: String) {
        viewModel.setBucketCancel(object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                setBucketCancel(bucketId)
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(requireActivity().supportFragmentManager)
            }

            override fun start() {
                startLoading()
            }

            override fun success() {
                getMainBucketList()
            }

        }, bucketId)

    }

    private val filterChangedListener: () -> Unit = {
        initBucketListUI()
    }

    private fun isOpenablePopup(): Boolean {
        val currentTime = Date().time
        val daysOverTime = 1000 * 60 * 60 * 24 * 3 // 3일로  설정
        return currentTime - getCloseAlarm3Days(requireContext()) >= daysOverTime
    }

    private fun showDdayPopup() {
        val ddayAlarmDialogFragment = DdayAlarmDialogFragment(goToDday)
        ddayAlarmDialogFragment.show(requireActivity().supportFragmentManager, "tag")
    }

    private fun createOnClickWriteListener(): View.OnClickListener {
        return View.OnClickListener {
            val directions = MainFragmentDirections.actionMainBucketToBucketWrite()
            directions.isAdsShow = currentBucketSize > 4
            it.findNavController().navigate(directions)
        }
    }

    private val goToDday: () -> Unit = {
        createOnClickDdayListener.onClick(requireView())
    }

    private fun createOnClickFilterListener(): View.OnClickListener {
        return View.OnClickListener {
            val filterDialogFragment = FilterDialogFragment(filterChangedListener)
            filterDialogFragment.show(requireActivity().supportFragmentManager, "tag")
        }
    }

    private fun createOnClickMyPageListener(): View.OnClickListener {
        return View.OnClickListener {
            val directions = MainFragmentDirections.actionMainBucketToMyPage()
            directions.isAdsShow = currentBucketSize > 4
            it.findNavController().navigate(directions)
        }
    }

    private val createOnClickDdayListener = View.OnClickListener {
        val directions = MainFragmentDirections.actionMainBucketToDday()
        it.findNavController().navigate(directions)
    }

    private fun bucketCancelListener(info: BucketItem) = View.OnClickListener {
        setBucketCancel(info.id)
    }

    private val showSnackBar: (BucketItem) -> Unit = { info: BucketItem ->
        showCancelSnackBar(requireView(), info)
    }

    private fun showCancelSnackBar(view: View, info: BucketItem) {
        val countText = if (info.goalCount > 1) "\" ${info.userCount}회 완료" else " \" 완료"
        MainSnackBarWidget.make(view, info.title, countText, bucketCancelListener(info))?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Preference.setEnableShowAlarm(requireContext(), false)
    }

}