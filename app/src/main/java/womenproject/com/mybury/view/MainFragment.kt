package womenproject.com.mybury.view

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.adapter.MainBucketListAdapter
import womenproject.com.mybury.base.BaseFragment
import womenproject.com.mybury.databinding.FragmentMainBinding
import womenproject.com.mybury.viewmodels.MainFragmentViewModel


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class MainFragment : BaseFragment<FragmentMainBinding, MainFragmentViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_main

    override val viewModel: MainFragmentViewModel
        get() = MainFragmentViewModel()

    override fun initStartView() {
        viewDataBinding.mainToolbar.filterClickListener = createOnClickFilterListener()
        viewDataBinding.mainBottomSheet.writeClickListener = createOnClickWriteListener()
        viewDataBinding.mainBottomSheet.noneClickListener = createOnClickDdayListener()

        initBucketListUI()
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }


    private fun initBucketListUI () {
        val layoutManager = LinearLayoutManager(context)

        viewDataBinding.bucketList.layoutManager = layoutManager
        viewDataBinding.bucketList.hasFixedSize()

        viewDataBinding.bucketList.adapter = MainBucketListAdapter(context, viewModel.getMainBucketList())

        viewDataBinding.progressBar.visibility = View.GONE

       /* mainFragmentViewModel.getMainBucketList(object : MainFragmentViewModel.OnBucketListGetEvent{
            override fun start() {
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun finish(bucketList: BucketList?) {
                if(bucketList != null)  {
                    binding.progressBar.visibility = View.GONE
                    binding.bucketList.adapter = MainBucketListAdapter(context, bucketList)
                }
            }
        })*/

    }

    private fun createOnClickWriteListener(): View.OnClickListener {
        return View.OnClickListener {
            val directions = MainFragmentDirections.actionMainBucketToBucketWrite()
            it.findNavController().navigate(directions)
        }
    }

    private fun createOnClickFilterListener() : View.OnClickListener {
        return View.OnClickListener {
            val filterDialogFragment = FilterDialogFragment.instance()
            filterDialogFragment.show(activity!!.supportFragmentManager, "tag")
        }
    }

    private fun createOnClickDdayListener() : View.OnClickListener {
        return View.OnClickListener {
            val directions = MainFragmentDirections.actionMainBucketToDdayBucket()
            it.findNavController().navigate(directions)
        }
    }
}