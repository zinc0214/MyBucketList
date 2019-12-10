package womenproject.com.mybury.presentation.main

import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.databinding.FragmentMainBinding
import womenproject.com.mybury.presentation.main.bucketlist.MainBucketListAdapter
import womenproject.com.mybury.presentation.main.MainFragmentDirections
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.presentation.viewmodels.MainFragmentViewModel
import kotlinx.android.synthetic.main.fragment_base_dialog.*
import womenproject.com.mybury.data.Preference.Companion.getAccessToken
import womenproject.com.mybury.data.Preference.Companion.getFilterForShow
import womenproject.com.mybury.data.Preference.Companion.getFilterListUp
import womenproject.com.mybury.data.Preference.Companion.getUserId
import java.util.*


/**
 * Created by HanAYeon on 2018. 11. 26..
 */

class MainFragment : BaseFragment<FragmentMainBinding, BucketInfoViewModel>() {


    override val layoutResourceId: Int
        get() = R.layout.fragment_main

    override val viewModel: BucketInfoViewModel
        get() = BucketInfoViewModel()

    override fun initDataBinding() {
        viewDataBinding.mainToolbar.filterClickListener = createOnClickFilterListener()
        viewDataBinding.mainBottomSheet.writeClickListener = createOnClickWriteListener()
        viewDataBinding.mainBottomSheet.myPageClickListener = createOnClickMyPageListener()

        initBucketListUI()
    }

    private fun initBucketListUI() {
        val layoutManager = LinearLayoutManager(context)

        viewDataBinding.bucketList.layoutManager = layoutManager
        viewDataBinding.bucketList.hasFixedSize()

        viewModel.getMainBucketList(object : BucketInfoViewModel.OnBucketListGetEvent {
            override fun fail() {
                stopLoading()
                Toast.makeText(context, "아이쿠, 데이터가 없나봐요! 그래서 더미 데이터를 준비했습니다!", Toast.LENGTH_SHORT).show()
                /* val list = viewModel.getDummyMainBucketList()
                 list.last().isLast = true
                 viewDataBinding.bucketList.adapter = MainBucketListAdapter(context, list)*/
            }

            override fun start() {
                startLoading()
            }

            override fun finish(bucketList: List<BucketItem>) {
                if (bucketList.isEmpty()) {
                    viewDataBinding.blankImg.visibility = View.VISIBLE
                    viewDataBinding.bucketList.visibility = View.GONE
                } else {
                    viewDataBinding.blankImg.visibility = View.GONE
                    viewDataBinding.bucketList.visibility = View.VISIBLE
                    viewDataBinding.bucketList.adapter = MainBucketListAdapter(context, bucketList)
                }
                stopLoading()

            }
        }, getUserId(context!!), getAccessToken(context!!), getFilterForShow(context!!), getFilterListUp(context!!))

        //   viewDataBinding.bucketList.adapter = MainBucketListAdapter(context, viewModel.getMainBucketList())
        //  viewDataBinding.progressBar.visibility = View.GONE

    }

    private val filterChangedListener: () -> Unit = {
        initBucketListUI()
    }

    private fun createOnClickWriteListener(): View.OnClickListener {
        return View.OnClickListener {
            val directions = MainFragmentDirections.actionMainBucketToBucketWrite()
            it.findNavController().navigate(directions)
        }
    }

    private fun createOnClickFilterListener(): View.OnClickListener {
        return View.OnClickListener {
            val filterDialogFragment = FilterDialogFragment(filterChangedListener)
            filterDialogFragment.show(activity!!.supportFragmentManager, "tag")
        }
    }

    private fun createOnClickMyPageListener(): View.OnClickListener {
        return View.OnClickListener {
            val directions = MainFragmentDirections.actionMainBucketToMyPage()
            it.findNavController().navigate(directions)
        }
    }

    private fun createOnClickDdayListener(): View.OnClickListener {
        return View.OnClickListener {
            val directions = MainFragmentDirections.actionMainBucketToMyPage()
            it.findNavController().navigate(directions)
        }
    }


}