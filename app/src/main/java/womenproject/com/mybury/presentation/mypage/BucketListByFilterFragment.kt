package womenproject.com.mybury.presentation.mypage

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
import womenproject.com.mybury.data.Preference.Companion.getFilterListUp
import womenproject.com.mybury.data.ShowFilter
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.databinding.FragmentBucketListByCategoryBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.dialog.LoadFailDialog
import womenproject.com.mybury.presentation.main.bucketlist.BucketItemHandler
import womenproject.com.mybury.presentation.main.bucketlist.MainBucketListAdapter
import womenproject.com.mybury.presentation.viewmodels.BucketListViewModel
import womenproject.com.mybury.ui.snackbar.MainSnackBarWidget
import womenproject.com.mybury.util.observeNonNull
import womenproject.com.mybury.util.showToast

@AndroidEntryPoint
class BucketListByFilterFragment : BaseFragment() {

    private lateinit var binding: FragmentBucketListByCategoryBinding
    private lateinit var filterType: String

    private val viewModel by viewModels<BucketListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bucket_list_by_category,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpObservers()
        initBucketListUI()
    }

    private fun setUpViews() {
        arguments?.let {
            val args = BucketListByFilterFragmentArgs.fromBundle(it)
            val filter = args.filter
            this.filterType = filter!!
        }

        binding.headerLayout.title =
            when (filterType) {
                ShowFilter.started.toString() -> "진행중"
                else -> "완료"
            }
        binding.headerLayout.backBtnOnClickListener = backBtnOnClickListener()
    }

    private fun setUpObservers() {
        viewModel.bucketCancelLoadState.observe(viewLifecycleOwner) {
            when (it) {
                LoadState.FAIL,
                LoadState.RESTART -> {
                    stopLoading()
                    LoadFailDialog { }
                }
                LoadState.START -> {
                    startLoading()
                }
                LoadState.SUCCESS -> {
                    stopLoading()
                    loadFilterBucketList()
                }
                else -> {
                    // do Nothing
                }
            }
        }

        viewModel.homeBucketList.observeNonNull(viewLifecycleOwner) {
            setUpBucketListAdapter(it.bucketlists)
        }

        viewModel.completeBucketState.observeNonNull(viewLifecycleOwner) {
            if (it.first && it.second != null) {
                showSnackBar.invoke(it.second!!)
            } else {
                requireContext().showToast("다시 시도해주세요.")
                loadFilterBucketList()
            }
        }

        loadFilterBucketList()
    }

    private fun loadFilterBucketList() {
        getFilterListUp(requireContext())?.let { filterListUp ->
            viewModel.getHomeBucketList(filterType, filterListUp)
        }
    }

    private fun initBucketListUI() {
        val layoutManager = LinearLayoutManager(context)

        binding.bucketList.layoutManager = layoutManager
        binding.bucketList.hasFixedSize()
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

    private fun setUpBucketListAdapter(bucketList: List<BucketItem>) {
        binding.bucketList.adapter = MainBucketListAdapter(object : BucketItemHandler {
            override fun bucketSelect(itemInfo: BucketItem) {
                val directions =
                    BucketListByFilterFragmentDirections.actionFilterBucketListToDetail()
                directions.bucketId = itemInfo.id
                this@BucketListByFilterFragment.findNavController().navigate(directions)
            }

            override fun bucketComplete(itemInfo: BucketItem) {
                viewModel.setBucketComplete(itemInfo)
            }
        }).apply {
            replaceItems(bucketList)
        }
    }
}