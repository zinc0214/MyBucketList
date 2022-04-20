package womenproject.com.mybury.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.Preference.Companion.getCloseAlarm3Days
import womenproject.com.mybury.data.Preference.Companion.getEnableShowAlarm
import womenproject.com.mybury.data.Preference.Companion.getFilterForShow
import womenproject.com.mybury.data.Preference.Companion.getFilterListUp
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.databinding.FragmentMainBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.detail.BucketDetailViewModel
import womenproject.com.mybury.presentation.dialog.LoadFailDialog
import womenproject.com.mybury.presentation.main.bucketlist.BucketItemHandler
import womenproject.com.mybury.presentation.main.bucketlist.MainBucketListAdapter
import womenproject.com.mybury.presentation.viewmodels.BucketListViewModel
import womenproject.com.mybury.ui.snackbar.MainSnackBarWidget
import womenproject.com.mybury.util.showToast
import java.util.*


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel by viewModels<BucketListViewModel>()
    private val detailViewModel by viewModels<BucketDetailViewModel>()
    private lateinit var bucketListAdapter: MainBucketListAdapter

    private var currentBucketSize = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBucketListUI()
    }

    private fun initBucketListUI() {
        val layoutManager = LinearLayoutManager(context)

        binding.mainToolbar.sortClickListener = bucketSortClickListener()
        binding.mainToolbar.filterClickListener = createOnClickFilterListener()
        binding.mainToolbar.searchClickListener = bucketSearchClickListener()
        binding.mainBottomSheet.writeClickListener = createOnClickWriteListener()
        binding.mainBottomSheet.myPageClickListener = createOnClickMyPageListener()

        bucketListAdapter = MainBucketListAdapter(object : BucketItemHandler {
            override fun bucketSelect(itemInfo: BucketItem) {
                val directions = MainFragmentDirections.actionMainBucketToBucketDetail()
                directions.bucketId = itemInfo.id
                this@MainFragment.findNavController().navigate(directions)
            }

            override fun bucketComplete(itemInfo: BucketItem) {
                detailViewModel.setBucketComplete(object : BaseViewModel.Simple3CallBack {
                    override fun restart() {
                        requireContext().showToast("다시 시도해주세요.")
                    }

                    override fun start() {

                    }

                    override fun success() {
                        showSnackBar.invoke(itemInfo)
                    }

                    override fun fail() {
                        requireContext().showToast("다시 시도해주세요.")
                    }

                }, itemInfo.id)
            }

        })

        binding.bucketList.layoutManager = layoutManager
        binding.bucketList.hasFixedSize()
        binding.bucketList.adapter = bucketListAdapter

        setUpObservers()
        getMainBucketList()
    }

    private fun setUpObservers() {
        viewModel.bucketListLoadState.observe(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> {
                    startLoading()
                }
                LoadState.SUCCESS -> {
                    stopLoading()
                }
                LoadState.FAIL -> {
                    stopLoading()
                    LoadFailDialog { }
                }
                LoadState.RESTART -> {
                    getMainBucketList()
                }
                else -> {
                    // do Nothing
                }
            }
        }

        viewModel.bucketCancelLoadState.observe(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> {
                    startLoading()
                }
                LoadState.SUCCESS -> {
                    stopLoading()
                    getMainBucketList()
                }
                LoadState.FAIL -> {
                    stopLoading()
                    LoadFailDialog { }
                }
                else -> {
                    // do Nothing
                }
            }
        }

        viewModel.homeBucketList.observe(viewLifecycleOwner) {
            it?.let {
                if (it.bucketlists.isEmpty()) {
                    binding.apply {
                        blankImg.visibility = View.VISIBLE
                        bucketList.visibility = View.GONE
                        endImage.visibility = View.GONE
                    }
                } else {
                    binding.apply {
                        blankImg.visibility = View.GONE
                        bucketList.visibility = View.VISIBLE
                        endImage.visibility = View.VISIBLE
                        bucketListAdapter.updateBucketList(it.bucketlists)
                    }
                }
                if (it.popupYn && isOpenablePopup() && getEnableShowAlarm(requireActivity())) {
                    showDdayPopup()
                }
                currentBucketSize = it.bucketlists.size
            }
        }
    }

    private fun getMainBucketList() {
        val filterForShow = getFilterForShow(requireContext())
        val filterListUp = getFilterListUp(requireContext())
        if (filterForShow == null || filterListUp == null) return
        viewModel.getHomeBucketList(filterForShow, filterListUp)
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
            directions.isAdsShow = currentBucketSize > 2
            it.findNavController().navigate(directions)
        }
    }

    private val goToDday: () -> Unit = {
        createOnClickDdayListener.onClick(requireView())
    }


    private fun bucketSortClickListener() = View.OnClickListener {
        val directions = MainFragmentDirections.actionMainBucketToBucketSort()
        it.findNavController().navigate(directions)
    }

    private fun bucketSearchClickListener() = View.OnClickListener {
        val directions = MainFragmentDirections.actionMainBucketToBucketSearch()
        it.findNavController().navigate(directions)
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
            directions.isAdsShow = currentBucketSize > 2
            it.findNavController().navigate(directions)
        }
    }

    private val createOnClickDdayListener = View.OnClickListener {
        val directions = MainFragmentDirections.actionMainBucketToDday()
        it.findNavController().navigate(directions)
    }

    private fun bucketCancelListener(info: BucketItem) {
        viewModel.bucketCancel(info.id)
    }

    private val showSnackBar: (BucketItem) -> Unit = { info: BucketItem ->
        showCancelSnackBar(requireView(), info)
    }

    private fun showCancelSnackBar(view: View, info: BucketItem) {
        val countText = if (info.goalCount > 1) "\" ${info.userCount}회 완료" else " \" 완료"
        MainSnackBarWidget.make(view, info.title, countText) { bucketCancelListener(info) }?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Preference.setEnableShowAlarm(requireContext(), false)
    }

}