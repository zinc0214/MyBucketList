package womenproject.com.mybury.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import womenproject.com.mybury.R
import womenproject.com.mybury.adapter.MainBucketListAdapter
import womenproject.com.mybury.databinding.FragmentMainBinding
import womenproject.com.mybury.viewmodels.MainFragmentViewModel


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class MainFragment : BaseFragment() {

    private lateinit var mainFragmentViewModel: MainFragmentViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var bucketList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mainFragmentViewModel = MainFragmentViewModel()

        binding = DataBindingUtil.inflate<FragmentMainBinding>(
                inflater, R.layout.fragment_main, container, false)

        binding.apply {

            viewModel = mainFragmentViewModel
            this@MainFragment.bucketList = binding.bucketList
            writeClickListener = createOnClickWriteListener()
            filterClickListener = createOnClickFilterListener()

            initBucketListUI()
        }

        return binding.root
    }


    private fun initBucketListUI () {
        val layoutManager = LinearLayoutManager(context)

        bucketList.layoutManager = layoutManager
        bucketList.hasFixedSize()
        bucketList.adapter = MainBucketListAdapter(context, mainFragmentViewModel.getMainBucketList())
    }

    private fun createOnClickWriteListener(): View.OnClickListener {
        return View.OnClickListener {
            val directions = MainFragmentDirections.ActionMainBucketToBucketWrite()
            it.findNavController().navigate(directions)
        }
    }

    private fun createOnClickFilterListener() : View.OnClickListener {
        return View.OnClickListener {

            val filterDialogFragment = FilterDialogFragment.Instance("FilterDailog")
            filterDialogFragment.show(activity!!.supportFragmentManager, "tag")
        }
    }

}