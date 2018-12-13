package womenproject.com.mybury.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import womenproject.com.mybury.adapter.MainBucketListAdapter
import womenproject.com.mybury.databinding.FragmentMainBinding


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class MainFragment : BaseFragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var bucketList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)

        binding.bucketList.layoutManager = layoutManager
        binding.bucketList.hasFixedSize()
        binding.bucketList.adapter = MainBucketListAdapter(context)
        binding.bucketList.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        return binding.root
    }

}