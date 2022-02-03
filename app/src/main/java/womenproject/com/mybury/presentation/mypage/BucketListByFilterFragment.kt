package womenproject.com.mybury.presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import womenproject.com.mybury.R
import womenproject.com.mybury.data.BucketItem
import womenproject.com.mybury.data.BucketList
import womenproject.com.mybury.data.Preference.Companion.getFilterListUp
import womenproject.com.mybury.data.ShowFilter
import womenproject.com.mybury.databinding.FragmentBucketListByCategoryBinding
import womenproject.com.mybury.presentation.base.BaseFragment
import womenproject.com.mybury.presentation.base.BaseViewModel
import womenproject.com.mybury.presentation.dialog.NetworkFailDialog
import womenproject.com.mybury.presentation.viewmodels.BucketInfoViewModel
import womenproject.com.mybury.ui.snackbar.MainSnackBarWidget

@AndroidEntryPoint
class BucketListByFilterFragment : BaseFragment() {

    private lateinit var binding: FragmentBucketListByCategoryBinding
    private lateinit var filterType: String
    private val viewModel by viewModels<BucketInfoViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bucket_list_by_category, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        arguments?.let {
            val args = BucketListByFilterFragmentArgs.fromBundle(it)
            val filter = args.filter
            this.filterType = filter!!
        }

        binding.headerLayout.title =
            when (filterType) {
                ShowFilter.started.toString() -> "진행중"
                else -> "완료"
            }
        binding.headerLayout.backBtnOnClickListener = backBtnOnClickListener()

        initBucketListUI()
    }

    private fun initBucketListUI() {
        val layoutManager = LinearLayoutManager(context)

        binding.bucketList.layoutManager = layoutManager
        binding.bucketList.hasFixedSize()

        getFilterListUp(requireContext())?.let {
            viewModel.getMainBucketList(object : BaseViewModel.MoreCallBackAny {
                override fun restart() {
                    initBucketListUI()
                }

                override fun fail() {
                    stopLoading()
                }

                override fun start() {
                    startLoading()
                }

                override fun success(value: Any) {
                    val response = value as BucketList
                    binding.bucketList.adapter =
                        CategoryBucketListAdapter(response.bucketlists, showSnackBar)
                    stopLoading()
                }
            }, filterType, it)
        }


    }

    private fun setBucketCancel(bucketId: String) {
        viewModel.setBucketCancel(object : BaseViewModel.Simple3CallBack {
            override fun restart() {
                setBucketCancel(bucketId)
            }

            override fun fail() {
                stopLoading()
                NetworkFailDialog().show(requireActivity().supportFragmentManager)
            }

            override fun start() {
                startLoading()
            }

            override fun success() {
                initBucketListUI()
            }

        }, bucketId)

    }

    private fun bucketCancelListener(info: BucketItem) = View.OnClickListener {
        setBucketCancel(info.id)
    }

    private val showSnackBar: (BucketItem) -> Unit = { info: BucketItem ->
        showCancelSnackBar(requireView(), info)
    }

    private fun showCancelSnackBar(view: View, info: BucketItem) {
        val countText = if (info.goalCount > 1) "\" ${info.userCount}회 완료" else " \" 완료"
        MainSnackBarWidget.make(view, info.title, countText, bucketCancelListener(info))?.show()
    }

}