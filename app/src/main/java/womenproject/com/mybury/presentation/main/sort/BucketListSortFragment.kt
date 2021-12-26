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
import womenproject.com.mybury.databinding.FragmentBucketSortBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.mypage.categoryedit.ItemTouchHelperCallback
import womenproject.com.mybury.presentation.viewmodels.BucketEditViewModel
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.ui.ItemMovedListener

class BucketListSortFragment : Fragment(),
    ItemDragListener,
    ItemMovedListener {

    private lateinit var viewModel: BucketEditViewModel
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
        viewModel = BucketEditViewModel()
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
        viewModel.loadState.observe(viewLifecycleOwner) {
            when (it) {
                BaseViewModel.LoadState.START -> {
                    startLoading()
                }
                BaseViewModel.LoadState.FAIL -> {
                    stopLoading()
                }
                BaseViewModel.LoadState.RESTART -> {
                    getMainBucketList()
                }
                else -> {
                    stopLoading()
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
        binding.toolBarLayout.setConfirmClickListener { updateBucketSort() }
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

    private fun updateBucketSort() {

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