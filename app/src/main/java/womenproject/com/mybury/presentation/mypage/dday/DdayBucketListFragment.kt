package womenproject.com.mybury.presentation.mypage.dday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference.Companion.getDdayFilterForShow
import womenproject.com.mybury.data.model.DdayBucketList
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.databinding.FragmentDdayListBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.dialog.LoadFailDialog
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.main.bucketlist.BucketItemHandler
import womenproject.com.mybury.presentation.viewmodels.BucketListViewModel
import womenproject.com.mybury.ui.snackbar.MainSnackBarWidget
import womenproject.com.mybury.util.observeNonNull
import womenproject.com.mybury.util.showToast

/**
 * Created by HanAYeon on 2019. 1. 16..
 */

@AndroidEntryPoint
class DdayBucketListFragment : BaseFragment() {

    private lateinit var binding: FragmentDdayListBinding
    private val viewModel by viewModels<BucketListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dday_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpObservers()
    }

    private fun setUpViews() {
        binding.ddayEachBucketList.layoutManager = LinearLayoutManager(context)
        binding.ddayEachBucketList.hasFixedSize()
        binding.backBtnOnClickListener = backBtnOnClickListener()
        binding.filterClickListener = filterOnClickListener()
        getDdayList()
    }

    private fun setUpObservers() {
        viewModel.bucketCancelLoadState.observe(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> {
                    startLoading()
                }
                LoadState.SUCCESS -> {
                    stopLoading()
                    getDdayList()
                }
                LoadState.FAIL -> {
                    stopLoading()
                    NetworkFailDialog().show(requireActivity().supportFragmentManager)
                }
                else -> {
                    // do Nothing
                }
            }
        }

        viewModel.completeBucketState.observeNonNull(viewLifecycleOwner) {
            if (it.first && it.second != null) {
                showSnackBar.invoke(it.second!!)
            } else {
                requireContext().showToast("다시 시도해주세요.")
                getDdayList()
            }
        }

        viewModel.loadDdayBucketList.observeNonNull(viewLifecycleOwner) {
            when (it.first) {
                LoadState.START -> startLoading()
                LoadState.RESTART -> getDdayList()
                LoadState.SUCCESS -> {
                    stopLoading()
                    setUpBucketListAdapter(it.second)
                }
                LoadState.FAIL -> {
                    stopLoading()
                    LoadFailDialog {
                        onBackPressedFragment()
                    }.show(requireActivity().supportFragmentManager)
                }
            }
        }
    }

    private fun getDdayList() {
        getDdayFilterForShow(requireContext())?.let {
            viewModel.getDdayEachBucketList(it)
        }
    }


    private fun filterOnClickListener() = View.OnClickListener {
        val filterDialogFragment = DdayFilterDialogFragment(filterChangedListener)
        filterDialogFragment.show(requireActivity().supportFragmentManager, "tag")
    }

    private val filterChangedListener: () -> Unit = {
        getDdayList()
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

    private fun setUpBucketListAdapter(ddayBucketList: List<DdayBucketList>) {
        binding.ddayEachBucketList.adapter = DdayBucketTotalListAdapter(ddayBucketList,
            bucketItemHandler = object : BucketItemHandler {
                override fun bucketSelect(itemInfo: BucketItem) {
                    val directions =
                        DdayBucketListFragmentDirections.actionDdayBucketToBucketDetail()
                    directions.bucketId = itemInfo.id
                    this@DdayBucketListFragment.findNavController().navigate(directions)
                }

                override fun bucketComplete(itemInfo: BucketItem) {
                    viewModel.setBucketComplete(itemInfo)
                }
            })
    }
}