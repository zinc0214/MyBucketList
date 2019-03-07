package womenproject.com.mybury.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.adapter.DdayBucketTotalListAdapter
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.FragmentBucketWriteBinding
import womenproject.com.mybury.databinding.FragmentDdayListBinding
import womenproject.com.mybury.viewmodels.BucketWriteViewModel
import womenproject.com.mybury.viewmodels.DdayBucketTotalListViewModel

class BucketWriteFragment : BaseFragment<FragmentBucketWriteBinding, BucketWriteViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_bucket_write

    override val viewModel: BucketWriteViewModel
        get() = BucketWriteViewModel()

    override fun initStartView() {
        viewDataBinding.memoImgAddListener = memoImgAddOnClickListener()
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }

    private fun memoImgAddOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            val filterDialogFragment = WriteMemoImgAddDialogFragment.instance()
            filterDialogFragment.show(activity!!.supportFragmentManager, "tag")
        }
    }
}