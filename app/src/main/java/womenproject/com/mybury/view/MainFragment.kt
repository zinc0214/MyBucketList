package womenproject.com.mybury.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.fragment_main.*
import womenproject.com.mybury.adapter.MainBucketListAdapter
import womenproject.com.mybury.databinding.FragmentMainBinding


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var bucketList : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)
       // val adapter = MainBucketListAdapter()
       // binding.bucketList.adapter = adapter

        val layoutManager = LinearLayoutManager(context)

      //  bucketList = bucket_list as RecyclerView
       // bucketList.layoutManager = layoutManager
       // bucketList.hasFixedSize()
       // bucketList.adapter = MainBucketListAdapter()

        binding.bucketList.layoutManager = layoutManager
       binding.bucketList.hasFixedSize()
        binding.bucketList.adapter = MainBucketListAdapter()
        binding.bucketList.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        return binding.root
    }

}