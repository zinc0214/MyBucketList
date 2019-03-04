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
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.FragmentBucketWriteBinding
import womenproject.com.mybury.databinding.FragmentDdayListBinding
import womenproject.com.mybury.viewmodels.BucketWriteViewModel
import womenproject.com.mybury.viewmodels.DdayBucketTotalListViewModel

class BucketWriteFragment : BaseFragment() {

    private lateinit var bucketWriteViewwModel: BucketWriteViewModel
    private lateinit var binding: FragmentBucketWriteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        bucketWriteViewwModel = BucketWriteViewModel(context)

        binding = DataBindingUtil.inflate<FragmentBucketWriteBinding>(
                inflater, R.layout.fragment_bucket_write, container, false)


        binding.apply {

            initBucketListUI()

        }


        return binding.root
    }


    private fun initBucketListUI() {

        binding.memoImgAddListener = memoImgAddOnClickListener()

        /*bucketWriteViewwModel.getDdayEachBucketList(object : DdayBucketTotalListViewModel.OnDdayBucketListGetEvent {
            override fun start() {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun finish(bucketList: BucketList?) {
                if (bucketList != null) {
                    binding.progressBar.visibility = View.GONE
                    binding.ddayEachBucketList.adapter = DdayBucketTotalListAdapter(context, bucketList)
                }
            }
        })*/
    }


    private fun memoImgAddOnClickListener() : View.OnClickListener {
        return View.OnClickListener {
            val filterDialogFragment = WriteMemoImgAddDialogFragment.instance()
            filterDialogFragment.show(activity!!.supportFragmentManager, "tag")
        }
    }
}