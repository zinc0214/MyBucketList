package womenproject.com.mybury.presentation.main.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.Preference
import womenproject.com.mybury.data.SortFilter
import womenproject.com.mybury.data.model.LoadState
import womenproject.com.mybury.databinding.FragmentBucketSortBinding
import womenproject.com.mybury.presentation.MainActivity
import womenproject.com.mybury.presentation.dialog.LoadFailDialog
import womenproject.com.mybury.presentation.viewmodels.BucketSortViewModel
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.util.showToast

@AndroidEntryPoint
class BucketListSortFragment : Fragment() {

    private val viewModel by viewModels<BucketSortViewModel>()
    private lateinit var binding: FragmentBucketSortBinding

    private lateinit var itemTouchHelper: ItemTouchHelper
    private var filterForShow: String? = null
    private var filterListUp: String? = null

    private val editBucketListAdapter =
        SortBucketListAdapter(dragListener = object : ItemDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                itemTouchHelper.startDrag(viewHolder)
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bucket_sort, container, false)
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
                LoadState.START -> {
                    startLoading()
                }

                LoadState.FAIL -> {
                    stopLoading()
                    LoadFailDialog { }
                }

                LoadState.RESTART -> {
                    getMainBucketList()
                }

                LoadState.SUCCESS -> {
                    stopLoading()
                }

                else -> {
                    // Do Nothing
                }
            }
        }

        viewModel.bucketUpdateState.observe(viewLifecycleOwner) {
            when (it) {
                LoadState.START -> {
                    startLoading()
                }

                LoadState.FAIL -> {
                    stopLoading()
                    LoadFailDialog { }
                }

                LoadState.RESTART -> {
                    updateBucketOrder()
                }

                LoadState.SUCCESS -> {
                    stopLoading()
                    successUpdateBucketOrder()
                }

                else -> {
                    // Do Nothing
                }
            }
        }
        viewModel.allBucketResult.observe(viewLifecycleOwner) {
            editBucketListAdapter.updateBucketList(it)
            setUpViews()
        }
    }

    private fun setUpViews() {
        setBucketListAdapter()
        binding.toolBarLayout.setConfirmClickListener { updateBucketOrder() }
    }

    private fun setBucketListAdapter() {
        binding.bucketEditListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = editBucketListAdapter
        }

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(editBucketListAdapter))
        itemTouchHelper.attachToRecyclerView(binding.bucketEditListView)
    }

    private fun updateBucketOrder() {
        viewModel.updateBucketListOrder(editBucketListAdapter.getBucketList())
    }

    private fun successUpdateBucketOrder() {
        "버킷리스트가 새롭게 정렬되었습니다.".showToast(requireContext())
        Preference.setFilterListUp(requireContext(), SortFilter.custom)
        requireActivity().onBackPressed()
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