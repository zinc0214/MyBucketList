package womenproject.com.mybury.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.adapter.MainBucketListAdapter
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.databinding.FragmentMainBinding
import womenproject.com.mybury.viewmodels.MainFragmentViewModel


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class MainFragment : BaseFragment() {

    private lateinit var mainFragmentViewModel: MainFragmentViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mainFragmentViewModel = MainFragmentViewModel(context)

        binding = DataBindingUtil.inflate<FragmentMainBinding>(
                inflater, R.layout.fragment_main, container, false)

        binding.apply {
            viewModel = mainFragmentViewModel
            mainToolbar.filterClickListener = createOnClickFilterListener()
            mainBottomSheet.writeClickListener = createOnClickWriteListener()
            mainBottomSheet.noneClickListener = createOnClickDdayListener()

            initBucketListUI()
        }

        return binding.root
    }


    private fun initBucketListUI () {
        val layoutManager = LinearLayoutManager(context)

        binding.bucketList.layoutManager = layoutManager
        binding.bucketList.hasFixedSize()

        binding.bucketList.adapter = MainBucketListAdapter(context, mainFragmentViewModel.getMainBucketList())

        binding.progressBar.visibility = View.GONE

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