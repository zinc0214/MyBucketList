package womenproject.com.mybury.presentation.main.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.SortFilter
import womenproject.com.mybury.databinding.FragmentBucketSortBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.mypage.categoryedit.ItemTouchHelperCallback
import womenproject.com.mybury.presentation.viewmodels.BucketSortViewModel
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.ui.ItemMovedListener
import womenproject.com.mybury.util.showToast

class BucketListSortFragment : Fragment(),
    ItemDragListener,
    ItemMovedListener {

    private lateinit var viewModel: BucketSortViewModel
    private lateinit var binding: FragmentBucketSortBinding

    private lateinit var itemTouchHelper: ItemTouchHelper
    private var changeBucketList = listOf<BucketItem>()
    private var originBucketList = listOf<BucketItem>()

    private var filterForShow: String? = null
    private var filterListUp: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bucket_sort, container, false)
        viewModel = BucketSortViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterForShow = Preference.getFilterForShow(requireContext())
        filterListUp = Preference.getFilterListUp(requireContext())
        setUpObservers()
        getMainBucketList()

    }

    private fun getMainBucketList() {
        if (filterForShow == null || filterListUp == null) {
            return
        }
        viewModel.getMainBucketList(filterForShow!!, filterListUp!!)
    }

    private fun setUpObservers() {
        viewModel.bucketLoadState.observe(viewLifecycleOwner) {
            when (it) {
                BaseViewModel.LoadState.START -> {
                    startLoading()
                }
                BaseViewModel.LoadState.FAIL -> {
                    stopLoading()
                    "다시 시도해주세요.".showToast(requireContext())
                }
                BaseViewModel.LoadState.RESTART -> {
                    getMainBucketList()
                }
                BaseViewModel.LoadState.SUCCESS -> {
                    stopLoading()
                }
                else -> {
                    // Do Nothing
                }
            }
        }

        viewModel.bucketUpdateState.observe(viewLifecycleOwner) {
            when (it) {
                BaseViewModel.LoadState.START -> {
                    startLoading()
                }
                BaseViewModel.LoadState.FAIL -> {
                    stopLoading()
                    "다시 시도해주세요.".showToast(requireContext())
                }
                BaseViewModel.LoadState.RESTART -> {
                    updateBucketOrder()
                }
                BaseViewModel.LoadState.SUCCESS -> {
                    stopLoading()
                    successUpdateBucketOrder()
                }
                else -> {
                    // Do Nothing
                }
            }
        }
        viewModel.allBucketResult.observe(viewLifecycleOwner) {
            changeBucketList = it
            originBucketList = it
            setUpViews()
        }
    }

    private fun setUpViews() {
        setBucketListAdapter()
        binding.toolBarLayout.setConfirmClickListener { updateBucketOrder() }
    }

    private fun setBucketListAdapter() {
        val editBucketListAdapter = SortBucketListAdapter(
            changeBucketList,
            this,
            this
        )

        binding.bucketEditListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = editBucketListAdapter
        }

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(editBucketListAdapter))
        itemTouchHelper.attachToRecyclerView(binding.bucketEditListView)
    }

    private fun updateBucketOrder() {
        viewModel.updateBucketListOrder(changeBucketList)
    }

    private fun successUpdateBucketOrder() {
        "버킷리스트가 새롭게 정렬되었습니다.".showToast(requireContext())
        Preference.setFilterListUp(requireContext(), SortFilter.custom)
        requireActivity().onBackPressed()
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun moved(list: List<Any>) {
        changeBucketList = list as ArrayList<BucketItem>
    }

    private fun startLoading() {
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.startLoading()
        }
    }

    private fun stopLoading() {
        if (activity is MainActivity) {
            val a = activity as MainActivity
            a.stopLoading()
        }
    }

}