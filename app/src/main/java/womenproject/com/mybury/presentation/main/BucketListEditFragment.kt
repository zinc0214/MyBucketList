package womenproject.com.mybury.presentation.main

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.FragmentBucketEditBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.main.bucketlist.EditBucketListAdapter
import womenproject.com.mybury.presentation.mypage.categoryedit.ItemTouchHelperCallback
import womenproject.com.mybury.presentation.viewmodels.BucketEditViewModel
import womenproject.com.mybury.ui.ItemDragListener
import womenproject.com.mybury.ui.ItemMovedListener

class BucketListEditFragment : BaseFragment<FragmentBucketEditBinding, BucketEditViewModel>(),
    ItemDragListener,
    ItemMovedListener {

    private lateinit var itemTouchHelper: ItemTouchHelper
    private var changeBucketList = listOf<BucketItem>()
    private var originBucketList = listOf<BucketItem>()

    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_edit

    override val viewModel: BucketEditViewModel
        get() = BucketEditViewModel()

    override fun initDataBinding() {
        getMainBucketList()
    }

    private fun getMainBucketList() {
        viewModel.getMainBucketList(object : BaseViewModel.MoreCallBackAny {
            override fun start() {
                startLoading()
            }

            override fun success(value: Any) {
                stopLoading()
                val response = value as BucketList
                originBucketList = response.bucketlists
                changeBucketList = response.bucketlists
                setBucketListAdapter()
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(requireActivity().supportFragmentManager)
            }

            override fun restart() {
                getMainBucketList()
            }

        })
    }

    private fun setBucketListAdapter() {
        Log.e("ayhan", "changeBucketList : ${changeBucketList.size}")
        val editBucketListAdapter = EditBucketListAdapter(
            changeBucketList,
            this,
            this
        )

        viewDataBinding.bucketEditListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = editBucketListAdapter
        }

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(editBucketListAdapter))
        itemTouchHelper.attachToRecyclerView(viewDataBinding.bucketEditListView)

    }


    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun moved(list: List<Any>) {
        changeBucketList = list as ArrayList<BucketItem>
    }
}