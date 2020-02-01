package womenproject.com.mybury.presentation.main

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.Preference.Companion.getFilterForShow
import womenproject.com.mybury.data.Preference.Companion.getFilterListUp
import womenproject.com.mybury.databinding.FragmentMainBinding
import womenproject.com.mybury.presentation.NetworkFailDialog
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.main.bucketlist.MainBucketListAdapter
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel


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

        getMainBucketList()

    }

    private fun getMainBucketList() {
        viewModel.getMainBucketList(object : BaseViewModel.MoreCallBackAnyList {
            override fun restart() {
                getMainBucketList()
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(activity!!.supportFragmentManager)
            }

            override fun start() {
                startLoading()
            }

            override fun success(value: List<Any>) {
                if (value.isEmpty()) {
                    viewDataBinding.blankImg.visibility = View.VISIBLE
                    viewDataBinding.bucketList.visibility = View.GONE
                    viewDataBinding.endImage.visibility = View.GONE
                } else {
                    viewDataBinding.blankImg.visibility = View.GONE
                    viewDataBinding.bucketList.visibility = View.VISIBLE
                    viewDataBinding.endImage.visibility = View.VISIBLE
                    viewDataBinding.bucketList.adapter = MainBucketListAdapter(value as List<BucketItem>)
                }
                stopLoading()

            }
        }, getFilterForShow(context!!), getFilterListUp(context!!))

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